package com.muhuang.salecrawler.cate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CateDTO {

    private Long id;

    private String name;

    private String url;

}
