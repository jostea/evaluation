package com.internship.evaluation.repository;

import com.internship.evaluation.model.entity.Stream;
import com.internship.evaluation.model.entity.Task;
import com.internship.evaluation.model.enums.ComplexityEnum;
import com.internship.evaluation.model.enums.TypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByTaskTypeAndComplexityAndIsEnabledAndStreams(TypeEnum taskTypeEnum, ComplexityEnum complexityEnum, boolean is_enabled, Stream streams);
}
