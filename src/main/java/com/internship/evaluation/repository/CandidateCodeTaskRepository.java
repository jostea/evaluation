package com.internship.evaluation.repository;

import com.internship.evaluation.model.entity.CandidateCodeTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateCodeTaskRepository extends JpaRepository<CandidateCodeTask, Long> {
}
