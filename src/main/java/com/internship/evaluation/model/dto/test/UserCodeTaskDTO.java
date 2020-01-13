package com.internship.evaluation.model.dto.test;

import com.internship.evaluation.model.entity.CandidateCodeTask;
import com.internship.evaluation.model.entity.CorrectCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCodeTaskDTO {
    private String questionId;
    private String candidateId;
    private String signature;
    private String userAnswer;
    private List<TestCaseDTO> testCases;

    public UserCodeTaskDTO(CandidateCodeTask candidateCodeTask) {
        this.questionId = candidateCodeTask.getCodeTask().getId().toString();
        this.candidateId = candidateCodeTask.getCandidate().getId().toString();
        this.signature = candidateCodeTask.getCodeTask().getSignature();
        this.userAnswer = candidateCodeTask.getCodeProvided();
        List<TestCaseDTO> constructorCases = new ArrayList<>();
        for (CorrectCode correctCode:candidateCodeTask.getCodeTask().getCorrectCodes()) {
            TestCaseDTO inLoopTestCase = new TestCaseDTO(correctCode);
            constructorCases.add(inLoopTestCase);
        }
        this.testCases = constructorCases;
    }
}
