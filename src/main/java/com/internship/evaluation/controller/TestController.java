package com.internship.evaluation.controller;

import com.internship.evaluation.model.dto.candidate.CandidateStartTestDTO;
import com.internship.evaluation.model.entity.Candidate;
import com.internship.evaluation.model.enums.TestStatusEnum;
import com.internship.evaluation.service.CandidateService;
import com.internship.evaluation.service.TestTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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

    private final TestTokenService tokenService;
    private final CandidateService candidateService;

    @GetMapping(value = "/testStart")
    public ModelAndView startTest(@RequestParam String thd_i8) {
        ModelAndView model = null;

        //check if token is valid. If not valid -> return error page
        Timestamp dateTokenCreated = tokenService.getTokenDateCreated(thd_i8);

        if (dateTokenCreated != null) {

            Timestamp dateTokenValidTo = getTimestampTokenValidTo(dateTokenCreated);

            if (dateTokenValidTo.after(Timestamp.valueOf(LocalDateTime.now()))) {
                CandidateStartTestDTO candidateStartTestDTO = candidateService.getCandidateStartTestByToken(thd_i8);

                if (candidateStartTestDTO.getTestStatusEnum().equals(TestStatusEnum.TEST_STARTED)) {
                    ModelMap map2 = new ModelMap();
                    map2.put("thd_i8", thd_i8);
                    model = new ModelAndView("redirect:/test/test", map2);
                    return model;
                } else if (candidateStartTestDTO.getTestStatusEnum().equals(TestStatusEnum.TEST_FINISHED)) {
                    model = new ModelAndView("templates/error");
                } else if (candidateStartTestDTO.getTestStatusEnum().equals(TestStatusEnum.WAITING_ACTIVATION)) {
                    if (candidateStartTestDTO != null) {
                        model = new ModelAndView("/test/testStart");
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

    @GetMapping(value = "/test/test")
    public ModelAndView test(String thd_i8) {
        //check status of candidate
        ModelAndView model = null;

        if (tokenService.getCandidateByToken(thd_i8).getTestStatus().equals(TestStatusEnum.TEST_STARTED)) {
            //check if token is valid. If not valid -> return error page
            Timestamp dateTokenCreated = tokenService.getTokenDateCreated(thd_i8);

            if (dateTokenCreated != null) {

                Timestamp dateTokenValidTo = getTimestampTokenValidTo(dateTokenCreated);

                if (dateTokenValidTo.after(Timestamp.valueOf(LocalDateTime.now()))) {
                    CandidateStartTestDTO candidateStartTestDTO = candidateService.getCandidateStartTestByToken(thd_i8);

                    if (candidateStartTestDTO.getTestStatusEnum().equals(TestStatusEnum.TEST_STARTED)
                            || candidateStartTestDTO.getTestStatusEnum().equals(TestStatusEnum.WAITING_ACTIVATION)) {
                        ModelMap map2 = new ModelMap();
                        map2.put("thd_i8", thd_i8);
                        model = new ModelAndView("/test/test", map2);
                        return model;
                    } else {
                        model = new ModelAndView("templates/error");
                    }
                } else {
                    model = new ModelAndView("templates/error");
                }
            }
        } else {
            model = new ModelAndView("redirect:/testStart");
            model.addObject("thd_i8", thd_i8);
        }
        return model;
    }

    @GetMapping(value = "/finish")
    public String tasks() {
        return "/test/testFinish";
    }

    private Timestamp getTimestampTokenValidTo(Timestamp dateTokenCreated){
        Integer tokenValidity = Integer.valueOf(env.getProperty("test_token.validity"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateTokenCreated.getTime());
        calendar.add(Calendar.MINUTE, tokenValidity);
        Timestamp dateTokenValidTo = new Timestamp(calendar.getTime().getTime());
        return dateTokenValidTo;
    }
}
