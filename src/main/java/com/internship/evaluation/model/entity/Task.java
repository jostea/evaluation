package com.internship.evaluation.model.entity;

import com.internship.evaluation.model.enums.ComplexityEnum;
import com.internship.evaluation.model.enums.TypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "task_table")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Title is required")
    private String title;

    @NotNull(message = "Description is required")
    private String description;

    @NotNull(message = "Type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "task_type")
    private TypeEnum taskType;

    @NotNull(message = "Complexity is required")
    @Enumerated(EnumType.STRING)
    private ComplexityEnum complexity;

    @Value("true")
    @Column(name = "is_enabled")
    private boolean isEnabled;

    @ManyToMany
    @JoinTable(name = "task_stream_table", joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "stream_id"))
    private List<Stream> streams;

    @OneToMany(mappedBy = "task")
    private List<AnswersOption> answersOptions;

    @OneToMany(mappedBy = "task")
    private List<CandidateCustomTask> candidateCustomTasks;

    @OneToMany(mappedBy = "task")
    private List<CandidateSingleTask> candidateSingleTasks;

    @OneToMany(mappedBy = "task")
    private List<CandidateMultiTask> candidateMultiTasks;

}
