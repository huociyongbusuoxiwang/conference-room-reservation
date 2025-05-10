package com.conference.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

// 已支付费用但取消预订的订单

@Data
public class BookingCancellation {
    @NotNull
    private Integer cancellationId; // 取消预定订单的编号
    @NotNull
    private Integer bookingId; // 被取消的预订订单的编号

    @NotNull
    private Integer customerId; // 客户编号

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime cancellationDate;// 取消预订的日期

    @NotEmpty
    private Float refundAmount;// 退款金额

    private String refundReason; // 取消原因

    @NotNull
    private Integer employeeId; // 审核取消申请的员工id

    // 不设置为非空，仅在退款申请时设置
    private Integer isVerified = 0; // 取消的订单是否通过管理员审核，默认值为0

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime approvedAt;// 审核时间
}
