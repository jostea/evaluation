package com.internship.evaluation.repository;

import com.internship.evaluation.model.entity.Stream;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StreamRepository extends JpaRepository<Stream, Long> {
}
