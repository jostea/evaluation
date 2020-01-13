package com.internship.evaluation.model.dto.test;

import com.internship.evaluation.model.entity.CandidateCodeTask;
import lombok.Data;

@Data
public class CandidateCodeResultDTO {
    private Long id;
    private Long candidateId;
    private Long codeTaskId;
    private String codeProvided;
    private Double rateCorrectness;
    private String message;
    private Boolean isCorrect;

    public CandidateCodeResultDTO(CandidateCodeTask candidateCodeTask) {
        this.id = candidateCodeTask.getId();
        this.candidateId = candidateCodeTask.getCandidate().getId();
        this.codeTaskId = candidateCodeTask.getCodeTask().getId();
        this.codeProvided = candidateCodeTask.getCodeProvided();
        this.rateCorrectness = candidateCodeTask.getRateCorrectness();
        this.message = candidateCodeTask.getMessage();
        this.isCorrect = candidateCodeTask.getIsCorrect();
    }
}
