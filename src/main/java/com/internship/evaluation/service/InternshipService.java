package com.internship.evaluation.service;

import com.internship.evaluation.model.entity.Internship;
import com.internship.evaluation.repository.InternshipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class InternshipService {

    private final InternshipRepository internshipRepository;

//    public InternshipService(InternshipRepository internshipRepository) {
//        this.internshipRepository = internshipRepository;
//    }

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

    public Optional<Internship> getCurrentInternship(){
        Optional<Internship> optionalInternship = internshipRepository.findFirstByIsCurrentTrue();
        return optionalInternship;
    }
}
