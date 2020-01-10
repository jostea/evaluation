package com.internship.evaluation.restcontroller;

import com.internship.evaluation.model.dto.candidate.CandidateRegistrationDTO;
import com.internship.evaluation.service.CandidateService;
import com.internship.evaluation.service.NotificationService;
import com.internship.evaluation.service.TestTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/registrationrest")
public class RegistrationRestController {

    private final CandidateService candidateService;
    private final TestTokenService testTokenService;
    private final NotificationService notificationService;

    @PostMapping("/addCandidate")
    public ResponseEntity<String> saveTask(@RequestBody CandidateRegistrationDTO candidateDTO) {
        try{
            Long newRegCandId = candidateService.addCandidate(candidateDTO);
            if ( newRegCandId == 0 ){
                log.info("[Candidate: " + candidateDTO.toString() + "] has tried to register again for the same internship and stream.");
                return new ResponseEntity<>("Such candidate is already registered for the selected stream and internship", HttpStatus.BAD_REQUEST);
            } else {
                //generate token
                testTokenService.generateNewTestToken(newRegCandId);

                //send notification email to the new registered candidate
                notificationService.sendTestInvite(newRegCandId);
                log.info("[Candidate: " + candidateDTO.toString() + "] was registered.");
                return new ResponseEntity<>("Candidate was registered.", HttpStatus.OK);
            }
        } catch (Exception e){
            log.error("Error while trying to add new candidate: " + candidateDTO.toString(), e);
            return new ResponseEntity<>("Error while additing new candidate", HttpStatus.BAD_REQUEST);
        }
    }
}
