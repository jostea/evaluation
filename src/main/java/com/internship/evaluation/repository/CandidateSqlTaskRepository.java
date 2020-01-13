package com.internship.evaluation.repository;

import com.internship.evaluation.model.entity.CandidateSqlTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateSqlTaskRepository extends JpaRepository<CandidateSqlTask, Long> {
}
