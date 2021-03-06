package com.internship.evaluation.repository;

import com.internship.evaluation.model.entity.CandidateSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidateSkillRepository extends JpaRepository<CandidateSkill, Long> {
    Optional<CandidateSkill> findCandidateSkillBySkillIdAndCandidateId(Long skillId, Long candidateId);

    List<CandidateSkill> findAllByCandidateId(Long id);
}
