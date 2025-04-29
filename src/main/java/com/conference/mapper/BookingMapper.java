package com.conference.mapper;

import com.conference.entity.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingMapper {
    Booking selectById(Integer bookingId);

    void insert(Booking booking);

    void updateBooking(Booking booking);

    List<Booking> selectByCustomerId(Integer customerId);

    List<Booking> selectExpiredUnpaidBookings(LocalDateTime localDateTime);
}
