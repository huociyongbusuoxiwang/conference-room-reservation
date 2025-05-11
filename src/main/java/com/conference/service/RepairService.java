package com.conference.service;


import com.conference.entity.Repair;
import com.conference.utils.Result;

import java.util.List;

public interface RepairService {
    Result reportRepair(Repair repair);

    Result processRepair(Integer repairId, String repairReport);

    Result getRepairsByStatus(String status);

    Result getRepairsByRoom(Integer roomId);

    Result<List<Repair>> getRepairs();
}
