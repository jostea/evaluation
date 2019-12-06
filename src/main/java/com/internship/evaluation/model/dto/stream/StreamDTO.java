package com.internship.evaluation.model.dto.stream;

import com.internship.evaluation.model.entity.Stream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StreamDTO {

    private Long id;

    private String name;

    private String disciplineName;

    private Long disciplineId;

    public StreamDTO(Stream stream) {
        this.disciplineId = stream.getDiscipline().getId();
        this.id = stream.getId();
        this.name = stream.getName();
        this.disciplineName = stream.getDiscipline().getName();
    }
}
