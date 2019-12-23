package com.internship.evaluation.model.dto.generate_test;

import com.internship.evaluation.model.entity.CandidateSqlTask;
import lombok.Data;

@Data
public class SqlAnswersDTO {

    private Long sqlTaskId;
    private String sqlAnswer;

    public SqlAnswersDTO(){

    }
    public SqlAnswersDTO(CandidateSqlTask entity){
        this.setSqlTaskId(entity.getSqlTask().getId());
        this.setSqlAnswer(entity.getStatementProvided());
    }
}
