package com.internship.evaluation.model.dto.generate_test;

import com.internship.evaluation.model.entity.CandidateSqlTask;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class SqlAnswersDTO {

    private Long sqlTaskId;
    private String sqlAnswer;
    private String correctStatement;
    private String message;
    private Boolean areRowsOrdered;
    private Boolean areColumnsNamed;
    private Boolean isCorrect;

    public SqlAnswersDTO(){

    }
    public SqlAnswersDTO(CandidateSqlTask entity){
        this.setSqlTaskId(entity.getSqlTask().getId());
        this.setSqlAnswer(entity.getStatementProvided());
    }

    @Override
    public String toString() {
        return "SqlAnswersDTO{" +
                "sqlTaskId=" + sqlTaskId +
                ", sqlAnswer='" + sqlAnswer + '\'' +
                ", correctStatement='" + correctStatement + '\'' +
                ", isCorrect=" + isCorrect +
                '}';
    }
}
