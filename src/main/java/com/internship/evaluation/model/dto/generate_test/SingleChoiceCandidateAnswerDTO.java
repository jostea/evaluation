package com.internship.evaluation.model.dto.generate_test;

import lombok.Data;

import java.util.ArrayList;

@Data
public class SingleChoiceCandidateAnswerDTO {
    private String token;
    private ArrayList<SingleChoiceTaskAnswerDTO> answers;
}
