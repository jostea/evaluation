package com.internship.evaluation.model.dto.test_token;

import com.internship.evaluation.model.entity.TestToken;
import lombok.*;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TestTokenDTO {

    private Long id;
    private Timestamp dateCrated;
    private boolean isActive;
    private String token;
    private Long candidateId;

    public TestTokenDTO(TestToken entity){
        this.setId(entity.getId());
        this.setDateCrated(entity.getDateCreated());
        this.setActive(entity.isActive());
        this.setToken(entity.getToken());
        this.setCandidateId(entity.getCandidate().getId());
    }

}
