package com.internship.evaluation.repository;

import com.internship.evaluation.model.entity.CandidateSingleTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateSingleTaskRepository extends JpaRepository<CandidateSingleTask, Long> {
}
