package com.conference.mapper;

import com.conference.entity.Booking;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;
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

    @Update("UPDATE booking SET " +
            "status_name = '已退款', " +
            "room_status = '已取消' " +
            "WHERE booking_id = #{bookingId}")
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

    @Select("SELECT b.*, m.room_name " +
            "FROM booking b " +
            "LEFT JOIN meeting_room m ON b.room_id = m.room_id " +
            "WHERE DATE(b.booking_date) = #{bookingDate} " +
            "AND HOUR(b.start_time) >= #{startHour} " +
            "AND HOUR(b.end_time) <= #{endHour} " +
            "AND b.room_status = '已预订'")
    List<Booking> selectbyConditions(LocalDate bookingDate, Integer startHour, Integer endHour);

    @Update("<script>" +
            "UPDATE booking " +
            "<set>" +
            "  <if test='roomId != null'>room_id = #{roomId},</if>" +
            "  <if test='customerId != null'>customer_id = #{customerId},</if>" +
            "  <if test='bookingDate != null'>booking_date = #{bookingDate},</if>" +
            "  <if test='startTime != null'>start_time = #{startTime},</if>" +
            "  <if test='endTime != null'>end_time = #{endTime},</if>" +
            "  <if test='totalHours != null'>total_hours = #{totalHours},</if>" +
            "  <if test='totalPrice != null'>total_price = #{totalPrice},</if>" +
            "  <if test='statusName != null'>status_name = #{statusName},</if>" +
            "  <if test='roomStatus != null'>room_status = #{roomStatus},</if>" +
            "  <if test='paymentStatus != null'>payment_status = #{paymentStatus}</if>" +
            "</set>" +
            "WHERE booking_id = #{bookingId}" +
            "</script>")
    int updateBookingStatus(Booking booking);

    @Select("SELECT b.*, m.room_name " +
            "FROM booking b " +
            "LEFT JOIN meeting_room m ON b.room_id = m.room_id")
    List<Booking> selectAllBookings();
}
