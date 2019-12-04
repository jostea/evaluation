package com.internship.evaluation.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "discipline_table")
public class Discipline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Discipline name is required")
    private String name;

    @OneToMany(mappedBy = "discipline")
    private List<Stream> streams;

    @Override
    public String toString() {
        return "Discipline{" +
                "name=" + name;
    }
}
