package com.conference.entity;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MeetingRoom {
    @NotNull
    private Integer roomId; // 会议室编号
    @NotNull
    private String roomName; // 会议室名称，如"大会议室301"
    @NotNull
    private String roomType; // 会议室类型，代指面积大小，如教室型，圆桌型等
    @NotNull
    private Integer capacity; // 会议室最大容纳人数
    @NotNull
    private Boolean multimediaSupport; // 是否支持多媒体设备（若支持则多媒体设备全部支持，否则只有会议室的基础设施）
    @NotNull
    private Double hourlyRate; // 每小时价格
    @NotNull
    private String statusName; // 会议室当前状态（预订订单创建成功则置为“已锁定”，支付成功则置为“已被预订”）
}
