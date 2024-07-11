package com.muhuang.salecrawler.rate;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

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

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "notes_rate_id")
    private List<NoteType> noteTypeList;

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

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "page_percent_vo_id")
    private PagePercentVo pagePercentVo;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "long_term_common_note_vo_id")
    private LongTermCommonNoteVo longTermCommonNoteVo;

    private int mEngagementNum;
    private int mFollowCnt;

}
