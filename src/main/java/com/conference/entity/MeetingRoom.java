package com.conference.entity;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MeetingRoom {
    @NotNull
    private Integer meetingRoomId;//会议室编号
    @NotNull
    private String meetingRoomName;//会议室名称
    @NotNull
    private String meetingRoomType;//会议室类型
    @NotNull
    private Integer meetingRoomCapacity;//会议室容量
    @NotNull
    private Boolean multimedia_support;//是否支持多媒体
    @NotNull
    private Float hourly_rate;//每小时价格
    @NotNull
    private String meetingRoomStatus;//状态
}
