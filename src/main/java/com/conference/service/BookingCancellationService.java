package com.conference.service;

import com.conference.entity.BookingCancellation;

import java.util.List;

public interface BookingCancellationService {
    List<BookingCancellation> getPendingCancellations();
}
