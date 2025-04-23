package com.conference.service;

import com.conference.entity.MeetingRoom;
import com.conference.utils.Result;

public interface MeetingRoomService {

    // 查询所有会议室列表
    Result list();

    // 根据roomId查询会议室信息
    Result findByRoomId(Integer roomId);

    // 增加会议室
    void addMeetingRoom(MeetingRoom meetingRoom);

    // 修改会议室配置信息
    void updateRoom(MeetingRoom meetingRoom);

    // 删除会议室
    void deleteRoom(Integer roomId);

}
