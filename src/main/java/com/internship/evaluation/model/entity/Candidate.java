package com.internship.evaluation.model.entity;

import com.internship.evaluation.model.dto.candidate.CandidateRegistrationDTO;
import com.internship.evaluation.model.enums.TestStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

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

    @OneToMany(mappedBy = "candidate", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<CandidateSkill> candidateSkills;

    @OneToMany(mappedBy = "candidate", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<CandidateCustomTask> candidateCustomTasks;

    @OneToMany(mappedBy = "candidate", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<CandidateSingleTask> candidateSingleTasks;

    @OneToMany(mappedBy = "candidate", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<CandidateSqlTask> candidateSqlTasks;

    @OneToMany(mappedBy = "candidate", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<CandidateCodeTask> candidateCodeTasks;

    @OneToMany(mappedBy = "candidate", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<CandidateMultiTask> candidateMultiTasks;

    @OneToOne(mappedBy = "candidate")
    private TestToken testToken;

    //copy constructor from CandidateRegistrationDTO
    public Candidate(CandidateRegistrationDTO dto){
        this.setEmail(dto.getEmail());
        this.setFirstName(dto.getFirstName());
        this.setLastName(dto.getLastName());
        this.setPhone(dto.getPhone());
    }

    @Override
    public String toString() {
        return "Candidate{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", dateRegistered=" + dateRegistered +
                ", testStatus=" + testStatus +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Candidate candidate = (Candidate) o;
        return Objects.equals(id, candidate.id) &&
                Objects.equals(email, candidate.email) &&
                Objects.equals(firstName, candidate.firstName) &&
                Objects.equals(lastName, candidate.lastName) &&
                Objects.equals(phone, candidate.phone) &&
                Objects.equals(internship, candidate.internship) &&
                Objects.equals(dateRegistered, candidate.dateRegistered) &&
                Objects.equals(dateTestStarted, candidate.dateTestStarted) &&
                Objects.equals(dateTestFinished, candidate.dateTestFinished) &&
                testStatus == candidate.testStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, firstName, lastName, phone, internship, dateRegistered, dateTestStarted, dateTestFinished, testStatus);
    }
}
