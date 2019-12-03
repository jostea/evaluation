package com.internship.evaluation.model.entity;

import com.internship.evaluation.model.enums.TestStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "candidate_table")
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Field is required")
    @Email(message = "Please provide a valid email")
    private String email;

    @NotNull(message = "Field is required")
    @Column(name = "first_name")
    private String firstName;

    @NotNull(message = "Field is required")
    @Column(name = "last_name")
    private String lastName;

    private String phone;

    @ManyToOne
    @JoinColumn(name = "internship_id")
    private Internship internship;

    @ManyToOne
    @JoinColumn(name = "stream_id")
    private Stream stream;

    @NotNull
    @Column(name = "date_registered")
    private Timestamp dateRegistered;

    @Column(name = "date_test_started")
    private Timestamp dateTestStarted;

    @Column(name = "date_test_finished")
    private Timestamp dateTestFinished;

    @NotNull
    @Column(name = "test_status")
    @Enumerated(EnumType.STRING)
    private TestStatusEnum testStatus;

    @OneToMany(mappedBy = "candidate")
    private List<CandidateSkill> candidateSkills;

    @OneToMany(mappedBy = "candidate")
    private List<CandidateCustomTask> candidateCustomTasks;

    @OneToMany(mappedBy = "candidate")
    private List<CandidateSingleTask> candidateSingleTasks;

    @OneToMany(mappedBy = "candidate")
    private List<CandidateSqlTask> candidateSqlTasks;

    @OneToMany(mappedBy = "candidate")
    private List<CandidateCodeTask> candidateCodeTasks;

    @OneToMany(mappedBy = "candidate")
    private List<CandidateMultiTask> candidateMultiTasks;

    @OneToOne(mappedBy = "candidate")
    private TestToken testToken;

}
