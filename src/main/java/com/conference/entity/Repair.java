package com.conference.entity;

import com.conference.utils.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class Repair {

    @NotNull
    private Integer repairId;   // 报修单id

    private Integer roomId; // 维修的房间id
    private String fault;   // 故障描述
    private Integer reporterId; // 报修员工的id
    private Integer repairerId; // 维修员工的id

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime reportTime;   // 报修的时间

    private String repairReport;    // 维修报告
    private String status = Status.UNREPAIR; // 维修状态，默认未处理

}
