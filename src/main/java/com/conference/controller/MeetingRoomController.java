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

    /** 查询所有会议室列表
     *  url地址：meetingroom
     *  请求方式：GET
     *  响应数据：
     *  {
     *      "code": 0,
     *      "message":"success",
     *      "data":[
     *          meeting1:{},
     *          meeting2:{},
     *          ...
     *      ]
     *  }
     */
    @GetMapping
    public Result<List<MeetingRoom>> list() {
        Result result = meetingRoomService.list();
        return result;
    }

    /** 根据roomId查询会议室配置信息
     *  url地址：meetingroom/roomDetail
     *  请求方式：GET
     *  请求参数：
     *  {
     *      "roomId":会议室编号
     *  }
     *  响应数据：
     *  {
     *      "code": 0,
     *      "message":"success",
     *      "data":{
     *          'roomName':'会议室名称',
     *          'roomType':'会议室类型'
     *          'capacity':'会议室容纳人数',
     *          'multimediaSupport':'是否支持多媒体设备',
     *          'hourlyRate':'每小时费用',
     *          'statusName':'会议室状态'
     *      }
     *  }
     */
    @GetMapping("roomDetail")
    public Result<MeetingRoom> showRoomDetail(Integer roomId){
        Result result = meetingRoomService.findByRoomId(roomId);
        return result;
    }

    /** 增加会议室
     *  url地址：meetingroom
     *  请求方式：POST
     *  请求参数：
     *  {
     *      "roomName":"会议室名称",
     *      "roomType":"会议室类型",
     *      "capacity":"会议室容纳人数",
     *      "multimediaSupport":"是否支持多媒体设备",
     *      "hourlyRate":"每小时费用",
     *      "statusName":"会议室状态"
     *  }
     *  响应数据：
     *  {
     *      "code": 0,
     *      "message":"success",
     *      "data":{}
     *  }
     */
    @PostMapping
    public Result addMeetingRoom(@RequestBody MeetingRoom meetingRoom){
        meetingRoomService.addMeetingRoom(meetingRoom);
        return Result.success();
    }

    /** 修改会议室配置信息
     *  url地址：meetingroom
     *  请求方式：PUT
     *  请求参数：
     *  {
     *      "roomId":"会议室编号",
     *      "roomName":"会议室名称",
     *      "roomType":"会议室类型",
     *      "capacity":"会议室容纳人数",
     *      "multimediaSupport":"是否支持多媒体设备",
     *      "hourlyRate":"每小时费用",
     *      "statusName":"会议室状态"
     *  }
     */
    @PutMapping
    public Result updateRoom(@RequestBody MeetingRoom meetingRoom){
        meetingRoomService.updateRoom(meetingRoom);
        return Result.success();
    }

    /** 删除会议室
     *  url地址：meetingroom
     *  请求方式：DELETE
     *  请求参数：
     *  {
     *      "roomId":"会议室编号"
     *  }
     *  响应数据：
     *  {
     *      "code": 0,
     *      "message":"success",
     *      "data":{}
     *  }
     */
    @DeleteMapping
    public Result deleteRoom(Integer roomId){
        meetingRoomService.deleteRoom(roomId);
        return Result.success();
    }
}
