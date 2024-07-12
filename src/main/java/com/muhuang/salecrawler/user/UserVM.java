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

    public UserVM(User user) {
        this.setId(user.getId());
        this.setDisplayName(user.getDisplayName());
        this.setUsername(user.getUsername());
        this.setImage(user.getImage());
    }

}
