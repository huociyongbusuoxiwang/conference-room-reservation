package com.conference.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

// 已支付费用但取消预订的订单

@Data
public class BookingCancellation {
    @NotNull
    private Integer cancellationId; // 取消预定订单的编号
    @NotNull
    private Integer bookingId; // 被取消的预订订单的编号
    @NotNull
    private LocalDate cancellationDate;// 取消预订的日期
    @NotNull
    private Float refundAmount;// 退款金额

    private String refundReason; // 取消原因
    @NotNull
    private Integer employeeId; // 审核取消申请的员工id
    @NotNull
    private LocalDate approvedAt;// 审核时间

    // 不设置为非空，仅在退款申请时设置
    private Integer isVerified = 0; // 取消的订单是否通过管理员审核，默认值为0
}
