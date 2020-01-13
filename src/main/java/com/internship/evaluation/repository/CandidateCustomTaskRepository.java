package com.internship.evaluation.repository;

import com.internship.evaluation.model.entity.CandidateCustomTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateCustomTaskRepository extends JpaRepository<CandidateCustomTask, Long> {
}
