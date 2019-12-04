package com.internship.evaluation.service;

import com.internship.evaluation.model.entity.Internship;
import com.internship.evaluation.repository.InternshipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class InternshipService {

    private final InternshipRepository internshipRepository;

    public String getCurrentInternshipName(){
        String name = "";
        Optional<Internship> optionalInternship = internshipRepository.findFirstByIsCurrentTrue();
        if (optionalInternship.isPresent()){
            name = optionalInternship.get().getName();
        }
        return name;
    }

    public Long getCurrentInternshipId(){
        Long id = null;
        Optional<Internship> optionalInternship = internshipRepository.findFirstByIsCurrentTrue();
        if (optionalInternship.isPresent()){
            id = optionalInternship.get().getId();
        }
        return id;
    }
}
