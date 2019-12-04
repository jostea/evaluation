package com.internship.evaluation.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "test_token_table")
public class TestToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "candidate_id", referencedColumnName = "id")
    private Candidate candidate;

    @NotEmpty
    private String token;

    @NotNull
    @Column(name = "date_created")
    private Timestamp dateCreated;

    @Column(name = "is_active")
    private boolean isActive;

}
