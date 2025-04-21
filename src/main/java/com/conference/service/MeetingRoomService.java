package com.conference.service;

import com.conference.utils.Result;

public interface MeetingRoomService {

    // 查询所有会议室信息
    Result list();

    // 根据roomId查询会议室信息
    Result findByRoomId(Integer roomId);

    // 增加会议室

    // 修改会议室配置信息

    // 删除会议室
}
