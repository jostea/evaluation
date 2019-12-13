package com.internship.evaluation.model.dto.generate_test;

import com.internship.evaluation.model.entity.CodeTask;
import lombok.Data;

@Data
public class GenerateCodeTaskDTO {
    private Long id;
    private String title;
    private String description;
    private String signature;

    public GenerateCodeTaskDTO(CodeTask codeTask) {
        this.id = codeTask.getId();
        this.title = codeTask.getTitle();
        this.description = codeTask.getDescription();
        this.signature = codeTask.getSignature();
    }
}
