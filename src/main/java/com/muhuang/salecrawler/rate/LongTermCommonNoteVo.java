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
public class LongTermCommonNoteVo {

    @Id
    @GeneratedValue
    private Long id;

}
