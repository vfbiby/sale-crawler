package com.muhuang.salecrawler.schedule;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/1.0/schedules")
@RestController
public class ScheduleItemSalesScrapingController {

    @PostMapping
    void createSchedule(){

    }

}
