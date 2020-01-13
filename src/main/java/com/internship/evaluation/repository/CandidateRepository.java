package com.internship.evaluation.repository;

import com.internship.evaluation.model.entity.Candidate;
import com.internship.evaluation.model.enums.TestStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    List<Candidate> findCandidatesByTestStatus(TestStatusEnum statusEnum);
}
