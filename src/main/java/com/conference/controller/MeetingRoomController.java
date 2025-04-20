package com.conference.controller;


import com.conference.service.MeetingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("meetingroom")
public class MeetingRoomController {

    @Autowired
    private MeetingRoomService meetingRoomService;


}
