package com.conference.service.impl;

import com.conference.entity.MeetingRoom;
import com.conference.mapper.MeetingRoomMapper;
import com.conference.service.MeetingRoomService;
import com.conference.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeetingRoomServiceImpl implements MeetingRoomService {

    @Autowired
    private MeetingRoomMapper meetingRoomMapper;


    // 查询所有会议室数据
    @Override
    public Result list() {
        List<MeetingRoom> meetingRooms = meetingRoomMapper.list();
        return Result.ok(meetingRooms);
    }

    // 根据roomId查询会议室信息
    @Override
    public Result findByRoomId(Integer roomId) {
        MeetingRoom data = meetingRoomMapper.findByRoomId(roomId);
        return Result.ok(data);
    }


    // 增加会议室

    // 修改会议室配置信息

    // 删除会议室

}
