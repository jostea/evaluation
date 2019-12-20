package com.internship.evaluation.repository;

import com.internship.evaluation.model.entity.SqlTask;
import com.internship.evaluation.model.entity.Stream;
import com.internship.evaluation.model.enums.ComplexityEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SqlTaskRepository extends JpaRepository<SqlTask, Long> {
    List<SqlTask> findAllByComplexityAndIsEnabledIsTrueAndStreams(ComplexityEnum complexityEnum, Stream stream);
}
