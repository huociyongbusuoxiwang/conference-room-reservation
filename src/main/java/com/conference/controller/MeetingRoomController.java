package com.conference.controller;


import com.conference.entity.MeetingRoom;
import com.conference.service.MeetingRoomService;
import com.conference.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("meetingroom")
public class MeetingRoomController {

    @Autowired
    private MeetingRoomService meetingRoomService;

    // 查询所有会议室信息 - 查询功能返回所有会议室的数据，于是在Result中用List对象作为data返回
    @GetMapping
    public Result<List<MeetingRoom>> list() {
        Result result = meetingRoomService.list();
        return result;
    }

    // 根据roomId查询会议室配置信息 - 在result返回配置信息
    @PostMapping("roomDetail")
    public Result showRoomDetail(Integer roomId){
        Result result = meetingRoomService.findByRoomId(roomId);
        return result;
    }

    // 增加会议室
    @PostMapping
    public Result addMeetingRoom(@RequestBody MeetingRoom meetingRoom){
        meetingRoomService.addMeetingRoom(meetingRoom);
        return Result.ok(null);
    }

    // 修改会议室配置信息
    @PutMapping
    public Result updateRoom(@RequestBody MeetingRoom meetingRoom){
        meetingRoomService.updateRoom(meetingRoom);
        return Result.ok(null);
    }

    // 删除会议室
    @DeleteMapping
    public Result deleteRoom(Integer roomId){
        meetingRoomService.deleteRoom(roomId);
        return Result.ok(null);
    }
}
