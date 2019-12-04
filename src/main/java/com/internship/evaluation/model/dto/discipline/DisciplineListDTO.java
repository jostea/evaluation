package com.internship.evaluation.model.dto.discipline;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DisciplineListDTO {

    private Long id;
    private String name;

}
