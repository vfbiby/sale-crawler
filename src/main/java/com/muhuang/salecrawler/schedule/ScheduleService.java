package com.muhuang.salecrawler.schedule;

import jakarta.annotation.Resource;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    @Resource
    ScheduleRepository scheduleRepository;

    public List<String> getToBeCrawledItemIds() {
        Example<Schedule> example = Example.of(Schedule.builder().status(ScheduleStatus.READY).build());
        return scheduleRepository.findAll(example)
                .stream().map(Schedule::getOutItemId).toList();
    }

    public void addToBeCrawledItems(List<String> itemIds) {
        if (CollectionUtils.isEmpty(itemIds)) {
            return;
        }
        Set<String> pendingItemIds =
                scheduleRepository.findAll(Example.of(Schedule.builder().status(ScheduleStatus.PENDING).build()))
                        .stream().map(Schedule::getOutItemId)
                        .collect(Collectors.toSet());

        List<String> toBeCrawledItemIds = itemIds.stream().filter(i -> !pendingItemIds.contains(i)).toList();
        List<Schedule> toSaveSchedules = toBeCrawledItemIds.stream()
                .map(e -> Schedule.builder().outItemId(e).status(ScheduleStatus.READY).build())
                .toList();
        scheduleRepository.saveAll(toSaveSchedules);

    }
}
