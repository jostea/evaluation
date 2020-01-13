package com.internship.evaluation.repository;

import com.internship.evaluation.model.entity.Internship;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface InternshipRepository extends JpaRepository<Internship, Long> {
    @Query
    Optional<Internship> findFirstByIsCurrentTrue();
}
