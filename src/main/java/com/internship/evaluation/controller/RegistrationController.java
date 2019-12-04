package com.internship.evaluation.controller;

import com.internship.evaluation.model.dto.discipline.DisciplineListDTO;
import com.internship.evaluation.model.dto.stream.StreamDTO;
import com.internship.evaluation.service.InternshipService;
import com.internship.evaluation.service.RestAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final InternshipService internshipService;
    private final RestAdminService restAdminService;

    @GetMapping(value = "/registration")
    public ModelAndView registration() {
        ModelAndView model = new ModelAndView("registration/registration");
        model.addObject("internship_name", internshipService.getCurrentInternshipName());
        model.addObject("internship_id", internshipService.getCurrentInternshipId());

        StreamDTO[] streams = restAdminService.getInternshipStreams();
        List<StreamDTO> listInternshipStreams = new ArrayList<>(Arrays.asList(streams));
        model.addObject("streams", listInternshipStreams);

        //disciplines
        HashSet<DisciplineListDTO> disciplines = new HashSet<>();
        for (StreamDTO stream : streams) {
            DisciplineListDTO discDto = new DisciplineListDTO(stream.getDisciplineId(), stream.getDisciplineName());
            if (!disciplines.contains(discDto)){
                disciplines.add(discDto);
            }
        }
        model.addObject("disciplines", disciplines);

        return model;
    }


}
