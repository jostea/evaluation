package com.internship.evaluation.model.dto.test;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CandidateCheckedCodeTasksDTO {
    List<CandidateCodeResultDTO> candidateCodeResultDTOS;
}
