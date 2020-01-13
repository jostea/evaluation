package com.internship.evaluation.model.dto.generate_test;

import com.internship.evaluation.model.entity.SqlGroup;
import lombok.Data;

@Data
public class SqlGroupDTO {
    private Long id;
    private String name;
    private String imagePath;

    public SqlGroupDTO(SqlGroup sqlGroup) {
        this.id = sqlGroup.getId();
        this.name = sqlGroup.getName();
        this.imagePath = sqlGroup.getImagePath();
    }
}
