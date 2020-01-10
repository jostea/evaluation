package com.internship.evaluation.service;

import com.internship.evaluation.model.entity.Internship;
import com.internship.evaluation.repository.InternshipRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InternshipServiceTest {
    @Mock
    private InternshipRepository internshipRepository;

    @InjectMocks
    private InternshipService internshipService;


    @Test
    public void shouldGetCurrentInternshipName() {
        Internship mockedInternship = Internship.builder()
                .id(1L)
                .isCurrent(true)
                .name("SomeName")
                .build();

        when(internshipRepository.findFirstByIsCurrentTrue()).thenReturn(Optional.of(mockedInternship));
        String returnedResult = internshipService.getCurrentInternshipName();
        verify(internshipRepository).findFirstByIsCurrentTrue();
        assertThat("SomeName").isEqualTo(returnedResult);

    }

    @Test
    public void shouldGetCurrentInternshipId() {
        Internship mockedInternship = Internship.builder()
                .id(1L)
                .isCurrent(true)
                .name("SomeName")
                .build();

        when(internshipRepository.findFirstByIsCurrentTrue()).thenReturn(Optional.of(mockedInternship));
        Long returnedResult = internshipService.getCurrentInternshipId();
        verify(internshipRepository).findFirstByIsCurrentTrue();
        assertThat(1L).isEqualTo(returnedResult);

    }

    @Test
    public void shouldGetCurrentInternship() {
        Internship mockedInternship = Internship.builder()
                .id(1L)
                .isCurrent(true)
                .name("SomeName")
                .build();
        Optional<Internship> optionalInternship=Optional.of(mockedInternship);
        when(internshipRepository.findFirstByIsCurrentTrue()).thenReturn(Optional.of(mockedInternship));
        Optional<Internship> returnedResult = internshipService.getCurrentInternship();
        verify(internshipRepository).findFirstByIsCurrentTrue();
        assertThat(optionalInternship).isEqualTo(returnedResult);

    }


}
