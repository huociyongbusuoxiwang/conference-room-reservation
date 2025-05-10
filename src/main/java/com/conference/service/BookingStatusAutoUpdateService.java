//package com.conference.service;
//
//import com.conference.entity.Booking;
//import com.conference.mapper.BookingMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//public class BookingStatusAutoUpdateService {
//
//    @Autowired
//    private BookingMapper bookingMapper;
//
//    // 每分钟执行一次状态检查
//    @Scheduled(cron = "0 * * * * ?")
//    public void autoUpdateBookingStatus() {
//        LocalDateTime now = LocalDateTime.now();
//
//        // 1. 更新应开始但未标记的预订为"使用中"
//        updateToInProgress(now);
//
//        // 2. 更新应结束但未标记的预订为"已完成"
//        updateToCompleted(now);
//    }
//
//    private void updateToInProgress(LocalDateTime now) {
//        // 获取所有已支付且开始时间已到但状态未更新的预订
//        List<Booking> bookingsToStart = bookingMapper.selectBookingsToStart(
//                "已支付", now.minusMinutes(15)); // 允许15分钟缓冲
//
//        for (Booking booking : bookingsToStart) {
//            booking.setRoomStatus("使用中");
//            bookingMapper.updateStatus(booking);
//
//            // 可以添加日志记录或通知
//            System.out.println("自动更新预订 " + booking.getBookingId() + " 状态为使用中");
//        }
//    }
//
//    private void updateToCompleted(LocalDateTime now) {
//        // 获取所有使用中且结束时间已过的预订
//        List<Booking> bookingsToComplete = bookingMapper.selectBookingsToComplete(now);
//
//        for (Booking booking : bookingsToComplete) {
//            booking.setStatusName("已完成");
//            booking.setRoomStatus("已空闲");
//            bookingMapper.updateStatus(booking);
//
//            // 可以添加日志记录或通知
//            System.out.println("自动更新预订 " + booking.getBookingId() + " 状态为已完成");
//        }
//    }
//}
