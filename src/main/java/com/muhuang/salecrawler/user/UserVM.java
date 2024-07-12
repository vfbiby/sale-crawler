package com.muhuang.salecrawler.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserVM {
    private Long id;

    private String displayName;

    private String username;

    private String image;

}
