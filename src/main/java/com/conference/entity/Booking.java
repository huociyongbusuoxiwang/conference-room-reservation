package com.conference.entity;


import com.conference.utils.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class Booking {
    @NotNull
    private Integer bookingId; // 预约编号
    @NotNull
    private Integer roomId; // 会议室编号
    @NotNull
    private Integer customerId; // 客户编号

    private String roomName; // 新增会议室名称字段

    @DateTimeFormat(pattern = "yyyy-MM-dd") // 从前端传递给后端的时间限制
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8") // 从数据库读取时间时的格式限制，需要指定时区，否则会出错
    private LocalDate bookingDate; // 预约日期

    // 客户最多可提前 60 天进行会议室预约，逾期不可预约
    // 会议室可使用时间为每日 8:00 - 21:00，使用时间单位为小时

    @DateTimeFormat(pattern = "HH")
    @JsonFormat(pattern = "HH", timezone = "GMT+8")
    private LocalTime startTime; // 预约开始时间 - 限制为小时(整数0-23)

    @DateTimeFormat(pattern = "HH")
    @JsonFormat(pattern = "HH", timezone = "GMT+8")
    private LocalTime endTime; // 预约结束时间 - 限制为小时(整数0-23)

    private Integer totalHours; //  预订的总时长, 通过开始和结束二者加减得出(限制为整数)

    @NotNull
    private Double totalPrice;  // 预订的总价格

    @NotEmpty
    private String statusName = Status.UNPAID; // 预订单状态((默认)未支付、已支付、已完成、已取消、已退款)

    @NotNull
    private String roomStatus; // 会议室状态

    @NotNull
    private Boolean paymentStatus; // 付款状态(已付款、未付款)

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime; // 预定订单创建时间

}
