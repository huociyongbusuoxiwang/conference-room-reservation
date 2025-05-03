package com.conference.mapper;



import com.conference.entity.MeetingRoom;
import com.conference.utils.Result;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface MeetingRoomMapper {

    // 查询所有会议室列表
    List<MeetingRoom> list();

    // 根据roomId查询会议室信息
    MeetingRoom findByRoomId(Integer roomId);

    // 增加会议室
    void addMeetingRoom(MeetingRoom meetingRoom);

    // 修改会议室配置信息
    void updateRoom(MeetingRoom meetingRoom);

    // 删除会议室
    void deleteRoom(Integer roomId);

    boolean hasTimeConflict(@Param("roomId") Integer roomId,
                            @Param("bookingDate") LocalDate bookingDate,
                            @Param("startHour") Integer startHour,
                            @Param("endHour") Integer endHour);

    List<MeetingRoom> findRoomsByConditions(
            @Param("minCapacity") Integer minCapacity,
            @Param("multimediaSupport") Boolean multimediaSupport
    );
}
