package com.internship.evaluation.model.dto.candidate;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CandidateRegistrationDTO {

    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "First Name cannot be null")
    private String firstName;

    @NotNull(message = "Last Name cannot be null")
    private String lastName;

    private String phone;

    @NotNull
    private Long internshipId;

    @NotNull
    private Long streamId;

}
