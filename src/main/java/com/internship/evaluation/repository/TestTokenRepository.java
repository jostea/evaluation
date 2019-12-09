package com.internship.evaluation.repository;

import com.internship.evaluation.model.entity.TestToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface TestTokenRepository extends JpaRepository<TestToken, Long> {

    @Query
    Optional<TestToken> findFirstByCandidateId(Long id);

    @Query
    Optional<TestToken> findFirstByToken(String token);
}
