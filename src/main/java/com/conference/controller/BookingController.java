package com.conference.controller;

import com.conference.entity.Booking;
import com.conference.entity.MeetingRoom;
import com.conference.service.BookingService;
import com.conference.service.MeetingRoomService;
import com.conference.utils.Result;
import com.conference.utils.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private MeetingRoomService meetingRoomService;

    /**
     * 查询客户所有预订记录
     * url地址：booking/list
     * 请求方式：GET
     * 请求参数：
     * {
     *     "customerId":客户ID (从登录态获取)
     * }
     * 响应数据：
     * {
     *     "code": 0,
     *     "message":"success",
     *     "data":[
     *         {
     *             "bookingId": 1,
     *             "roomName": "会议室A",
     *             "bookingDate": "2025-05-01",
     *             "startTime": "09:00",
     *             "endTime": "12:00",
     *             "totalPrice": 600.00,
     *             "statusName": "已支付"
     *         },
     *         ...
     *     ]
     * }
     */
    @GetMapping("list")
    public Result<List<Booking>> listBookings(@RequestParam Integer customerId) {
        Result result = bookingService.listByCustomerId(customerId);
        return result;
    }

    /**
     * 根据条件筛选可用会议室
     * url地址：booking/availableRooms
     * 请求方式：GET
     * 请求参数：
     * {
     *     "bookingDate": "2025-05-01",  // 预订日期
     *     "startHour": 9,               // 开始小时(8-21)
     *     "endHour": 12,                // 结束小时(8-21)
     *     "capacity": 10,               // 最少容纳人数
     *     "multimediaSupport": true     // 是否需要多媒体设备
     * }
     * 响应数据：
     * {
     *     "code": 0,
     *     "message":"success",
     *     "data":[
     *         {
     *             "roomId": 1,
     *             "roomName": "会议室A",
     *             "roomType": "标准会议室",
     *             "capacity": 20,
     *             "multimediaSupport": true,
     *             "hourlyRate": 200.00,
     *             "statusName": "空闲中"
     *         },
     *         ...
     *     ]
     * }
     */
    @GetMapping("availableRooms")
    public Result<List<MeetingRoom>> findAvailableRooms(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate bookingDate,
            @RequestParam Integer startHour,
            @RequestParam Integer endHour,
            @RequestParam(required = false) Integer capacity,
            @RequestParam(required = false) Boolean multimediaSupport) {

        // 验证时间范围是否合法(8:00-21:00)
        if (startHour < 8 || endHour > 21 || startHour >= endHour) {
            return Result.error("会议室可使用时间为每日8:00-21:00，且结束时间必须晚于开始时间");
        }

        Result result = meetingRoomService.findAvailableRooms(
                bookingDate, startHour, endHour, capacity, multimediaSupport);
        return result;
    }

    /**
     * 创建预订订单
     * url地址：booking
     * 请求方式：POST
     * 请求参数：
     * {
     *     "roomId": 1,               // 会议室ID
     *     "customerId": 123,         // 客户ID (从登录态获取)
     *     "bookingDate": "2025-05-01", // 预订日期
     *     "startHour": 9,            // 开始小时(8-21)
     *     "endHour": 12              // 结束小时(8-21)
     * }
     * 响应数据：
     * {
     *     "code": 0,
     *     "message":"success",
     *     "data":{
     *         "bookingId": 1,        // 订单ID
     *         "totalPrice": 600.00,  // 总金额
     *         "expireTime": "2025-04-26 10:30:00" // 支付过期时间(创建时间+30分钟)
     *     }
     * }
     */
    @PostMapping
    public Result createBooking(
            @RequestParam Integer roomId,
            @RequestParam Integer customerId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate bookingDate,
            @RequestParam Integer startHour,
            @RequestParam Integer endHour) {

        // 验证预订日期是否在60天内
        if (bookingDate.isAfter(LocalDate.now().plusDays(60))) {
            return Result.error("最多只能提前60天预订会议室");
        }

        // 验证时间范围是否合法(8:00-21:00)
        if (startHour < 8 || endHour > 21 || startHour >= endHour) {
            return Result.error("会议室可使用时间为每日8:00-21:00，且结束时间必须晚于开始时间");
        }

        // 构建Booking实体
        Booking booking = new Booking();
        booking.setRoomId(roomId);
        booking.setCustomerId(customerId);
        booking.setBookingDate(bookingDate.atStartOfDay());
        booking.setStartTime(LocalDateTime.of(bookingDate, LocalTime.of(startHour, 0)));
        booking.setEndTime(LocalDateTime.of(bookingDate, LocalTime.of(endHour, 0)));
        booking.setTotalHours(endHour - startHour);

        // 检查会议室是否可用
        if (!meetingRoomService.isRoomAvailable(
                roomId,
                bookingDate,
                startHour,
                endHour)) {
            return Result.error("该时间段会议室已被预订");
        }

        Result result = bookingService.createBooking(booking);
        return result;
    }

    /**
     * 支付订单
     * url地址：booking/pay
     * 请求方式：POST
     * 请求参数：
     * {
     *     "bookingId": 1,           // 订单ID
     *     "paymentMethod": "alipay" // 支付方式
     * }
     * 响应数据：
     * {
     *     "code": 0,
     *     "message":"支付成功",
     *     "data":{
     *         "bookingId": 1,
     *         "statusName": "已支付"
     *     }
     * }
     */
    @PostMapping("pay")
    public Result payBooking(@RequestParam Integer bookingId, @RequestParam String paymentMethod) {
        // 检查订单是否已过期
        if (bookingService.isBookingExpired(bookingId)) {
            return Result.error("订单已过期，请重新预订");
        }

        Result result = bookingService.payBooking(bookingId, paymentMethod);
        return result;
    }


    /**
     * 获取订单详情
     * url地址：booking/detail
     * 请求方式：GET
     * 请求参数：
     * {
     *     "bookingId": 1           // 订单ID
     * }
     * 响应数据：
     * {
     *     "code": 0,
     *     "message":"success",
     *     "data":{
     *         "bookingId": 1,
     *         "roomName": "会议室A",
     *         "bookingDate": "2025-05-01",
     *         "startTime": "09:00",
     *         "endTime": "12:00",
     *         "totalHours": 3,
     *         "totalPrice": 600.00,
     *         "statusName": "已支付",
     *         "createTime": "2025-04-26 10:00:00"
     *     }
     * }
     */
    @GetMapping("detail")
    public Result getBookingDetail(@RequestParam Integer bookingId) {
        Result result = bookingService.getBookingDetail(bookingId);
        return result;
    }
}