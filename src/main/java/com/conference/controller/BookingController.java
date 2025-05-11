package com.conference.controller;

import com.conference.entity.Booking;
import com.conference.entity.BookingCancellation;
import com.conference.entity.MeetingRoom;
import com.conference.service.BookingService;
import com.conference.service.MeetingRoomService;
import com.conference.utils.Result;
import com.conference.utils.Status;
import com.conference.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private MeetingRoomService meetingRoomService;

    /**
     * 查询客户所有预订记录（用户端）
     * url地址：booking/list
     * 请求方式：GET
     * 请求参数：
     * {
     *     "customerId":客户ID
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
    public Result<List<Booking>> listBookings() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer customerId = (Integer) map.get("id");

        Result result = bookingService.listByCustomerId(customerId);
        return result;
    }

    /**
     * 查询客户所有预订记录（员工端）
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
    @GetMapping("list_forEmployee")
    public Result<List<Booking>> listBookingsforEmployee(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate bookingDate,
                                                         @RequestParam Integer startHour,
                                                         @RequestParam Integer endHour) {
        Result result = bookingService.listByCustomerIdforemployee(bookingDate, startHour, endHour);
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
    @PostMapping("createBooking")
    public Result createBooking(
            @RequestParam Integer roomId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate bookingDate,
            @RequestParam Integer startHour,
            @RequestParam Integer endHour) {

        // 基本时间验证（纯参数校验，不涉及业务逻辑）
        if (bookingDate.isAfter(LocalDate.now().plusDays(60))) {
            return Result.error("最多只能提前60天预订会议室");
        }
        if (bookingDate.isBefore(LocalDate.now())) {
            return Result.error("不能预订过去的日期");
        }
        if (startHour < 8 || endHour > 21 || startHour >= endHour) {
            return Result.error("会议室可使用时间为每日8:00-21:00，且结束时间必须晚于开始时间");
        }
        if (bookingDate.isEqual(LocalDate.now()) && startHour < LocalTime.now().getHour()) {
            return Result.error("当天预订的开始时间不能早于当前时间");
        }

        Map<String, Object> map = ThreadLocalUtil.get();
        Integer customerId = (Integer) map.get("id");

        // 构建Booking实体（只设置基本属性）
        Booking booking = new Booking();
        booking.setRoomId(roomId);
        booking.setCustomerId(customerId);
        booking.setBookingDate(bookingDate);
        booking.setStartTime(LocalTime.of(startHour, 0));
        booking.setEndTime(LocalTime.of(endHour, 0));
        booking.setTotalHours(endHour - startHour);

        // 调用Service层处理核心业务逻辑
        return bookingService.createBooking(booking);
    }

    /**
     * 支付订单
     * url地址：booking/pay
     * 请求方式：POST
     * 请求参数：
     * {
     *     "bookingId": 1,           // 订单ID
     *     "paymentMethod": "alipay" // 支付方式 //这里的支付方式前端可设置为微信支付和支付宝支付
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


    /**
     * 更改会议室状态（员工端使用）
     * 在用户开始使用会议室之后，员工手动更新会议室状态为"使用中"
     * 在用户结束使用会议室之后，员工检查会议室并手动更新会议室状态为"空闲中"或者"维护中“
     */
    @PostMapping("updateRoomStatus")
    public Result updateRoomStatus(@RequestParam Integer roomId, @RequestParam String statusName) {
        // 验证状态是否合法
        if (!Status.UNDER_USING.equals(statusName) && !Status.AVAILABLE.equals(statusName) && !Status.UNDER_REPAIR.equals(statusName)) {
            return Result.error("无效的会议室状态");
        }

        // 更新会议室状态
        Result result = meetingRoomService.updateRoomStatus(roomId, statusName);
        return result;
    }


    /**
     * 更新预订状态（员工端使用）
     */
    @PutMapping("updateBooking")
    public Result updateBooking(@RequestBody Booking booking) {
        if (booking.getBookingId() == null) {
            return Result.error("预订ID不能为空");
        }

        // 更新预订状态
        Result result = bookingService.updateBookingStatus(booking);
        return result;
    }



}