package com.conference.service.impl;

import com.conference.entity.MeetingRoom;
import com.conference.mapper.MeetingRoomMapper;
import com.conference.service.MeetingRoomService;
import com.conference.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MeetingRoomServiceImpl implements MeetingRoomService {

    @Autowired
    private MeetingRoomMapper meetingRoomMapper;


    // 查询所有会议室列表
    @Override
    public Result list() {
        List<MeetingRoom> meetingRooms = meetingRoomMapper.list();
        return Result.success(meetingRooms);
    }

    // 根据roomId查询会议室信息
    @Override
    public Result findByRoomId(Integer roomId) {
        MeetingRoom room = meetingRoomMapper.findByRoomId(roomId);
        return Result.success(room);
    }

    // 增加会议室
    @Override
    public void addMeetingRoom(MeetingRoom meetingRoom) {
        meetingRoomMapper.addMeetingRoom(meetingRoom);
    }


    // 修改会议室配置信息
    @Override
    public void updateRoom(MeetingRoom meetingRoom) {
        meetingRoomMapper.updateRoom(meetingRoom);
    }

    // 删除会议室
    @Override
    public void deleteRoom(Integer roomId) {
        meetingRoomMapper.deleteRoom(roomId);
    }

    @Override
    public Result findAvailableRooms(LocalDate bookingDate, Integer startHour, Integer endHour, Integer capacity, Boolean multimediaSupport) {
        return null;
    }

    @Override
    public boolean isRoomAvailable(Integer roomId, LocalDate bookingDate, Integer startHour, Integer endHour) {
        return false;
    }
}
