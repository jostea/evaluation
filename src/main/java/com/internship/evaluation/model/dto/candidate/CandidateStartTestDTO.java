package com.internship.evaluation.model.dto.candidate;

import com.internship.evaluation.model.entity.Candidate;
import com.internship.evaluation.model.enums.TestStatusEnum;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CandidateStartTestDTO {

    @NotNull
    private String internshipName;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String disciplineName;

    @NotNull
    private String streamName;

    @NotNull
    private Integer testTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TestStatusEnum testStatusEnum;

    public CandidateStartTestDTO(Candidate entity){
        this.setInternshipName(entity.getInternship().getName());
        this.setFirstName(entity.getFirstName());
        this.setLastName(entity.getLastName());
        this.setDisciplineName(entity.getStream().getDiscipline().getName());
        this.setStreamName(entity.getStream().getName());
        this.setTestStatusEnum(entity.getTestStatus());
    }

}
