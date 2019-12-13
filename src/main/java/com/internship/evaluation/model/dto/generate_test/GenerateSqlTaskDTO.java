package com.internship.evaluation.model.dto.generate_test;

import com.internship.evaluation.model.entity.SqlTask;
import lombok.Data;

@Data
public class GenerateSqlTaskDTO {
    private Long id;
    private String title;
    private String description;
    private SqlGroupDTO sqlGroup;

    public GenerateSqlTaskDTO(SqlTask sqlTask) {
        this.id = sqlTask.getId();
        this.title = sqlTask.getTitle();
        this.description = sqlTask.getDescription();
        this.sqlGroup = new SqlGroupDTO(sqlTask.getSqlGroup());
    }
}
