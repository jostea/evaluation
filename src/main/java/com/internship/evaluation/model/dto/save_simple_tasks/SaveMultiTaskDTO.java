package com.internship.evaluation.model.dto.save_simple_tasks;

import lombok.Data;

import java.util.List;

@Data
public class SaveMultiTaskDTO {
    Long candidateTaskId;
    List<Long> multiTaskAnswers;
}
