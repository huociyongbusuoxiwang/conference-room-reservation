package com.conference.entity;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class Booking {
    @NotNull
    private Integer bookingId; // 预约编号
    @NotNull
    private Integer meetingRoomId; // 会议室编号
    @NotNull
    private Integer customerId; // 客户编号
    @NotNull
    private LocalDate startTime; // 预约开始时间
    @NotNull
    private LocalDate endTime; // 预约结束时间
    @NotNull
    private Date bookingDate; // 预约日期
    @NotNull
    private String bookingStatus; // 预约状态
    @NotNull
    private Integer Status;//预定状态
    @NotNull
    private Boolean paymentStatus; // 付款状态
    @NotNull
    private LocalDate createTime; // 创建时间

}
