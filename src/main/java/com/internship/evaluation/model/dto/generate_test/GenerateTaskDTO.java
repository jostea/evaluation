package com.internship.evaluation.model.dto.generate_test;

import com.internship.evaluation.model.entity.AnswersOption;
import com.internship.evaluation.model.entity.Task;
import com.internship.evaluation.model.enums.TypeEnum;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GenerateTaskDTO {
    private Long id;
    private String title;
    private String description;
    private String taskType;
    private List<GenerateAnswerOptionDTO> answersOptions;

    public GenerateTaskDTO(Task task) {
        List<GenerateAnswerOptionDTO> generateAnswerOptionDTOS = new ArrayList<>();
        this.id = task.getId();
        this.description = task.getDescription();
        this.title = task.getTitle();
        this.taskType = task.getTaskType().getType();
        for (AnswersOption answersOption: task.getAnswersOptions()) {
            generateAnswerOptionDTOS.add(new GenerateAnswerOptionDTO(answersOption));
        }
        this.answersOptions = generateAnswerOptionDTOS;
    }
}
