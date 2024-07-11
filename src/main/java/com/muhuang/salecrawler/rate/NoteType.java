package com.muhuang.salecrawler.rate;

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
public class NoteType {

    @Id
    @GeneratedValue
    private Long id;

    private String contentTag;
    private double percent;

}
