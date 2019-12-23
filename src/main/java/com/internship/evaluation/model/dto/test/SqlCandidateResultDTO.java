package com.internship.evaluation.model.dto.test;

import com.internship.evaluation.model.dto.generate_test.SqlAnswersDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SqlCandidateResultDTO {

    private List<SqlAnswersDTO> checkedCandidateSqlAnswers;

    @Override
    public String toString() {
        return "SqlCandidateResultDTO{" +
                "checkedCandidateSqlAnswers=" + checkedCandidateSqlAnswers +
                '}';
    }
}
