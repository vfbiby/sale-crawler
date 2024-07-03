package com.muhuang.salecrawler.cate;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CateRepository extends JpaRepository<Cate, Long> {
    Cate findByOutCateId(int cateId);
}
