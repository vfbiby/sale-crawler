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
//            "impSearchPercent":0.077,
//            "impFollowPercent":0.05,
//            "impDetailPercent":0.134,
//            "impNearbyPercent":0,
//            "impOtherPercent":0.02300000000000002,
//            "readHomefeedPercent":0.664,
//            "readSearchPercent":0.075,
//            "readFollowPercent":0.099,
//            "readDetailPercent":0.136,
//            "readNearbyPercent":0,
//            "readOtherPercent":0.02599999999999994
}
