package com.conference;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.conference.mapper")    // 指定mapper接口所在位置
public class ConferenceRoomReservationApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConferenceRoomReservationApplication.class, args);
    }
}