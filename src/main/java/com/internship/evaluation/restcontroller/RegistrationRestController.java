package com.internship.evaluation.restcontroller;

import com.internship.evaluation.model.dto.candidate.CandidateRegistrationDTO;
import com.internship.evaluation.service.CandidateService;
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

    @PostMapping("/addCandidate")
    public ResponseEntity<String> saveTask(@RequestBody CandidateRegistrationDTO candidateDTO) {
        try{
            if (!candidateService.addCandidate(candidateDTO)){
                log.info("[Candidate: " + candidateDTO.toString() + "] has tried to register again for the same internship and stream.");
                return new ResponseEntity<>("Such candidate is already registered for the selected stream and internship", HttpStatus.BAD_REQUEST);
            } else {
                log.info("[Candidate: " + candidateDTO.toString() + "] was registered.");
                return new ResponseEntity<>("Candidate was registered.", HttpStatus.OK);
            }
        } catch (Exception e){
            log.error("Error while trying to add new candidate: " + candidateDTO.toString() + "; \nStack Trace: " + e.getStackTrace());
            return new ResponseEntity<>("Error while additing new candidate", HttpStatus.BAD_REQUEST);
        }
    }
}
