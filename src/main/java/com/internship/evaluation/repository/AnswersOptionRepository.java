package com.internship.evaluation.repository;

import com.internship.evaluation.model.entity.AnswersOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswersOptionRepository extends JpaRepository<AnswersOption, Long> {
}
