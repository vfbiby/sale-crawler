package com.muhuang.salecrawler.rate;

import com.muhuang.salecrawler.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoteType extends BaseEntity {

    private String contentTag;
    private double percent;

}
