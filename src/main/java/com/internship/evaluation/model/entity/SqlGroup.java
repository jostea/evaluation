package com.internship.evaluation.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "sql_group_table")
public class SqlGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Name of SQL Group is required")
    private String name;

    @NotNull(message = "Path to image is required")
    @Column(name = "image_path")
    private String imagePath;

    @OneToMany(mappedBy = "sqlGroup")
    private List<SqlTask> sqlTasks;

    @Override
    public String toString() {
        return "SqlGroup{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
