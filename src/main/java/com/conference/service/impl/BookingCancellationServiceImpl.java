package com.conference.service.impl;

import com.conference.entity.BookingCancellation;
import com.conference.mapper.BookingCancellationMapper;
import com.conference.service.BookingCancellationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingCancellationServiceImpl implements BookingCancellationService {

    @Autowired
    private BookingCancellationMapper bookingCancellationMapper;

    @Override
    public List<BookingCancellation> getPendingCancellations() {
        return List.of();
    }
}
