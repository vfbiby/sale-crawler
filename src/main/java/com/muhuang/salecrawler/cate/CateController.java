package com.muhuang.salecrawler.cate;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/1.0/cates")
public class CateController {

    private final CateRepository cateRepository;

    public CateController(CateRepository cateRepository) {
        this.cateRepository = cateRepository;
    }

    @PostMapping
    void createCateList(@RequestBody List<CateChildrenDTO> cateChildrenDTOList) {
        CateChildrenDTO cateChildrenDTO = cateChildrenDTOList.get(0);
        Cate parent = Cate.builder().outCateId(cateChildrenDTO.getId()).cateName(cateChildrenDTO.getName()).build();
        cateRepository.save(parent);
        List<Cate> cateList = cateChildrenDTO.getChildren().stream().map(cateDTO -> {
            Cate child = Cate.builder().outCateId(cateDTO.getId()).cateName(cateDTO.getName()).build();
            child.setParent(parent);
            return child;
        }).collect(Collectors.toList());
        cateRepository.saveAll(cateList);
    }

}
