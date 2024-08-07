package com.muhuang.salecrawler.cate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CateChildrenDTO {

    private Integer id;

    private String name;

    private List<CateDTO> children;

}
