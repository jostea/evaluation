package com.internship.evaluation.repository;

import com.internship.evaluation.model.entity.Stream;
import com.internship.evaluation.model.entity.StreamTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StreamTimeRepository extends JpaRepository<StreamTime, Long> {

    @Query
    Optional<StreamTime> findFirstByStream(Stream stream);
}
