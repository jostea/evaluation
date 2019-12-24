package com.internship.evaluation.restcontroller;

import com.internship.evaluation.exception.TimeOut;
import com.internship.evaluation.service.TimerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/getLeftTime")
public class TimerRestController {

    private final TimerService timerService;

    @GetMapping("/{token}")
    public ResponseEntity<Integer> getLeftTime(@PathVariable("token") String token) {
        try {
            return new ResponseEntity<>(timerService.getLeftTime(token), HttpStatus.OK);
        } catch (TimeOut timeOut) {
            log.error("Time out for candidate with token: " + token);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
