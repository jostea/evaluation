package com.internship.evaluation.model.dto.generate_test;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SingleChoiceTaskAnswerDTO {

    private Long singleChoiceTaskId;
    private Long selectedAnswerOptionId;
    private String selectedAnswerOptionText;
    private Boolean isCorrect;

}
