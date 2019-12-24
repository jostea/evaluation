package com.internship.evaluation.repository;

import com.internship.evaluation.model.entity.TestStructure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestStructureRepository extends JpaRepository<TestStructure, Long> {
}
