package com.internship.evaluation.model.dto.candidate;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CandidateNotificationDTO {

    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "First Name cannot be null")
    private String firstName;

    @NotNull(message = "Last Name cannot be null")
    private String lastName;

    @NotNull
    private String internshipName;

    @NotNull
    private  String disciplineName;

    @NotNull
    private String streamName;

    @NotNull
    private String urlTestLink;

    @NotNull
    private String token;

    public CandidateNotificationDTO(CandidateRegistrationDTO dto){
        this.setEmail(dto.getEmail());
        this.setFirstName(dto.getFirstName());
        this.setLastName(dto.getLastName());
    }
}
