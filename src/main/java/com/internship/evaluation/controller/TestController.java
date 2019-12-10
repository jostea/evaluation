package com.internship.evaluation.controller;

import com.internship.evaluation.model.dto.candidate.CandidateStartTestDTO;
import com.internship.evaluation.model.enums.TestStatusEnum;
import com.internship.evaluation.service.CandidateService;
import com.internship.evaluation.service.InternshipService;
import com.internship.evaluation.service.StreamTimeService;
import com.internship.evaluation.service.TestTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TestController {

    @Autowired
    private Environment env;

    private final InternshipService internshipService;
    private final TestTokenService tokenService;
    private final CandidateService candidateService;
    private final StreamTimeService streamTimeService;

    @GetMapping(value = "/testStart")
    public ModelAndView startTest(@RequestParam String thd_i8) {
        ModelAndView model = null;

        //check if token is valid. If not valid -> return error page
        Timestamp dateTokenCreated = tokenService.getTokenDateCreated(thd_i8);

        if (dateTokenCreated != null) {

            Integer tokenValidity = Integer.valueOf(env.getProperty("test_token.validity"));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(dateTokenCreated.getTime());
            calendar.add(Calendar.MINUTE, tokenValidity);
            Timestamp dateTokenValidTo = new Timestamp(calendar.getTime().getTime());

            if (dateTokenValidTo.after(Timestamp.valueOf(LocalDateTime.now()))) {
                model = new ModelAndView("test/testStart");
                CandidateStartTestDTO candidateStartTestDTO = candidateService.getCandidateStartTestByToken(thd_i8);

                if (candidateStartTestDTO.getTestStatusEnum().equals(TestStatusEnum.TEST_STARTED)){
                    return new ModelAndView("redirect:test?thd_i8=" + thd_i8);
                } else if (candidateStartTestDTO.getTestStatusEnum().equals(TestStatusEnum.TEST_FINISHED)){
                    model = new ModelAndView("templates/error");
                }

                else {
                    if (candidateStartTestDTO != null) {
                        model.addObject("internship_name", candidateStartTestDTO.getInternshipName());
                        model.addObject("discipline", candidateStartTestDTO.getDisciplineName());
                        model.addObject("stream", candidateStartTestDTO.getStreamName());
                        model.addObject("test_time", candidateStartTestDTO.getTestTime());
                        model.addObject("first_name", candidateStartTestDTO.getFirstName());
                        model.addObject("last_name", candidateStartTestDTO.getLastName());
                    } else {
                        model = new ModelAndView("templates/error");
                    }
                }
            } else {
                model = new ModelAndView("templates/error");
            }
        }
        return model;
    }
}
