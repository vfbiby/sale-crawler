package com.muhuang.salecrawler.cate;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/1.0/cates")
public class CateController {

    @PostMapping
    void createCateList(){

    }

}
