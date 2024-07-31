package com.muhuang.salecrawler.schedule;

import com.muhuang.salecrawler.item.ItemService;
import com.muhuang.salecrawler.item.SaleAssociatedItemMustNotBeNullException;
import com.muhuang.salecrawler.sale.Sale;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.function.*;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final ItemService itemService;

    public ScheduleService(ScheduleRepository scheduleRepository, ItemService itemService) {
        this.scheduleRepository = scheduleRepository;
        this.itemService = itemService;
    }

    public List<String> getPendingItemIds() {
        Example<Schedule> example = Example.of(Schedule.builder().status(ScheduleStatus.PENDING).build());
        return scheduleRepository.findAll(example)
                .stream().map(Schedule::getOutItemId).toList();
    }

    public void savePendingItemIds(List<String> itemIds) {
        if (CollectionUtils.isEmpty(itemIds)) {
            return;
        }
        Set<String> runningItemIds = getRunningItemIds();
        List<String> toBeCrawledItemIds = itemIds.stream().filter(i -> !runningItemIds.contains(i)).toList();

        scheduleRepository.saveAll(getToSaveSchedules(toBeCrawledItemIds));

    }

    private static List<Schedule> getToSaveSchedules(List<String> toBeCrawledItemIds) {
        return toBeCrawledItemIds.stream()
                .map(e -> Schedule.builder().outItemId(e).status(ScheduleStatus.PENDING).build())
                .toList();
    }

    private Set<String> getRunningItemIds() {
        return scheduleRepository.findAll(Example.of(Schedule.builder().status(ScheduleStatus.RUNNING).build()))
                .stream().map(Schedule::getOutItemId)
                .collect(Collectors.toSet());
    }

    public Boolean saveSellCount(String toCrawledItemId) {
        Schedule schedule = scheduleRepository.findByOutItemId(toCrawledItemId);
        schedule.checkStatus();
        setRunning(schedule);

        Integer totalSellCount = withException(
                () -> itemService.getTotalSellCountByOneBound(toCrawledItemId), schedule, "crawl", toCrawledItemId);
        Sale sale = withException(
                () -> itemService.saveSellCount(totalSellCount, toCrawledItemId), schedule, "save", toCrawledItemId);

        if (isSuccessful(toCrawledItemId, totalSellCount, sale)) {
            scheduleRepository.deleteByOutItemId(toCrawledItemId);
            return true;
        }
        setPending(schedule);
        return false;
    }

    private static boolean isSuccessful(String toCrawledItemId, Integer totalSellCount, Sale sale) {
        return totalSellCount != null && sale != null
                && totalSellCount.equals(sale.getSellCount()) && toCrawledItemId.equals(sale.getItem().getOutItemId());
    }

    public <T> T withException(Supplier<T> supplier, Schedule schedule, String type, String toCrawledItemId) {
        try {
            return supplier.get();
        } catch (Throwable throwable) {
            setPending(schedule);
            if (type.equals("crawl")) {
                throw new ItemCrawlFailedException(String.format("itemId=%s的商品，调用销量数据接口失败！", toCrawledItemId));
            } else if (type.equals("save")) {
                throw new SaleAssociatedItemMustNotBeNullException("找不到 itemId=" + toCrawledItemId + " 的商品！");
            }
        }
        return null;
    }


    private void setPending(Schedule schedule) {
        setStatus(schedule, ScheduleStatus.PENDING);
    }

    private void setRunning(Schedule schedule) {
        setStatus(schedule, ScheduleStatus.RUNNING);
    }

    private void setStatus(Schedule schedule, ScheduleStatus status) {
        schedule.setStatus(status);
        scheduleRepository.save(schedule);
    }

}
