package com.conference.service;


import com.conference.entity.Repair;
import com.conference.utils.Result;

public interface RepairService {
    Result reportRepair(Repair repair);

    Result processRepair(Integer repairId, String repairReport);

    Result getRepairsByStatus(String status);

    Result getRepairsByRoom(Integer roomId);
}
