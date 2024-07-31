package com.muhuang.salecrawler.schedule;

import jakarta.annotation.Resource;
import lombok.Getter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ScheduleJobService {

    @Resource
    ScheduleService scheduleService;

    @Getter
    private AtomicInteger count = new AtomicInteger(0);

    @Scheduled(cron = "0/1 * * * * ?")
    public void crawling() {
        count.addAndGet(1);

        List<String> pendingItemIds = scheduleService.getPendingItemIds();
        for (String pendingItemId : pendingItemIds) {
            scheduleService.saveSellCount(pendingItemId);
        }
    }


}
