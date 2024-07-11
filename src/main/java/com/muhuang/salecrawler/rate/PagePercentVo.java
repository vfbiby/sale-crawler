package com.muhuang.salecrawler.rate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagePercentVo {

    @Id
    @GeneratedValue
    private Long id;

    private double impHomefeedPercent;
    private double impSearchPercent;
    private double impFollowPercent;
    private double impDetailPercent;
    private double impNearbyPercent;
    private double impOtherPercent;
    private double readHomefeedPercent;
    private double readSearchPercent;
    private double readFollowPercent;
    private double readDetailPercent;
    private double readNearbyPercent;
    private double readOtherPercent;

}
