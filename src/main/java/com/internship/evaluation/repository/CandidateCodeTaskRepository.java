package com.internship.evaluation.repository;

import com.internship.evaluation.model.entity.CandidateCodeTask;
import com.internship.evaluation.model.entity.CodeTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateCodeTaskRepository extends JpaRepository<CandidateCodeTask, Long> {
    CandidateCodeTask findByCodeTask(CodeTask codeTask);
}
