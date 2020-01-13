package com.internship.evaluation.model.dto.stream_time;

import com.internship.evaluation.model.entity.StreamTime;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class StreamTimeDTO {

    private Long streamId;
    private Integer timeTest;

    public StreamTimeDTO(StreamTime entity){
        this.setStreamId(entity.getStream().getId());
        this.setTimeTest(entity.getTimeTest());
    }

}
