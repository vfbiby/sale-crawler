package com.muhuang.salecrawler.cate;

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
public class Cate {

    @Id
    @GeneratedValue
    private Long id;

    private Integer outCateId;

}
