package com.muhuang.salecrawler.schedule;

import com.muhuang.salecrawler.shared.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}


