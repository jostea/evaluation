package com.internship.evaluation.model.dto.generate_test;

import lombok.Data;

import java.util.List;

@Data
public class MultiChoiceCandidateAnswerDTO {

    private List<MultiChoiceTaskAnswerDTO> multiChoiceAnswers;

}
