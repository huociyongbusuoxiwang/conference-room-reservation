package com.conference.service.impl;

import com.conference.entity.MeetingRoom;
import com.conference.mapper.MeetingRoomMapper;
import com.conference.service.MeetingRoomService;
import com.conference.utils.Result;
import com.conference.utils.Status;
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
        try {
            // 参数校验
            if (startHour >= endHour) {
                return Result.error("结束时间必须晚于开始时间");
            }
            if (bookingDate.isBefore(LocalDate.now())) {
                return Result.error("不能预订过去日期的会议室");
            }

            // 基础条件查询（容量、多媒体设备）
            List<MeetingRoom> baseRooms = meetingRoomMapper.findRoomsByConditions(
                    capacity != null ? capacity : 0,
                    multimediaSupport != null ? multimediaSupport : false
            );

            // 过滤不可用时间段
            List<MeetingRoom> availableRooms = baseRooms.stream()
                    .filter(room -> {
                        // 状态检查（这里有问题）
                        if (Status.UNDER_REPAIR.equals(room.getStatusName())) return false;

                        // 时间冲突检查
                        return !meetingRoomMapper.hasTimeConflict(
                                room.getRoomId(),
                                bookingDate,
                                startHour,
                                endHour
                        );
                    })
                    .toList();

            return Result.success(availableRooms);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    @Override
    public boolean isRoomAvailable(Integer roomId, LocalDate bookingDate, Integer startHour, Integer endHour) {
        try {
            // 检查会议室状态
            MeetingRoom room = meetingRoomMapper.findByRoomId(roomId);
            if (room == null || !Status.AVAILABLE.equals(room.getStatusName())) {
                return false;
            }

            // 检查时间冲突
            return !meetingRoomMapper.hasTimeConflict(
                    roomId,
                    bookingDate,
                    startHour,
                    endHour
            );

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Result updateRoomStatus(Integer roomId, String statusName) {
        try {
            // 检查会议室是否存在
            MeetingRoom room = meetingRoomMapper.findByRoomId(roomId);
            if (room == null) {
                return Result.error("会议室不存在");
            }

            // 更新会议室状态
            meetingRoomMapper.updateRoomStatus(roomId, statusName);
            return Result.success("会议室状态更新成功");
        } catch (Exception e) {
            return Result.error("更新会议室状态失败: " + e.getMessage());
        }
    }
}
