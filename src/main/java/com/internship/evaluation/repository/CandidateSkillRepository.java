package com.internship.evaluation.repository;

import com.internship.evaluation.model.entity.CandidateSkill;
import com.internship.evaluation.model.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidateSkillRepository extends JpaRepository<CandidateSkill, Long> {

    Optional<CandidateSkill> findCandidateSkillBySkillId(Long id);
}
