package com.internship.evaluation.model.dto.generate_test;

import com.internship.evaluation.model.entity.*;
import com.internship.evaluation.model.enums.TaskTypeEnum;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GenerateTaskDTO {
    private Long id;
    private Long taskId;
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

    public GenerateTaskDTO(CandidateMultiTask candidateMultiTask) {
        this.id = candidateMultiTask.getId();
        this.taskId = candidateMultiTask.getId();
        this.title = candidateMultiTask.getTask().getTitle();
        this.taskType = TaskTypeEnum.fromString(candidateMultiTask.getTask().getTaskType().name()).getType();
        this.description = candidateMultiTask.getTask().getDescription();
        List<GenerateAnswerOptionDTO> generateAnswerOptionDTOS = new ArrayList<>();
        for (AnswersOption answersOption:candidateMultiTask.getAnswersOptions()) {
            this.answersOptions.add(new GenerateAnswerOptionDTO(answersOption));
        }
        this.answersOptions = generateAnswerOptionDTOS;
    }

    public GenerateTaskDTO(CandidateSingleTask candidateSingleTask) {
        this.id = candidateSingleTask.getId();
        this.taskId = candidateSingleTask.getTask().getId();
        this.title = candidateSingleTask.getTask().getTitle();
        this.description = candidateSingleTask.getTask().getDescription();
        this.taskType = TaskTypeEnum.fromString(candidateSingleTask.getTask().getTaskType().name()).getType();
        List<GenerateAnswerOptionDTO> generateAnswerOptionDTOS = new ArrayList<>();
        if (candidateSingleTask.getAnswersOption() != null) {
            AnswersOption answersOption = candidateSingleTask.getAnswersOption();
            GenerateAnswerOptionDTO generateAnswerOptionDTO = new GenerateAnswerOptionDTO(answersOption);
            generateAnswerOptionDTOS.add(generateAnswerOptionDTO);
            this.answersOptions = generateAnswerOptionDTOS;
        } else {
            this.answersOptions = new ArrayList<>();
        }

    }

    public GenerateTaskDTO(CandidateCustomTask candidateCustomTask) {
        this.id = candidateCustomTask.getId();
        this.taskId = candidateCustomTask.getTask().getId();
        this.title = candidateCustomTask.getTask().getTitle();
        this.description = candidateCustomTask.getTask().getDescription();
        this.taskType = TaskTypeEnum.fromString(candidateCustomTask.getTask().getTaskType().name()).getType();
        List<GenerateAnswerOptionDTO> generateAnswerOptionDTOS = new ArrayList<>();
        if (candidateCustomTask.getCustomAnswer() != null) {
            String customAnswer = candidateCustomTask.getCustomAnswer();
            GenerateAnswerOptionDTO generateAnswerOptionDTO = new GenerateAnswerOptionDTO(customAnswer);
            generateAnswerOptionDTOS.add(generateAnswerOptionDTO);
            this.answersOptions = generateAnswerOptionDTOS;
        } else {
            this.answersOptions = new ArrayList<>();
        }
    }
}
