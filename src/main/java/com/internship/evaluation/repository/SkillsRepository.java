package com.internship.evaluation.repository;

import com.internship.evaluation.model.entity.Skill;
import com.internship.evaluation.model.enums.SkillsTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillsRepository extends JpaRepository<Skill, Long> {
    List<Skill> findSkillsBySkillType(SkillsTypeEnum skillType);
}
