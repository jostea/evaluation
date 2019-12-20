package com.internship.evaluation.restcontroller;

import com.internship.evaluation.exception.CandidateNotFound;
import com.internship.evaluation.exception.StreamNotFound;
import com.internship.evaluation.model.dto.candidate.candidateskill.CandidateSkillDTO;
import com.internship.evaluation.model.dto.candidate.candidateskill.CandidateSkillsDTOFromUI;
import com.internship.evaluation.model.dto.skill.SkillDTO;
import com.internship.evaluation.model.dto.skill.SkillsSpecifiedByStreamDTO;
import com.internship.evaluation.service.CandidateSkillService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/candidateskill")
public class CandidateSkillRestController {

    private final CandidateSkillService candidateSkillService;

    @GetMapping("/getcandidatestream/{token}")
    public ResponseEntity<List<SkillsSpecifiedByStreamDTO>> getCandidatesSkills(@PathVariable("token") String token, Authentication authentication) {
        try {
            return new ResponseEntity<>(candidateSkillService.getSkillForSpecifiedStream(token), HttpStatus.OK);
        } catch (StreamNotFound e) {
            log.error("Error when user '" + authentication.getName() + "' get candidate's skills specified by stream; \nerror message: " + e.getMessage()
                    + "\nstack trace: " + e.getStackTrace());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e) {
            log.error("Error when user '" + authentication.getName() + "' get candidate's skills; \nerror message: " + e.getMessage()
                    + "\nstack trace: " + e.getStackTrace());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getsortedskills/{token}")
    public ResponseEntity<List<List<SkillDTO>>> getSkillsSpecifiedByType(@PathVariable("token") String token, Authentication authentication) {
        try {
            return new ResponseEntity<>(
                    candidateSkillService.getSkillSpecifiedBySkillType(token), HttpStatus.OK);
        } catch (StreamNotFound e) {
            log.error("Error when user '" + authentication.getName() + "' get candidate's skills; \nerror message: " + e.getMessage()
                    + "\nstack trace: " + e.getStackTrace());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error when user '" + authentication.getName() + "' get candidate's skills; \nerror message: " + e.getMessage()
                    + "\nstack trace: " + e.getStackTrace());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @ResponseBody
    @PutMapping("/saveCandidateSkills/{token}")
    public ResponseEntity<String> saveCandidatesSkills(@PathVariable("token") String token, @RequestBody List<CandidateSkillsDTOFromUI> list, Authentication authentication) {
        candidateSkillService.updateCandidateSkills(list, token);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/populateCandidatesSkills/{token}")
    public ResponseEntity<String> populateCandidateSkills(@PathVariable("token") String token, Authentication authentication) {
        try {
            candidateSkillService.populateCandidatesSkills(token);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (StreamNotFound e) {
            log.error("Error when user '" + authentication.getName() + "' get candidate's skills; \nerror message: " + e.getMessage()
                    + "\nstack trace: " + e.getStackTrace());
            return new ResponseEntity<>("This candidate didn't have stream",HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/findallcandidateskills/{token}")
    public ResponseEntity<List<CandidateSkillDTO>> getAllCandidatesSkills(@PathVariable("token") String token, Authentication authentication) {
        try {
            return new ResponseEntity<>(candidateSkillService.findAllCandidatesSkills(token), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error when user '" + authentication.getName() + "' get all candidate's skills; \nerror message: " + e.getMessage()
                    + "\nstack trace: " + e.getStackTrace());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
