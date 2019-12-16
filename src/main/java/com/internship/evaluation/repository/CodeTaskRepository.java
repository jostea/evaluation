package com.internship.evaluation.repository;

import com.internship.evaluation.model.entity.CodeTask;
import com.internship.evaluation.model.enums.ComplexityEnum;
import com.internship.evaluation.model.enums.TechnologyEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeTaskRepository extends JpaRepository<CodeTask, Long> {
    List<CodeTask> findAllByComplexityAndIsEnabledIsTrueAndTechnology(ComplexityEnum complexity, TechnologyEnum technologyEnum);
}
