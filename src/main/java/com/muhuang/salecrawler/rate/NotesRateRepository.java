package com.muhuang.salecrawler.rate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotesRateRepository extends JpaRepository<NotesRate, Long> {
}
