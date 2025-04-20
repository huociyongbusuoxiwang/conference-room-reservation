package com.conference.service.impl;

import com.conference.mapper.BookingCancellationMapper;
import com.conference.service.BookingCancellationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingCancellationServiceImpl implements BookingCancellationService {

    @Autowired
    private BookingCancellationMapper bookingCancellationMapper;

}
