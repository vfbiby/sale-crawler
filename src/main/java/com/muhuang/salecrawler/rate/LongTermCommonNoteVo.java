package com.muhuang.salecrawler.rate;

import com.muhuang.salecrawler.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LongTermCommonNoteVo extends BaseEntity {

    private LocalDate startPublishTime;
    private LocalDate endPublishTime;
    private int noteNumber;
    private int recentReadNum;
    private double recentReadBeyondRate;
    private int longTermReadNum;
    private double longTermReadBeyondRate;
    private double recentSearchPagePercent;
    private double recentFindPagePercent;
    private double recentKolHomePagePercent;
    private double recentFollowPagePercent;
    private double recentOtherPagePercent;
    private double longTermSearchPagePercent;
    private double longTermFindPagePercent;
    private double longTermKolHomePagePercent;
    private double longTermFollowPagePercent;
    private double longTermOtherPagePercent;
}
