package com.conference.service;

import com.conference.entity.BookingCancellation;

import java.util.List;

public interface BookingCancellationService {
    List<BookingCancellation> getPendingCancellations();

    String applyForCancellation(BookingCancellation cancellation);

    boolean processCancellation(Integer cancellationId, Integer verificationStatus);

    List<BookingCancellation> getCancellationsByCustomer(Integer customerId);
}
