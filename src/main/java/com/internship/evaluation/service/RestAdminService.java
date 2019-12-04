package com.internship.evaluation.service;

import com.internship.evaluation.model.dto.stream.StreamDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestAdminService {

    @Autowired
    private Environment env;

    private final RestTemplate restTemplate;

    public RestAdminService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public StreamDTO[] getInternshipStreams(){
        String url = env.getProperty("adminconnect.property")+"/streamView/streams/internship";
        ResponseEntity<StreamDTO[]> response = restTemplate.getForEntity(url, StreamDTO[].class);
        StreamDTO[] array = response.getBody();
        return array;
    }
 }
