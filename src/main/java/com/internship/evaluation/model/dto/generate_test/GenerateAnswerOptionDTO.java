package com.internship.evaluation.model.dto.generate_test;

import com.internship.evaluation.model.entity.AnswersOption;
import lombok.Data;

@Data
public class GenerateAnswerOptionDTO {
    private Long id;
    private String answerOptionValue;

    GenerateAnswerOptionDTO(AnswersOption answersOption) {
        this.id = answersOption.getId();
        this.answerOptionValue = answersOption.getAnswerOptionValue();
    }
}
