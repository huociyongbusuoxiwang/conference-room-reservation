package com.conference.controller;


import com.conference.service.RepairService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("repair")
public class RepairController {

    private RepairService repairService;

}
