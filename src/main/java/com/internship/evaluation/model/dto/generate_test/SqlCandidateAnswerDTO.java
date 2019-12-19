package com.internship.evaluation.model.dto.generate_test;

import lombok.Data;

import java.util.ArrayList;

@Data
public class SqlCandidateAnswerDTO {
    private String token;
    private ArrayList<SqlAnswersDTO> answers;
}
