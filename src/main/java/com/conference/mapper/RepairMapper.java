package com.conference.mapper;

import com.conference.entity.Repair;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RepairMapper {
    @Insert("INSERT INTO repair (room_id, fault, reporter_id, report_time, status) " +
            "VALUES (#{roomId}, #{fault}, #{reporterId}, #{reportTime}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "repairId")
    int insert(Repair repair);

    @Update("UPDATE repair SET " +
            "repairer_id = #{repairerId}, " +
            "repair_report = #{repairReport}, " +
            "status = #{status} " +
            "WHERE repair_id = #{repairId}")
    int update(Repair repair);

    @Select("SELECT * FROM repair WHERE repair_id = #{repairId}")
    Repair selectById(Integer repairId);

    @Select("SELECT * FROM repair WHERE room_id = #{roomId} ORDER BY report_time DESC")
    List<Repair> selectByRoomId(Integer roomId);

    @Select("SELECT * FROM repair WHERE status = #{status} ORDER BY report_time DESC")
    List<Repair> selectByStatus(String status);
}