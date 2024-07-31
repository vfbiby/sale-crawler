package com.muhuang.salecrawler.schedule;

import com.muhuang.salecrawler.shared.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Schedule extends BaseEntity {

    @NotNull
    private String outItemId;

    @Enumerated(value = EnumType.STRING)
    private ScheduleStatus status;

    public boolean isRunning() {
        return ScheduleStatus.RUNNING.equals(this.status);
    }

    public void checkStatus() {
        if (isRunning()) {
            throw new ItemIsCrawlingException(String.format("itemId=%s的商品，正在爬取并存储销量信息！", outItemId));
        }
    }

}


