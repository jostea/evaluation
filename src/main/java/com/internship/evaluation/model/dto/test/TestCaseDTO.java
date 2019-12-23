package com.internship.evaluation.model.dto.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestCaseDTO {
    private String caseId;
    private String input;
    private String output; //TODO: Actually do we need it here???
}
