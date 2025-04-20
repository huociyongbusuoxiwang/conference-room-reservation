package com.conference.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingConcellation {
    @NotNull
    private Integer cancellationId; // 取消编号
    @NotNull
    private Integer bookingId; // 预约编号
    @NotNull
    private LocalDate cancellationDate;// 取消日期
    @NotNull
    private Float refundAmount;// 退款金额

    private String refundReason; // 取消原因
    @NotNull
    private Integer employeeId; // 取消状态
    @NotNull
    private LocalDate approvedAt;// 审核时间


}
