package com.internship.evaluation.model.dto.generate_test;

import lombok.Data;
import java.util.Map;

@Data
public class MultiChoiceTaskAnswerDTO {

    private Long multiChoiceTaskId;
    private Map<Long, String> aoSelectedAnswers;
    private Map<Long, String> aoCorrectAnswers;
    private boolean isCorrect;

}
