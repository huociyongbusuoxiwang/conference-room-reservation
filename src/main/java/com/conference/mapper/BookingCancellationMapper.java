package com.conference.mapper;

import com.conference.entity.BookingCancellation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface BookingCancellationMapper {


    int updateStatus(BookingCancellation cancellation);

    //注意需要把数据库的employeeid的非空改为可以空
    @Insert("INSERT INTO booking_cancellation(booking_id, customer_id, cancellation_date, refund_amount, refund_reason, employee_id, is_verified, approved_at) " +
            "VALUES(#{bookingId}, #{customerId}, #{cancellationDate}, #{refundAmount}, #{refundReason}, #{employeeId}, #{isVerified}, #{approvedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "cancellationId")
    int insert(BookingCancellation cancellation);

    @Select("SELECT * FROM booking_cancellation WHERE is_verified = 0 ORDER BY cancellation_date ASC")
    List<BookingCancellation> getPendingCancellations();

    @Select("SELECT * FROM booking_cancellation WHERE cancellation_id = #{cancellationId}")
    BookingCancellation selectById(Integer cancellationId);

    @Update("UPDATE booking_cancellation SET " +
            "is_verified = #{isVerified}, " +
            "employee_id = #{employeeId}, " +
            "approved_at = #{approvedAt} " +
            "WHERE cancellation_id = #{cancellationId}")
    int updateVerificationStatus(BookingCancellation cancellation);

    @Select("SELECT * FROM booking_cancellation WHERE customer_id = #{customerId} ORDER BY cancellation_date DESC")
    List<BookingCancellation> selectByCustomerId(Integer customerId);
}
