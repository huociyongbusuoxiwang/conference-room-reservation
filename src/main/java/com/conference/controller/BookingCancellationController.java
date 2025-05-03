package com.conference.controller;


import com.conference.entity.BookingCancellation;
import com.conference.service.BookingCancellationService;
import com.conference.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cancellation")
public class BookingCancellationController {

    @Autowired
    private BookingCancellationService bookingCancellationService;

    // 获取待处理的取消申请
    @GetMapping("/pending")
    public Result<List<BookingCancellation>> getPendingCancellations() {
        return Result.success(bookingCancellationService.getPendingCancellations());
    }

    // 处理取消申请
    @PostMapping("/process")
    public Result processCancellation(@RequestBody BookingCancellation cancellation) {
        // 这里可以添加处理取消申请的逻辑
        // 例如，更新数据库中的状态等
        return Result.success("取消申请已处理");
    }

}
