package com.conference.mapper;



import com.conference.entity.MeetingRoom;
import com.conference.utils.Result;

import java.util.List;
import java.util.Map;

public interface MeetingRoomMapper {

    // 查询所有会议室数据
    List<MeetingRoom> list();

    // 根据roomId查询会议室信息
    MeetingRoom findByRoomId(Integer roomId);

    // 增加会议室

    // 修改会议室配置信息

    // 删除会议室
}
