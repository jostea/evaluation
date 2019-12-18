package com.internship.evaluation.service;

import com.internship.evaluation.config.NotificationConfiguration;
import com.internship.evaluation.model.entity.Candidate;
import com.internship.evaluation.model.entity.Stream;
import com.internship.evaluation.model.entity.StreamTime;
import com.internship.evaluation.model.entity.TestToken;
import com.internship.evaluation.repository.CandidateRepository;
import com.internship.evaluation.repository.StreamRepository;
import com.internship.evaluation.repository.StreamTimeRepository;
import com.internship.evaluation.repository.TestTokenRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;
import java.util.Properties;

@Data
@AllArgsConstructor
@Service
public class NotificationService {

    @Autowired
    private Environment env;

    private NotificationConfiguration notificationConfiguration;
    private final CandidateRepository candidateRepository;
    private final TestTokenRepository testTokenRepository;
    private final StreamRepository streamRepository;
    private final StreamTimeRepository streamTimeRepository;

    public void sendTestInvite(Long candId) {
        //Get Candidate
        Optional<Candidate> candOpt = candidateRepository.findById(candId);

        Candidate candidate;
        TestToken testToken = null;

        if (candOpt.isPresent()) {
            candidate = candOpt.get();

            //get Test_Token
            Optional<TestToken> testTokenOpt = testTokenRepository.findFirstByCandidateId(candId);

            if (testTokenOpt.isPresent()) {
                testToken = testTokenOpt.get();

                //get Stream
                Stream stream = streamRepository.findById(candidate.getStream().getId()).get();

                //get time to pass test for the selected stream
                Integer timeTest = 0;
                Optional<StreamTime> streamTimeOptional = streamTimeRepository.findFirstByStream(stream);
                if (streamTimeOptional.isPresent()) {
                    timeTest = streamTimeOptional.get().getTimeTest();
                }

                String tokenValidity = env.getProperty("test_token.validity");

                sendRegistrationEmail(candidate, stream, testToken, tokenValidity, timeTest);
            }
        }
    }

    private void sendRegistrationEmail(Candidate candidate, Stream stream, TestToken testToken, String tokenValidity, Integer timeTest) {
        JavaMailSenderImpl javaMailSender = prepareJavaMailSender();

        //Construct URI
        String evalPortalHost = env.getProperty("eval_portal.host");
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(evalPortalHost)
                .path("/testStart")
                .query("thd_i8={keyword}")
                .buildAndExpand(testToken.getToken());

        StringBuilder text = new StringBuilder();

        text.append("Hello ")
                .append(candidate.getFirstName())
                .append(" ")
                .append(candidate.getLastName())
                .append(",")
                .append("\n\nYou have been registered to take the test for the discipline ")
                .append(stream.getDiscipline().getName())
                .append(" and stream ")
                .append(stream.getName())
                .append(".")
                .append("\n\nPlease follow the link to start the test: ")
                .append(uriComponents.toUriString())
                .append("\n\nYou have ")
                .append(tokenValidity)
                .append(" minutes to start the test. ")
                .append("In case you start later you will have to register again.")
                .append("\n\nYou will have ")
                .append(timeTest)
                .append(" minutes to pass the test.")
                .append("\n\nWe wish you good luck! You will be contacted in case you successfully pass the test.")
                .append("\n\nENDAVA TEAM");

        String subject = "ENDAVA Internship Test: discipline ["
                + stream.getDiscipline().getName() + "], stream ["
                + stream.getName() + "]";

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(candidate.getEmail());
        mailMessage.setSubject(subject);
        mailMessage.setText(text.toString());

        javaMailSender.send(mailMessage);
    }

    private JavaMailSenderImpl prepareJavaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(notificationConfiguration.getHost());
        javaMailSender.setUsername(notificationConfiguration.getUsername());
        javaMailSender.setPassword(notificationConfiguration.getPassword());
        javaMailSender.setHost(notificationConfiguration.getHost());

        Properties props = javaMailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return javaMailSender;
    }
}
