package com.internship.evaluation.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "internship_table")
public class Internship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name of internship is required")
    private String name;

    @Column(name = "is_current")
    private boolean isCurrent;

    @OneToMany(mappedBy = "internship")
    private List<Candidate> candidates;

    @ManyToMany
    @JoinTable(name = "internship_stream_table", joinColumns = @JoinColumn(name = "internship_id"),
            inverseJoinColumns = @JoinColumn(name = "stream_id"))
    private List<Stream> streams;

    @Override
    public String toString() {
        return "Internship{" +
                "name='" + name + '\'' +
                ", isCurrent=" + isCurrent +
                '}';
    }
}
