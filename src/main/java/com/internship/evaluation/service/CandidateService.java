package com.internship.evaluation.service;

import com.internship.evaluation.model.dto.candidate.CandidateRegistrationDTO;
import com.internship.evaluation.model.entity.Candidate;
import com.internship.evaluation.model.entity.Internship;
import com.internship.evaluation.model.entity.Stream;
import com.internship.evaluation.model.enums.TestStatusEnum;
import com.internship.evaluation.repository.CandidateRepository;
import com.internship.evaluation.repository.InternshipRepository;
import com.internship.evaluation.repository.StreamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final StreamRepository streamRepository;
    private final InternshipRepository internshipRepository;

    public boolean addCandidate(CandidateRegistrationDTO candidateDto) {
        //result of operation (is saved or not)
        boolean result;

        //get Stream by id
        Optional<Stream> streamOpt = streamRepository.findById(candidateDto.getStreamId());
        Stream streamDb = null;
        if (streamOpt.isPresent()) {
            streamDb = streamOpt.get();
        }

        //get Internship by id
        Optional<Internship> internshipOpt = internshipRepository.findById(candidateDto.getInternshipId());
        Internship internshipDb = null;
        if (internshipOpt.isPresent()) {
            internshipDb = internshipOpt.get();
        }

        //check if in DB already exists this Candidate (email, stream_id, internship_id)
        if (candidateExists(candidateDto.getEmail(), internshipDb, streamDb)) {
            result = false;
        } else {
            Candidate newCandidate = new Candidate(candidateDto);
            newCandidate.setStream(streamDb);
            newCandidate.setInternship(internshipDb);
            newCandidate.setTestStatus(TestStatusEnum.WAITING_ACTIVATION);
            newCandidate.setDateRegistered(Timestamp.valueOf(LocalDateTime.now()));
            candidateRepository.save(newCandidate);
            result = true;
        }
        return result;
    }

    /**
     * Method to check if there alreay exist a Candidat with the same email, stream and internship
     *
     * @param email
     * @param internship
     * @param stream
     * @return
     */
    private boolean candidateExists(String email, Internship internship, Stream stream) {
        boolean result = true;
        List<Candidate> inDb = candidateRepository.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .collect(Collectors.toList());

        if (inDb.size() == 0){
            return false;
        }

        for (Candidate candidate : inDb) {
            //compare Stream and Internship
            if (candidate.getStream().getId() == stream.getId() && candidate.getInternship().getId() == internship.getId()) {
                result = true;
                break;
            } else {
                result = false;
            }
        }
        return result;
    }
}

