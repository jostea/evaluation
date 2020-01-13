package com.internship.evaluation.repository;

import com.internship.evaluation.model.entity.CodeTask;
import com.internship.evaluation.model.entity.CorrectCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CorrectCodeRepository extends JpaRepository<CorrectCode, Long> {
}