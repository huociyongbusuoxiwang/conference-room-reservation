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
    private Integer roomId; // 会议室编号
    @NotNull
    private Integer customerId; // 客户编号
    @NotNull
    private Date bookingDate; // 预约日期
    @NotNull
    private LocalDate startTime; // 预约开始时间
    @NotNull
    private LocalDate endTime; // 预约结束时间
    @NotNull
    private Integer totalHours; //  预订的总时长, 通过开始和结束二者加减得出(限制为整数)
    @NotNull
    private Double totalPrice;  // 预订的总价格
    @NotNull
    private String statusName;// 预订单状态(未支付、已支付、已完成、已取消、已退款)
    @NotNull
    private Boolean paymentStatus; // 付款状态(已付款、未付款)
    @NotNull
    private LocalDate createTime; // 预定订单创建时间

}
