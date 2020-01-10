package com.internship.evaluation.service;

import com.internship.evaluation.model.dto.candidate.CandidateNotificationDTO;
import com.internship.evaluation.model.dto.candidate.CandidateRegistrationDTO;
import com.internship.evaluation.model.dto.candidate.CandidateStartTestDTO;
import com.internship.evaluation.model.dto.generate_test.SqlAnswersDTO;
import com.internship.evaluation.model.dto.generate_test.SqlCandidateAnswerDTO;
import com.internship.evaluation.model.dto.save_code.SaveCodeAnswerDTOFromUI;
import com.internship.evaluation.model.dto.save_simple_tasks.SaveCustomAnswerDTO;
import com.internship.evaluation.model.dto.save_simple_tasks.SaveMultiTaskDTO;
import com.internship.evaluation.model.dto.save_simple_tasks.SaveSingleTaskDTO;
import com.internship.evaluation.model.dto.test_token.TestTokenDTO;
import com.internship.evaluation.model.entity.*;
import com.internship.evaluation.model.enums.TestStatusEnum;
import com.internship.evaluation.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final StreamRepository streamRepository;
    private final InternshipRepository internshipRepository;
    private final TestTokenService testTokenService;
    private final StreamTimeRepository streamTimeRepository;
    private final CandidateSqlTaskRepository candidateSqlTaskRepository;
    private final CandidateMultiTaskRepository candidateMultiTaskRepository;
    private final AnswersOptionRepository answersOptionRepository;
    private final CandidateSingleTaskRepository candidateSingleTaskRepository;
    private final CandidateCustomTaskRepository candidateCustomTaskRepository;
    private final CandidateCodeTaskRepository candidateCodeTaskRepository;
    private final CodeTaskRepository codeTaskRepository;

    public CandidateNotificationDTO getCandidateByToken(String token) {
        CandidateNotificationDTO candidateNotificationDTO = null;
        TestTokenDTO testTokenDTO = testTokenService.getTestTokenByToken(token);
        if (testTokenDTO != null) {
            Candidate entity = candidateRepository.getOne(testTokenDTO.getCandidateId());
            if (entity != null) {
                candidateNotificationDTO = new CandidateNotificationDTO(entity);
            }
        }
        return candidateNotificationDTO;
    }

    public CandidateStartTestDTO getCandidateStartTestByToken(String token) {
        CandidateStartTestDTO candidateStartTestDTO = null;
        TestTokenDTO testTokenDTO = testTokenService.getTestTokenByToken(token);
        if (testTokenDTO != null) {
            Candidate entity = candidateRepository.getOne(testTokenDTO.getCandidateId());
            if (entity != null) {
                candidateStartTestDTO = new CandidateStartTestDTO(entity);
                Optional<StreamTime> streamOpt = streamTimeRepository.findFirstByStream_Id(entity.getStream().getId());
                if (streamOpt.isPresent()) {
                    candidateStartTestDTO.setTestTime(streamOpt.get().getTimeTest());
                }
            }
        }
        return candidateStartTestDTO;
    }


    public Long addCandidate(CandidateRegistrationDTO candidateDto) {
        //result of operation (is saved or not)
        Long regCandId;

        //get Stream by id
        Optional<Stream> streamOpt = streamRepository.findById(candidateDto.getStreamId());
        Stream streamDb = null;
        if (streamOpt.isPresent()) {
            streamDb = streamOpt.get();
        }

        //get Internship by id
        Optional<Internship> internshipOpt = internshipRepository.findById(candidateDto.getInternshipId());
        Internship internshipDb = null;
        if (internshipOpt.isPresent()) {
            internshipDb = internshipOpt.get();
        }

        //check if in DB already exists this Candidate (email, stream_id, internship_id)
        if (candidateExists(candidateDto.getEmail(), internshipDb, streamDb)) {
            regCandId = 0L;     //assign "0" if such candidate already exists
        } else {
            Candidate newCandidate = new Candidate(candidateDto);
            newCandidate.setStream(streamDb);
            newCandidate.setInternship(internshipDb);
            newCandidate.setTestStatus(TestStatusEnum.WAITING_ACTIVATION);
            newCandidate.setDateRegistered(Timestamp.valueOf(LocalDateTime.now()));
            regCandId = candidateRepository.saveAndFlush(newCandidate).getId();
        }
        return regCandId;
    }

    public void updateCandidate(Candidate candidate) {
        candidateRepository.save(candidate);
    }

    public void saveCandidateSqlAnswers(Candidate candidate, SqlCandidateAnswerDTO dto) {
        ArrayList<CandidateSqlTask> sqlTasksAssignedToCandidate = new ArrayList<>(candidate.getCandidateSqlTasks());

        //Reconcile SqlTasks from UI to the SqlTasks from DB
        ArrayList<Long> idFromDB = new ArrayList<>();
        ArrayList<Long> idFromUI = new ArrayList<>();
        for (CandidateSqlTask candidateSqlTask : candidate.getCandidateSqlTasks()) {
            idFromDB.add(candidateSqlTask.getSqlTask().getId());
        }
        for (SqlAnswersDTO sqlAnswersDTO : dto.getAnswers()) {
            idFromUI.add(sqlAnswersDTO.getSqlTaskId());
        }

        ArrayList<Long> idFromDBcopy = new ArrayList<>(idFromDB);
        idFromDBcopy.removeAll(idFromUI);
        if (idFromDBcopy.size() > 0) {
            log.warn("There are [" + idFromDBcopy.size() + "] SQL tasks assigned to the candidate ("
                    + candidate.getTestToken() + "), which were not reconciled to the SQL answers arrived from UI");
        }

        ArrayList<Long> idFromUIcopy = new ArrayList<>(idFromUI);
        idFromUIcopy.removeAll(idFromDB);
        if (idFromUIcopy.size() > 0) {
            log.warn("There are [" + idFromDBcopy.size() + "] SQL tasks arrived from UI for candidate ("
                    + candidate.getTestToken() + "), which were not reconciled to the SQL answers from DB");
        }

        //Update candidate's sql task with sql answers provided
        ArrayList<CandidateSqlTask> sqlTasksToUpdate = new ArrayList<>();
        for (CandidateSqlTask candidateSqlTask : sqlTasksAssignedToCandidate) {
            String sqlStatementAnswer = dto.getAnswers()
                    .stream()
                    .filter(a -> a.getSqlTaskId().equals(candidateSqlTask.getSqlTask().getId()))
                    .findFirst()
                    .get()
                    .getSqlAnswer();
            candidateSqlTask.setStatementProvided(sqlStatementAnswer);
            sqlTasksToUpdate.add(candidateSqlTask);
        }
        candidate.setCandidateSqlTasks(sqlTasksToUpdate);
        candidateRepository.save(candidate);
    }

    public void saveCandidateMultiAnswers(SaveMultiTaskDTO saveMultiTaskDTO) throws Exception {
        try {
            CandidateMultiTask candidateMultiTaskLocal = candidateMultiTaskRepository.findById(saveMultiTaskDTO.getCandidateTaskId()).get();
            List<AnswersOption> newAnswers = new ArrayList<>();
            for (Long id : saveMultiTaskDTO.getMultiTaskAnswers()) {
                newAnswers.add(answersOptionRepository.findById(id).get());
            }
            candidateMultiTaskLocal.setAnswersOptions(newAnswers);
            candidateMultiTaskRepository.save(candidateMultiTaskLocal);
        } catch (Exception e) {
            throw new Exception("Task answers could not be updated");
        }
    }
    public void saveCandidateOneSqlAnswer(Candidate candidate, SqlCandidateAnswerDTO dto) {
        if (dto.getAnswers().size() == 1){
            SqlAnswersDTO sqlAnswersDTO = dto.getAnswers().get(0);
            ArrayList<CandidateSqlTask> sqlTasksAssignedToCandidate = new ArrayList<>(candidate.getCandidateSqlTasks());

            Optional<CandidateSqlTask> sqlTaskToUpdateOpt = sqlTasksAssignedToCandidate
                    .stream()
                    .filter(t -> t.getSqlTask().getId().equals(sqlAnswersDTO.getSqlTaskId()))
                    .findFirst();

            if (sqlTaskToUpdateOpt.isPresent()){
                CandidateSqlTask sqlTaskToUpdate = sqlTaskToUpdateOpt.get();
                sqlTasksAssignedToCandidate.remove(sqlTaskToUpdate);
                sqlTaskToUpdate.setStatementProvided(sqlAnswersDTO.getSqlAnswer());
                sqlTasksAssignedToCandidate.add(sqlTaskToUpdate);
                candidateRepository.save(candidate);
            }
        }
    }
    public void saveCandidateSingleAnswer(SaveSingleTaskDTO saveSingleTaskDTO) throws Exception {
        try {
            CandidateSingleTask candidateSingleTaskLocal = candidateSingleTaskRepository.findById(saveSingleTaskDTO.getCandidateTaskId()).get();
            candidateSingleTaskLocal.setAnswersOption(answersOptionRepository.findById(saveSingleTaskDTO.getSingleAnswer()).get());
            candidateSingleTaskRepository.save(candidateSingleTaskLocal);
        } catch (Exception e) {
            throw new Exception("Single answer could not be saved");
        }
    }

    public void saveCandidateCustomAnswer(SaveCustomAnswerDTO saveCustomAnswerDTO) throws Exception {
        try {
            CandidateCustomTask candidateCustomTaskLocal = candidateCustomTaskRepository.findById(saveCustomAnswerDTO.getTaskId()).get();
            candidateCustomTaskLocal.setCustomAnswer(saveCustomAnswerDTO.getAnswerContent());
            candidateCustomTaskRepository.save(candidateCustomTaskLocal);
        } catch (Exception e) {
            throw new Exception("Single answer could not be saved");
        }
    }

    public void saveCandidateCodeAnswer(SaveCodeAnswerDTOFromUI saveCodeAnswerDTOFromUI) throws Exception {
        try {
            Optional<CodeTask> codeTaskToSearchBy = codeTaskRepository.findById(saveCodeAnswerDTOFromUI.getId());
            if (codeTaskToSearchBy.isPresent()) {
                Optional<CandidateCodeTask> fromRepo = Optional.of(candidateCodeTaskRepository.findByCodeTaskAndCandidate(codeTaskToSearchBy.get(), testTokenService.getCandidateByToken(saveCodeAnswerDTOFromUI.getToken())));
                CandidateCodeTask toBeUpdated = fromRepo.get();
                toBeUpdated.setCodeProvided(saveCodeAnswerDTOFromUI.getCode());
                candidateCodeTaskRepository.save(toBeUpdated);
            }
        } catch (Exception e) {
            log.warn("Code task could not be updated. ", e);
            throw new Exception("Task could not be updated");
        }
    }

    //region PRIVATE METHODS

    /**
     * Method to check if there alreay exist a Candidat with the same email, stream and internship
     *
     * @param email
     * @param internship
     * @param stream
     * @return
     */
    private boolean candidateExists(String email, Internship internship, Stream stream) {
        boolean result = true;
        List<Candidate> inDb = candidateRepository.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .collect(Collectors.toList());

        if (inDb.isEmpty()) {
            return false;
        }

        for (Candidate candidate : inDb) {
            //compare Stream and Internship
            if (candidate.getStream().getId().equals(stream.getId()) && candidate.getInternship().getId().equals(internship.getId())) {
                result = true;
                break;
            } else {
                result = false;
            }
        }
        return result;
    }
    //endregion
}

