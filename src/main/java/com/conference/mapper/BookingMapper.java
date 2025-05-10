package com.conference.mapper;

import com.conference.entity.Booking;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingMapper {

    Booking selectById(Integer bookingId);

    void insert(Booking booking);

    void updateBooking(Booking booking);

    List<Booking> selectByCustomerId(Integer customerId);

    List<Booking> selectExpiredUnpaidBookings(LocalDateTime localDateTime);

    @Update("UPDATE booking SET status_name = '已取消' WHERE booking_id = #{bookingId}")
    void updateStatusToCancel(Integer bookingId);

    @Update("UPDATE booking SET status_name = '已退款' WHERE booking_id = #{bookingId}")
    void updateStatusToRefund(Integer bookingId);

    @Select("SELECT * FROM booking WHERE status_name = #{status} AND start_time <= #{now} AND status_name != '使用中'")
    List<Booking> selectBookingsToStart(@Param("status") String status, @Param("now") LocalDateTime now);

    @Update("UPDATE booking SET " +
            "status_name = #{statusName}, " +
            "room_status = #{roomStatus}, " +
            "WHERE booking_id = #{bookingId}")
    void updateStatus(Booking booking);

    @Select("SELECT * FROM booking WHERE status_name = '使用中' AND end_time <= #{now}")
    List<Booking> selectBookingsToComplete(LocalDateTime now);
}
