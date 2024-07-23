package com.muhuang.salecrawler.schedule;

import com.muhuang.salecrawler.item.ItemService;
import com.muhuang.salecrawler.sale.Sale;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private ScheduleRepository scheduleRepository;

    private ItemService itemService;

    public ScheduleService(ScheduleRepository scheduleRepository, ItemService itemService) {
        this.scheduleRepository = scheduleRepository;
        this.itemService = itemService;
    }

    public List<String> getToBeCrawledItemIds() {
        Example<Schedule> example = Example.of(Schedule.builder().status(ScheduleStatus.READY).build());
        return scheduleRepository.findAll(example)
                .stream().map(Schedule::getOutItemId).toList();
    }

    public void saveToBeCrawledItems(List<String> itemIds) {
        if (CollectionUtils.isEmpty(itemIds)) {
            return;
        }
        Set<String> pendingItemIds =
                scheduleRepository.findAll(Example.of(Schedule.builder().status(ScheduleStatus.RUNNING).build()))
                        .stream().map(Schedule::getOutItemId)
                        .collect(Collectors.toSet());

        List<String> toBeCrawledItemIds = itemIds.stream().filter(i -> !pendingItemIds.contains(i)).toList();
        List<Schedule> toSaveSchedules = toBeCrawledItemIds.stream()
                .map(e -> Schedule.builder().outItemId(e).status(ScheduleStatus.READY).build())
                .toList();
        scheduleRepository.saveAll(toSaveSchedules);

    }

    public Boolean saveSellCount(String toCrawledItemId) {
        Schedule schedule = scheduleRepository.findByOutItemId(toCrawledItemId);
        if (schedule.isRunning()) {
            throw new ItemIsCrawlingException(String.format("itemId=%s的商品，正在爬取并存储销量信息！", toCrawledItemId));
        }
        schedule.setStatus(ScheduleStatus.RUNNING);
        scheduleRepository.save(schedule);

        Integer totalSellCount = null;
        Sale sale = null;
        try {
            totalSellCount = itemService.getTotalSellCountByOneBound(toCrawledItemId);
        } catch (Exception e) {
            schedule.setStatus(ScheduleStatus.READY);
            scheduleRepository.save(schedule);
            throw new ItemCrawlFailedException(String.format("itemId=%s的商品，调用销量数据接口失败！", toCrawledItemId));
        }

        try {
            sale = itemService.saveSellCount(totalSellCount, toCrawledItemId);
        } catch (Exception e) {
            schedule.setStatus(ScheduleStatus.READY);
            scheduleRepository.save(schedule);
        }

        boolean result = totalSellCount != null && sale != null
                && totalSellCount.equals(sale.getNumber()) && toCrawledItemId.equals(sale.getItem().getOutItemId());

        if (result) {
            scheduleRepository.deleteByOutItemId(toCrawledItemId);
        } else {
            schedule.setStatus(ScheduleStatus.READY);
            scheduleRepository.save(schedule);
        }

        return result;
    }

}
