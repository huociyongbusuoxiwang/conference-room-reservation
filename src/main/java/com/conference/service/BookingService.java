package com.conference.service;

import com.conference.entity.Booking;
import com.conference.utils.Result;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

public interface BookingService {
    Result<List<Booking>> listByCustomerId(Integer customerId);

    Result createBooking(Booking booking);

    boolean isBookingExpired(Integer bookingId);

    Result payBooking(Integer bookingId, String paymentMethod);

    Result<Booking> getBookingDetail(Integer bookingId);

    @Scheduled(fixedRate = 60000) // 每分钟检查一次
    void checkAndReleaseExpiredBookings();
}
