package com.internship.evaluation.model.dto.generate_test;

import lombok.Data;

import java.util.List;

@Data
public class GenerateTestDTO {
    private List<GenerateTaskDTO> tasks;
    private List<GenerateSqlTaskDTO> sqlTasks;
    private List<GenerateCodeTaskDTO> codeTasks;
}
