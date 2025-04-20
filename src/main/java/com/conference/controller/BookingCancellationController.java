package com.conference.controller;


import com.conference.service.BookingCancellationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("cancellation")
public class BookingCancellationController {

    @Autowired
    private BookingCancellationService bookingCancellationService;

}
