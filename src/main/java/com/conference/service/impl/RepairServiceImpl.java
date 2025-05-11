package com.conference.service.impl;


import com.conference.entity.MeetingRoom;
import com.conference.entity.Repair;
import com.conference.mapper.MeetingRoomMapper;
import com.conference.mapper.RepairMapper;
import com.conference.service.RepairService;
import com.conference.utils.Result;
import com.conference.utils.Status;
import com.conference.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class RepairServiceImpl implements RepairService {

    @Autowired
    private RepairMapper repairMapper;

    @Autowired
    private MeetingRoomMapper meetingRoomMapper;

    @Override
    @Transactional
    public Result reportRepair(Repair repair) {
        try {
            // 验证会议室是否存在
            MeetingRoom room = meetingRoomMapper.findByRoomId(repair.getRoomId());
            if (room == null) {
                return Result.error("会议室不存在");
            }

            Map<String, Object> map = ThreadLocalUtil.get();
            Integer reporterId = (Integer) map.get("id");

            // 设置报修时间和状态
            repair.setReporterId(reporterId);
            repair.setReportTime(LocalDateTime.now());
            repair.setStatus(Status.UNREPAIR);

            // 保存报修记录
            repairMapper.insert(repair);

            // 更新会议室状态为维护中
            room.setStatusName(Status.UNDER_REPAIR);
            meetingRoomMapper.updateRoom(room);

            return Result.success(repair);
        } catch (Exception e) {
            return Result.error("报修失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result processRepair(Integer repairId, String repairReport) {
        try {
            // 获取报修记录
            Repair repair = repairMapper.selectById(repairId);
            if (repair == null) {
                return Result.error("报修记录不存在");
            }

            Map<String, Object> map = ThreadLocalUtil.get();
            Integer repairerId = (Integer) map.get("id");

            // 更新维修信息
            repair.setRepairerId(repairerId);
            repair.setRepairReport(repairReport);
            repair.setStatus(Status.REPAIRED);
            repairMapper.update(repair);

            // 更新会议室状态为空闲
            MeetingRoom room = meetingRoomMapper.findByRoomId(repair.getRoomId());
            if (room != null) {
                room.setStatusName(Status.AVAILABLE);
                meetingRoomMapper.updateRoom(room);
            }

            return Result.success(repair);
        } catch (Exception e) {
            return Result.error("维修处理失败: " + e.getMessage());
        }
    }

    @Override
    public Result getRepairsByStatus(String status) {
        try {
            List<Repair> repairs = repairMapper.selectByStatus(status);
            return Result.success(repairs);
        } catch (Exception e) {
            return Result.error("获取报修记录失败: " + e.getMessage());
        }
    }

    @Override
    public Result getRepairsByRoom(Integer roomId) {
        try {
            List<Repair> repairs = repairMapper.selectByRoomId(roomId);
            return Result.success(repairs);
        } catch (Exception e) {
            return Result.error("获取报修记录失败: " + e.getMessage());
        }
    }

    @Override
    public Result<List<Repair>> getRepairs() {
        try {
            List<Repair> repairs = repairMapper.selectAll();
            return Result.success(repairs);
        } catch (Exception e) {
            return Result.error("获取报修记录失败: " + e.getMessage());
        }
    }
}
