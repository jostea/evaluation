package com.internship.evaluation.model.entity;

import com.internship.evaluation.model.enums.SkillsTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "skills_table")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Skill is required")
    private String name;

    @NotNull(message = "Skill type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "skill_type")
    private SkillsTypeEnum skillType;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "skills_stream_table", joinColumns = @JoinColumn(name = "skill_id"),
            inverseJoinColumns = @JoinColumn(name = "stream_id"))
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Stream> streams;

}
