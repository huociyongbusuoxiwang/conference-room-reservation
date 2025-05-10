package com.conference.controller;


import com.conference.entity.Repair;
import com.conference.service.RepairService;
import com.conference.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("repair")
public class RepairController {

    @Autowired
    private RepairService repairService;

    /**
     * 提交报修申请
     */
    @PostMapping("/report")
    public Result reportRepair(@RequestBody Repair repair) {
        return repairService.reportRepair(repair);
    }

    /**
     * 处理维修
     */
    @PostMapping("/process")
    public Result processRepair(@RequestParam Integer repairId,
                                @RequestParam String repairReport) {
        return repairService.processRepair(repairId, repairReport);
    }

    /**
     * 根据状态获取报修列表
     */
    @GetMapping("/list/status")
    public Result getRepairsByStatus(@RequestParam String status) {
        return repairService.getRepairsByStatus(status);
    }

    /**
     * 获取会议室的报修历史
     */
    @GetMapping("/list/room")
    public Result getRepairsByRoom(@RequestParam Integer roomId) {
        return repairService.getRepairsByRoom(roomId);
    }

}
