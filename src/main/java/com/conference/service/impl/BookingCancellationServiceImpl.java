package com.conference.service.impl;

import com.conference.entity.Booking;
import com.conference.entity.BookingCancellation;
import com.conference.mapper.BookingCancellationMapper;
import com.conference.mapper.BookingMapper;
import com.conference.service.BookingCancellationService;
import com.conference.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Service
public class BookingCancellationServiceImpl implements BookingCancellationService {

    @Autowired
    private BookingCancellationMapper bookingCancellationMapper;

    @Autowired
    private BookingMapper bookingMapper;

    @Override
    public List<BookingCancellation> getPendingCancellations() {
        return bookingCancellationMapper.getPendingCancellations();
    }

    @Override
    public String applyForCancellation(BookingCancellation cancellation) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer customerId = (Integer) map.get("id");

        // 1. 获取预订信息
        Booking booking = bookingMapper.selectById(cancellation.getBookingId());
        if (booking == null) {
            throw new IllegalArgumentException("无效的预订ID");
        }

        LocalDateTime bookingStartDateTime = LocalDateTime.of(
                booking.getBookingDate(),
                booking.getStartTime()
        );
        System.out.println("预订开始时间: " + bookingStartDateTime);
        // 2. 获取预订开始时间
//        LocalTime bookingStartTime = booking.getStartTime();
        System.out.println("预订开始时间: " + bookingStartDateTime);
        LocalDateTime cancellationTime = LocalDateTime.now();
        System.out.println("取消申请时间: " + cancellationTime);

        // 3. 计算提前小时数
        long hoursBefore = Duration.between(cancellationTime, bookingStartDateTime).toHours();

        // 4. 检查是否小于24小时
        if (hoursBefore < 24) {
            return "时间已小于24小时，无法取消";
        }

        // 5. 设置取消信息
        cancellation.setCustomerId(customerId);
        cancellation.setCancellationDate(cancellationTime);
//        cancellation.setVerified(false);
        cancellation.setEmployeeId(null);
        cancellation.setApprovedAt(null);

        // 6. 计算退款金额
        float refundRate;
        if (hoursBefore >= 72) {
            refundRate = 1.0f;
        } else if (hoursBefore >= 48) {
            refundRate = 0.75f;
        } else {
            refundRate = 0.25f;
        }
        cancellation.setRefundAmount((float)(booking.getTotalPrice() * refundRate));

        // 7. 保存取消申请
        bookingCancellationMapper.insert(cancellation);
        return "取消申请已提交";
    }


    @Override
    public boolean processCancellation(Integer cancellationId, Integer verificationStatus) {
        // 1. 从线程上下文中获取当前员工ID
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer employeeId = (Integer) map.get("id");

        if (employeeId == null) {
            throw new IllegalStateException("未获取到当前员工信息，请先登录");
        }

        // 2. 验证其他参数
        if (cancellationId == null) {
            throw new IllegalArgumentException("取消申请ID不能为空");
        }
//        if (verificationStatus == null || (verificationStatus != 1 && verificationStatus != 2)) {
//            throw new IllegalArgumentException("审核状态无效 (1-通过, 2-拒绝)");
//        }

        // 3. 获取取消申请记录
        BookingCancellation cancellation = bookingCancellationMapper.selectById(cancellationId);
        if (cancellation == null) {
            throw new IllegalArgumentException("找不到对应的取消申请记录");
        }

        // 4. 检查是否已审核
        if (cancellation.getIsVerified() != 0) {
            throw new IllegalStateException("该申请已审核，无法再次审核");
        }

        // 4. 设置审核信息
        cancellation.setEmployeeId(employeeId); // 使用从上下文中获取的employeeId
        System.out.println("审核员工ID: " + employeeId);
        cancellation.setApprovedAt(LocalDateTime.now());
        cancellation.setIsVerified(verificationStatus);

//        System.out.println(cancellation);

        // 5. 更新数据库
        boolean success = bookingCancellationMapper.updateVerificationStatus(cancellation) > 0;

        // 6. 如果审核通过，执行退款和状态更新
        if (success && verificationStatus != 0) {
            bookingMapper.updateStatusToRefund(cancellation.getBookingId());
        }

        return success;
    }

    @Override
    public List<BookingCancellation> getCancellationsByCustomer(Integer customerId) {
        if (customerId == null) {
            throw new IllegalArgumentException("客户ID不能为空");
        }
        return bookingCancellationMapper.selectByCustomerId(customerId);
    }
}
