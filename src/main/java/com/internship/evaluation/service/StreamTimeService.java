package com.internship.evaluation.service;

import com.internship.evaluation.model.dto.stream_time.StreamTimeDTO;
import com.internship.evaluation.model.entity.StreamTime;
import com.internship.evaluation.repository.StreamTimeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class StreamTimeService {

    private final StreamTimeRepository streamTimeRepository;

    public StreamTimeDTO getStreamTimeByStreamId(Long streamId){
        Optional<StreamTime> streamTimeOptional = streamTimeRepository.findFirstByStream_Id(streamId);
        if ( streamTimeOptional.isPresent() ){
            return new StreamTimeDTO(streamTimeOptional.get());
        }
        else {
            return null;
        }
    }
}
