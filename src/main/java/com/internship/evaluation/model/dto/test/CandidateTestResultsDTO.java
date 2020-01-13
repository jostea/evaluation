package com.internship.evaluation.model.dto.test;

import com.internship.evaluation.model.dto.generate_test.MultiChoiceCandidateAnswerDTO;
import com.internship.evaluation.model.dto.generate_test.SingleChoiceCandidateAnswerDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class CandidateTestResultsDTO {

    private Long candidateId;
    private String token;
    private SingleChoiceCandidateAnswerDTO singleChoiceTasksResults;
    private MultiChoiceCandidateAnswerDTO multiChoiceCandidateAnswerDTO;
    private SqlCandidateResultDTO sqlResults;
    private CandidateCheckedCodeTasksDTO candidateCheckedCodeTasksDTO;

    @Override
    public String toString() {
        return "CandidateTestResultsDTO{" +
                "candidateId=" + candidateId +
                ", token='" + token + '\'' +
                ", sqlResults=" + sqlResults +
                '}';
    }
}
