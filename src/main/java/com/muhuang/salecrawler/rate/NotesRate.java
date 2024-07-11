package com.muhuang.salecrawler.rate;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class NotesRate {

    @Id
    @GeneratedValue
    private Long id;

    private String kocId;
    private int noteNumber;
    private int videoNoteNumber;
    private double hundredLikePercent;
    private double thousandLikePercent;
    //                    "noteType":[
//
//    {
//        "contentTag":"时尚",
//            "percent":"87.9"
//    },
//
//    {
//        "contentTag":"出行旅游",
//            "percent":"6.1"
//    }
//    ],
//            "tradeNames":null,
    private int impMedian;
    private double impMedianBeyondRate;
    private int readMedian;
    private double readMedianBeyondRate;
    private int interactionMedian;
    private double interactionRate;
    private double interactionBeyondRate;
    private int likeMedian;
    private int collectMedian;
    private int commentMedian;
    private int shareMedian;
    private double videoFullViewRate;
    private double videoFullViewBeyondRate;
    private double picture3sViewRate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "page_percent_vo_id")
    private PagePercentVo pagePercentVo;
    //
//    {
//    },
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "long_term_common_note_vo_id")
    private LongTermCommonNoteVo longTermCommonNoteVo;
    //
//    {
//        "startPublishTime":"2023-11-14",
//            "endPublishTime":"2024-01-13",
//            "noteNumber":42,
//            "recentReadNum":58479,
//            "recentReadBeyondRate":"70.5",
//            "longTermReadNum":12446,
//            "longTermReadBeyondRate":"76.7",
//            "recentSearchPagePercent":"9.0",
//            "recentFindPagePercent":"47.8",
//            "recentKolHomePagePercent":"8.7",
//            "recentFollowPagePercent":"32.2",
//            "recentOtherPagePercent":"2.3",
//            "longTermSearchPagePercent":"58.2",
//            "longTermFindPagePercent":"6.1",
//            "longTermKolHomePagePercent":"28.8",
//            "longTermFollowPagePercent":"0.6",
//            "longTermOtherPagePercent":"6.3"
//    },
//            "longTermCooperateNoteVo":
//
//    {
//        "startPublishTime":"2023-11-14",
//            "endPublishTime":"2024-01-13",
//            "noteNumber":4,
//            "recentReadNum":53462,
//            "recentReadBeyondRate":"78.6",
//            "longTermReadNum":7871,
//            "longTermReadBeyondRate":"74.7",
//            "recentSearchPagePercent":"6.6",
//            "recentFindPagePercent":"42.6",
//            "recentKolHomePagePercent":"12.4",
//            "recentFollowPagePercent":"35.9",
//            "recentOtherPagePercent":"2.5",
//            "longTermSearchPagePercent":"14.4",
//            "longTermFindPagePercent":"5.0",
//            "longTermKolHomePagePercent":"65.1",
//            "longTermFollowPagePercent":"1.0",
//            "longTermOtherPagePercent":"14.5"
//    },
    private int mEngagementNum;
    private int mFollowCnt;

}
