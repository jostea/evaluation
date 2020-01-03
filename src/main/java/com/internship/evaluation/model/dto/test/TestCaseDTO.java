package com.internship.evaluation.model.dto.test;

import com.internship.evaluation.model.entity.CorrectCode;
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

    public TestCaseDTO(CorrectCode correctCode) {
        this.caseId = correctCode.getId().toString();
        this.input = correctCode.getInput();
        this.output = correctCode.getOutput();
    }
}
