package com.conference.service.impl;

import com.conference.entity.Booking;
import com.conference.entity.MeetingRoom;
import com.conference.mapper.BookingMapper;
import com.conference.mapper.MeetingRoomMapper;
import com.conference.service.BookingService;
import com.conference.utils.Result;
import com.conference.utils.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    private MeetingRoomMapper meetingRoomMapper;

    /**
     * 创建会议室预订订单
     * @param booking 包含会议室ID、客户ID、预订时间等信息的预订对象
     * @return 返回操作结果，包含创建的预订信息或错误信息
     */
    @Override
    @Transactional
    public Result createBooking(Booking booking) {
//        try {
//            // 计算总价格
//            MeetingRoom room = meetingRoomMapper.findByRoomId(booking.getRoomId());
//            if (room == null) {
//                return Result.error("会议室不存在");
//            }
//
//            // 检查会议室状态
//            if (Status.UNDER_REPAIR.equals(room.getStatusName())) {
//                return Result.error("会议室正在维修中，暂不可预订");
//            }
//
//            if (!Status.AVAILABLE.equals(room.getStatusName())) {
//                return Result.error("会议室当前不可用");
//            }
//
//            booking.setRoomName(room.getRoomName());
//            booking.setTotalPrice(room.getHourlyRate() * booking.getTotalHours());
//            booking.setStatusName(Status.UNPAID);
//            booking.setPaymentStatus(false);
//            booking.setCreateTime(LocalDateTime.now());
//
//            // 保存预订记录
//            bookingMapper.insert(booking);
//
//            // 更新会议室状态为"已锁定"
////            room.setStatusName(Status.UNAVAILABLE);
////            meetingRoomMapper.updateRoom(room);
//
//            return Result.success(booking);
//        } catch (Exception e) {
//            throw new RuntimeException("创建预订失败", e);
//        }

        try {
            // 1. 获取会议室信息
            MeetingRoom room = meetingRoomMapper.findByRoomId(booking.getRoomId());
            if (room == null) {
                return Result.error("会议室不存在");
            }

            // 2. 检查会议室状态（包括维修状态）
            if (Status.UNDER_REPAIR.equals(room.getStatusName())) {
                return Result.error("会议室正在维修中，暂不可预订");
            }
            if (!Status.AVAILABLE.equals(room.getStatusName())) {
                return Result.error("会议室当前不可用");
            }

            // 3. 检查时间冲突
            if (meetingRoomMapper.hasTimeConflict(booking.getRoomId(), booking.getBookingDate(),
                    booking.getStartTime().getHour(), booking.getEndTime().getHour())) {
                return Result.error("该时间段会议室已被预订");
            }

            // 4. 设置预订信息
            booking.setRoomName(room.getRoomName());
            booking.setTotalPrice(room.getHourlyRate() * booking.getTotalHours());
            booking.setStatusName(Status.UNPAID);
            booking.setPaymentStatus(false);
            booking.setCreateTime(LocalDateTime.now());

            // 5. 保存预订记录
            bookingMapper.insert(booking);

            return Result.success(booking);
        } catch (Exception e) {
            throw new RuntimeException("创建预订失败，请稍后重试");
        }
    }

    /**
     * 处理会议室预订支付
     * @param bookingId 预订订单ID
     * @param paymentMethod 支付方式
     * @return 返回支付结果，包含更新后的预订信息或错误信息
     */
    @Override
    @Transactional
    public Result payBooking(Integer bookingId, String paymentMethod) {
        Booking booking = bookingMapper.selectById(bookingId);
        if (booking == null) {
            return Result.error("订单不存在");
        }

        if (Status.PAID.equals(booking.getStatusName())) {
            return Result.error("订单已支付");
        }

        if (isBookingExpired(bookingId)) {
            // 订单已过期，更新状态为"已取消"
            bookingMapper.updateStatusToCancel(bookingId);
            // 并释放会议室
            meetingRoomMapper.updateRoomStatus(booking.getRoomId(), Status.AVAILABLE);
            return Result.error("订单已过期");
        }

        // 模拟支付处理
        try {
            // 更新订单状态
            booking.setStatusName(Status.PAID);
            booking.setPaymentStatus(true);
            booking.setRoomStatus(Status.BOOKED);
            bookingMapper.updateBooking(booking);


            return Result.success(booking);
        } catch (Exception e) {
            throw new RuntimeException("支付处理失败", e);
        }
    }

    /**
     * 根据客户ID查询其所有预订记录
     * @param customerId 客户ID
     * @return 返回该客户的所有预订记录列表
     */
    @Override
    public Result<List<Booking>> listByCustomerId(Integer customerId) {
        List<Booking> bookings = bookingMapper.selectByCustomerId(customerId);
        return Result.success(bookings);
    }

    /**
     * 获取预订订单详细信息
     * @param bookingId 预订订单ID
     * @return 返回订单详细信息或错误提示
     */
    @Override
    public Result<Booking> getBookingDetail(Integer bookingId) {
        Booking booking = bookingMapper.selectById(bookingId);
        if (booking == null) {
            return Result.error("订单不存在");
        }
        return Result.success(booking);
    }

    @Override
    public Result<List<Booking>> listByCustomerIdforemployee(LocalDate bookingDate, Integer startHour, Integer endHour) {
        List<Booking> bookings = bookingMapper.selectbyConditions(bookingDate, startHour, endHour);
        System.out.println(bookings);
        return Result.success(bookings);
    }

    @Override
    public Result updateBookingStatus(Booking booking) {

        // 1. 检查预订记录是否存在
        Booking existingBooking = bookingMapper.selectById(booking.getBookingId());
        if (existingBooking == null) {
            return Result.error("预订记录不存在");
        }
        // 2. 更新预订状态
        int updated = bookingMapper.updateBookingStatus(booking);
        if (updated <= 0) {
            return Result.error("更新预订状态失败");
        }

        return Result.success("更新预订状态成功");
    }


    /**
     * 检查预订订单是否已过期（超过30分钟未支付）
     * @param bookingId 预订订单ID
     * @return true表示已过期，false表示未过期
     */
    @Override
    public boolean isBookingExpired(Integer bookingId) {
        Booking booking = bookingMapper.selectById(bookingId);
        System.out.println(booking);
        if (booking == null) return true;

        // 检查是否超过30分钟未支付
        return LocalDateTime.now().isAfter(booking.getCreateTime().plusMinutes(30))
                && Status.UNPAID.equals(booking.getStatusName());
    }


    /**
     * 定时任务：每分钟检查并释放过期未支付的预订
     * 自动将过期订单状态改为"已取消"，并释放关联的会议室
     */
//    @Override
//    @Scheduled(fixedRate = 60000) // 每分钟检查一次
//    public void checkAndReleaseExpiredBookings() {
//        List<Booking> expiredBookings = bookingMapper.selectExpiredUnpaidBookings(
//                LocalDateTime.now().minusMinutes(30));
//
//        for (Booking booking : expiredBookings) {
//            // 更新订单状态为"已取消"
//            booking.setStatusName(Status.CANCEL);
//            bookingMapper.updateBooking(booking);
//
//            // 释放会议室
//            MeetingRoom room = meetingRoomMapper.findByRoomId(booking.getRoomId());
//            room.setStatusName(Status.AVAILABLE);
//            meetingRoomMapper.updateRoom(room);
//        }
//    }
}
