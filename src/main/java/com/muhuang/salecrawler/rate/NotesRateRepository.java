package com.muhuang.salecrawler.rate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotesRateRepository extends JpaRepository<NotesRate, Long> {
    Optional<NotesRate> findByUniqueNotesRateId(String uniqueNotesRateId);
}
