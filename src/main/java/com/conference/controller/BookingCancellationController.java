package com.conference.controller;


import com.conference.entity.BookingCancellation;
import com.conference.service.BookingCancellationService;
import com.conference.utils.Result;
import com.conference.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("cancellation")
public class BookingCancellationController {

    @Autowired
    private BookingCancellationService bookingCancellationService;


    /**
    * 获取退费规则（客户端使用）
    */
    @GetMapping("/refundRules")
    public Result<String> getRefundRules() {
        String refundRulesText = """
            取消退费规则：
            
            1. 提前72小时以上取消，退还全额费用
            2. 提前48-72小时取消，退还75%费用
            3. 提前24-48小时取消，退还25%费用
            4. 24小时内无法取消预定，费用不予退还
            """;

        return Result.success(refundRulesText);
    }

    /**
     * 提交取消申请（客户端使用）
     */
    @PostMapping("/apply")
    public Result applyForCancellation(@RequestBody BookingCancellation cancellation) {
        String message = bookingCancellationService.applyForCancellation(cancellation);
        if (message.equals("取消申请已提交")) {
            return Result.success(message);
        } else {
            return Result.error(message);
        }
    }


    /**
     * 获取待处理的取消申请（员工端使用）
     */
    @GetMapping("/pending")
    public Result<List<BookingCancellation>> getPendingCancellations() {
        return Result.success(bookingCancellationService.getPendingCancellations());
    }

    /**
     * 处理取消申请（员工端使用）
     */
    @PostMapping("/process")
    public Result processCancellation(Integer cancellationId, Integer verificationStatus) {
        if (bookingCancellationService.processCancellation(cancellationId, verificationStatus)) {
            return Result.success("取消申请已处理");
        }
        return Result.success("取消申请已处理");
    }

    /**
     * 获取取消预订列表（客户端使用）
     */
    @GetMapping("/customer/cancellations")
    public Result getCustomerCancellations() {
        // 从ThreadLocal中获取当前登录客户ID
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer customerId = (Integer) map.get("id");

        if (customerId == null) {
            return Result.error("未获取到客户信息，请先登录");
        }

        List<BookingCancellation> cancellations = bookingCancellationService.getCancellationsByCustomer(customerId);
        return Result.success(cancellations);
    }

}
