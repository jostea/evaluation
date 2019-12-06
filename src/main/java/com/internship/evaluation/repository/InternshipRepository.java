package com.internship.evaluation.repository;

import com.internship.evaluation.model.entity.Internship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface InternshipRepository extends JpaRepository<Internship, Long> {
    @Query
    Optional<Internship> findFirstByIsCurrentTrue();
}
