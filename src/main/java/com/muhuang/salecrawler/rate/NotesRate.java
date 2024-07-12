package com.muhuang.salecrawler.rate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.muhuang.salecrawler.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotesRate extends BaseEntity {

    @Column(updatable = false)
    private LocalDate captureDate;

    @Enumerated(EnumType.STRING)
    private NotesType type;

    private String uniqueNotesRateId;

    @PrePersist
    private void onLoad() {
        generateCaptureDate();
        this.setUniqueNotesRateId(getNotesRateId());
    }

    private void generateCaptureDate() {
        if (this.getCaptureDate() == null)
            this.setCaptureDate(LocalDate.now());
    }

    private String getNotesRateId() {
        return userId + captureDate.toString() + type;
    }

    private String userId;
    private int noteNumber;
    private int videoNoteNumber;
    private double hundredLikePercent;
    private double thousandLikePercent;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "notes_rate_id")
    private List<NoteType> noteType;

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
    @JsonProperty("mEngagementNum")
    private int mEngagementNum;
    @JsonProperty("mFollowCnt")
    private int mFollowCnt;


    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "page_percent_vo_id")
    private PagePercentVo pagePercentVo;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "long_term_common_note_vo_id")
    private LongTermCommonNoteVo longTermCommonNoteVo;

    public String generateUniqueNotesRateId() {
        generateCaptureDate();
        return getNotesRateId();
    }
}
