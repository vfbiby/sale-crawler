package com.muhuang.salecrawler.schedule;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/1.0/schedules")
@RestController
public class ScheduleItemSalesScrapingController {

    private final ScheduleRepository scheduleRepository;

    public ScheduleItemSalesScrapingController(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @PostMapping
    void createSchedule(@RequestBody Schedule schedule) throws ScheduleItemIdNotNullException {
        if (schedule.getOutItemId() == null) {
            throw new ScheduleItemIdNotNullException();
        }
        schedule.setStatus("pending");
        scheduleRepository.save(schedule);
    }

}
