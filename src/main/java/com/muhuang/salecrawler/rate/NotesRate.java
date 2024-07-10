package com.muhuang.salecrawler.rate;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class NotesRate {

    @Id
    @GeneratedValue
    private Long id;

    private int noteNumber;

    private int videoNoteNumber;

    private double hundredLikePercent;

//                    "thousandLikePercent":"54.5",
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
//            "impMedian":313411,
//            "impMedianBeyondRate":"90.0",
//            "readMedian":53676,
//            "readMedianBeyondRate":"89.0",
//            "interactionMedian":1952,
//            "interactionRate":"8.6",
//            "interactionBeyondRate":"85.3",
//            "likeMedian":1309,
//            "collectMedian":566,
//            "commentMedian":77,
//            "shareMedian":69,
//            "videoFullViewRate":"17.1",
//            "videoFullViewBeyondRate":"58.5",
//            "picture3sViewRate":"74.5",
//            "pagePercentVo":
//
//    {
//        "impHomefeedPercent":0.716,
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
//    },
//            "longTermCommonNoteVo":
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
//            "mEngagementNum":2171,
//            "mFollowCnt":172

}
