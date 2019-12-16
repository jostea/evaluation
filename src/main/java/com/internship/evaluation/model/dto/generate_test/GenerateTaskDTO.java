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
    private List<GenerateAnswerOptionDTO> candidateAnswers;
    private List<GenerateAnswerOptionDTO> allAnswerOptions;

    public GenerateTaskDTO(Task task) {
        List<GenerateAnswerOptionDTO> generateAnswerOptionDTOS = new ArrayList<>();
        this.taskId = task.getId();
        this.description = task.getDescription();
        this.title = task.getTitle();
        this.taskType = task.getTaskType().getType();
        for (AnswersOption answersOption: task.getAnswersOptions()) {
            generateAnswerOptionDTOS.add(new GenerateAnswerOptionDTO(answersOption));
        }
        this.allAnswerOptions = generateAnswerOptionDTOS;
    }

    public GenerateTaskDTO(CandidateMultiTask candidateMultiTask) {
        this.id = candidateMultiTask.getId();
        this.taskId = candidateMultiTask.getTask().getId();
        this.title = candidateMultiTask.getTask().getTitle();
        this.taskType = TaskTypeEnum.fromString(candidateMultiTask.getTask().getTaskType().name()).getType();
        this.description = candidateMultiTask.getTask().getDescription();
        List<GenerateAnswerOptionDTO> generateAnswerOptionDTOS = new ArrayList<>();
        List<GenerateAnswerOptionDTO> allAnswerOptions = new ArrayList<>();
        for (AnswersOption answersOption:candidateMultiTask.getAnswersOptions()) {
            this.candidateAnswers.add(new GenerateAnswerOptionDTO(answersOption));
        }
        this.candidateAnswers = generateAnswerOptionDTOS;
        for (AnswersOption answersOption:candidateMultiTask.getTask().getAnswersOptions()) {
            allAnswerOptions.add(new GenerateAnswerOptionDTO(answersOption));
        }
        this.allAnswerOptions = allAnswerOptions;
    }

    public GenerateTaskDTO(CandidateSingleTask candidateSingleTask) {
        this.id = candidateSingleTask.getId();
        this.taskId = candidateSingleTask.getTask().getId();
        this.title = candidateSingleTask.getTask().getTitle();
        this.description = candidateSingleTask.getTask().getDescription();
        this.taskType = TaskTypeEnum.fromString(candidateSingleTask.getTask().getTaskType().name()).getType();
        List<GenerateAnswerOptionDTO> generateAnswerOptionDTOS = new ArrayList<>();
        List<GenerateAnswerOptionDTO> allAnswerOptions = new ArrayList<>();
        if (candidateSingleTask.getAnswersOption() != null) {
            AnswersOption answersOption = candidateSingleTask.getAnswersOption();
            GenerateAnswerOptionDTO generateAnswerOptionDTO = new GenerateAnswerOptionDTO(answersOption);
            generateAnswerOptionDTOS.add(generateAnswerOptionDTO);
            this.candidateAnswers = generateAnswerOptionDTOS;
        } else {
            this.candidateAnswers = new ArrayList<>();
        }
        this.candidateAnswers = generateAnswerOptionDTOS;
        for (AnswersOption answersOption:candidateSingleTask.getTask().getAnswersOptions()) {
            allAnswerOptions.add(new GenerateAnswerOptionDTO(answersOption));
        }
        this.allAnswerOptions = allAnswerOptions;

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
            this.candidateAnswers = generateAnswerOptionDTOS;
        } else {
            this.candidateAnswers = new ArrayList<>();
        }
        this.allAnswerOptions = null;
    }
}
