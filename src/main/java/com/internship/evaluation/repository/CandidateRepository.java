package com.internship.evaluation.repository;

import com.internship.evaluation.model.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {

}
