package com.muhuang.salecrawler.schedule;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Schedule findByOutItemId(String outItemId);

    @Transactional
    void deleteByOutItemId(String outItemId);
}
