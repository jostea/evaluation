package com.internship.evaluation.service;

import com.internship.evaluation.model.dto.generate_test.*;
import com.internship.evaluation.model.entity.*;
import com.internship.evaluation.model.enums.TaskTypeEnum;
import com.internship.evaluation.model.enums.TechnologyEnum;
import com.internship.evaluation.model.enums.TestStatusEnum;
import com.internship.evaluation.model.enums.TypeEnum;
import com.internship.evaluation.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GenerateTestService {
    private final TestTokenRepository testTokenRepository;
    private final CandidateRepository candidateRepository;
    private final TaskRepository taskRepository;
    private final SqlTaskRepository sqlTaskRepository;
    private final CodeTaskRepository codeTaskRepository;
    private final CandidateMultiTaskRepository candidateMultiTaskRepository;
    private final CandidateSingleTaskRepository candidateSingleTaskRepository;
    private final CandidateCustomTaskRepository candidateCustomTaskRepository;
    private final CandidateSqlTaskRepository candidateSqlTaskRepository;
    private final CandidateCodeTaskRepository candidateCodeTaskRepository;
    private final CandidateService candidateService;

    public GenerateTestDTO generateTest(String token) throws Exception {
        try {
            GenerateTestDTO generateTestDTO = new GenerateTestDTO();
            List<GenerateTaskDTO> taskDTOList = new ArrayList<>();
            List<GenerateSqlTaskDTO> sqlTaskDTOList = new ArrayList<>();
            List<GenerateCodeTaskDTO> codeTaskDTOList = new ArrayList<>();
            List<Task> tasks;
            List<SqlTask> sqlTasks;
            List<CodeTask> codeTasks;
            List<Task> candidateTasks = new ArrayList<>();
            List<SqlTask> candidateSqlTasks = new ArrayList<>();
            List<CodeTask> candidateCodeTasks = new ArrayList<>();
            Candidate candidate = candidateRepository.findById(testTokenRepository.findFirstByToken(token).get().getCandidate().getId()).get();
            for (TestStructure ts : candidate.getStream().getTestStructures()) {
                tasks = taskRepository.findAllByTaskTypeAndComplexityAndIsEnabledAndStreams(TypeEnum.fromString(ts.getTaskType().name()), ts.getComplexity(), true, candidate.getStream());
                sqlTasks = sqlTaskRepository.findAllByComplexityAndIsEnabledIsTrueAndStreams(ts.getComplexity(), candidate.getStream());
                codeTasks = codeTaskRepository.findAllByComplexityAndIsEnabledIsTrueAndTechnologyAndStreams(ts.getComplexity(), TechnologyEnum.JAVA, candidate.getStream());
                if (ts.getTaskType().equals(TaskTypeEnum.MULTI_CHOICE) || ts.getTaskType().equals(TaskTypeEnum.SINGLE_CHOICE) || ts.getTaskType().equals(TaskTypeEnum.CUSTOM_QUESTION)) {
                    List<Integer> randomsForTask = getListOfRandoms(tasks.size(), ts.getNrQuestions().intValue());
                    for (Integer random : randomsForTask) {
                        Task randomTask = tasks.get(random);
                        candidateTasks.add(randomTask);
                        taskDTOList.add(new GenerateTaskDTO(randomTask));
                    }
                }
                if (ts.getTaskType().equals(TaskTypeEnum.SQL_QUESTION)) {
                    int max = ts.getNrQuestions().intValue();
                    List<Integer> randomsForSqlTask = getListOfRandoms(sqlTasks.size(), max);
                    for (Integer random : randomsForSqlTask) {
                        SqlTask sqlRandomTask = sqlTasks.get(random);
                        candidateSqlTasks.add(sqlRandomTask);
                        sqlTaskDTOList.add(new GenerateSqlTaskDTO(sqlRandomTask));
                    }
                }
                if (ts.getTaskType().equals(TaskTypeEnum.CODE_QUESTION)) {
                    int max = ts.getNrQuestions().intValue();
                    List<Integer> randomsForCodeTask = getListOfRandoms(codeTasks.size(), max);
                    for (Integer random : randomsForCodeTask) {
                        CodeTask codeRandomTask = codeTasks.get(random);
                        candidateCodeTasks.add(codeRandomTask);
                        codeTaskDTOList.add(new GenerateCodeTaskDTO(codeRandomTask));
                    }
                }
                if (ts.getTaskType().equals(TaskTypeEnum.SINGLE_CHOICE) || ts.getTaskType().equals(TaskTypeEnum.MULTI_CHOICE) || ts.getTaskType().equals(TaskTypeEnum.CUSTOM_QUESTION)) {
                    for (Task task : candidateTasks) {
                        if (task.getTaskType().equals(TypeEnum.MULTI_CHOICE) && ts.getTaskType().equals(TaskTypeEnum.fromString(task.getTaskType().name()))) {
                            CandidateMultiTask candidateMultiTask = new CandidateMultiTask(task, candidate);
                            candidateMultiTaskRepository.save(candidateMultiTask);
                        } else if (task.getTaskType().equals(TypeEnum.SINGLE_CHOICE) && ts.getTaskType().equals(TaskTypeEnum.fromString(task.getTaskType().name()))) {
                            CandidateSingleTask candidateSingleTask = new CandidateSingleTask(task, candidate);
                            candidateSingleTaskRepository.save(candidateSingleTask);
                        } else if (task.getTaskType().equals(TypeEnum.CUSTOM_QUESTION) && ts.getTaskType().equals(TaskTypeEnum.fromString(task.getTaskType().name()))) {
                            CandidateCustomTask candidateCustomTask = new CandidateCustomTask(task, candidate);
                            candidateCustomTaskRepository.save(candidateCustomTask);
                        }
                    }
                    candidateTasks.clear();
                }
                if (ts.getTaskType().equals(TaskTypeEnum.SQL_QUESTION)) {
                    for (SqlTask sqlTask : candidateSqlTasks) {
                        CandidateSqlTask candidateSqlTask = new CandidateSqlTask(sqlTask, candidate);
                        candidateSqlTaskRepository.save(candidateSqlTask);
                    }
                    candidateSqlTasks.clear();
                }
                if (ts.getTaskType().equals(TaskTypeEnum.CODE_QUESTION)) {
                    for (CodeTask codeTask : candidateCodeTasks) {
                        CandidateCodeTask candidateCodeTask = new CandidateCodeTask(codeTask, candidate);
                        candidateCodeTaskRepository.save(candidateCodeTask);
                    }
                    candidateCodeTasks.clear();
                }
            }
            generateTestDTO.setTasks(taskDTOList);
            generateTestDTO.setSqlTasks(sqlTaskDTOList);
            generateTestDTO.setCodeTasks(codeTaskDTOList);
            candidate.setDateTestStarted(Timestamp.valueOf(LocalDateTime.now()));
            candidate.setTestStatus(TestStatusEnum.TEST_STARTED);
            candidateService.updateCandidate(candidate);
            return generateTestDTO;
        } catch (Exception e) {
            throw new Exception("could not get tasks using current token");
        }
    }

    public GenerateTestDTO getExistingTest(Candidate candidate) {
        GenerateTestDTO existingTest = new GenerateTestDTO();
        List<CandidateMultiTask> existingMulti = candidateMultiTaskRepository.findAllByCandidateId(candidate.getId());
        List<GenerateTaskDTO> simpleTasks = new ArrayList<>();
        List<GenerateSqlTaskDTO> sqlTasks = new ArrayList<>();
        List<GenerateCodeTaskDTO> codeTasks = new ArrayList<>();

        if (!existingMulti.isEmpty()) {
            for (CandidateMultiTask candidateMultiTask : existingMulti) {
                GenerateTaskDTO generateTaskDTO = new GenerateTaskDTO(candidateMultiTask);
                simpleTasks.add(generateTaskDTO);
            }
        }

        if (!candidate.getCandidateSingleTasks().isEmpty()) {
            for (CandidateSingleTask candidateSingleTask : candidate.getCandidateSingleTasks()) {
                GenerateTaskDTO generateTaskDTO = new GenerateTaskDTO(candidateSingleTask);
                simpleTasks.add(generateTaskDTO);
            }
        }

        if (!candidate.getCandidateCustomTasks().isEmpty()) {
            for (CandidateCustomTask candidateCustomTask : candidate.getCandidateCustomTasks()) {
                GenerateTaskDTO generateTaskDTO = new GenerateTaskDTO(candidateCustomTask);
                simpleTasks.add(generateTaskDTO);
            }
        }

        if (!candidate.getCandidateSqlTasks().isEmpty()) {
            for (CandidateSqlTask candidateSqlTask : candidate.getCandidateSqlTasks()) {
                GenerateSqlTaskDTO generateSqlTaskDTO = new GenerateSqlTaskDTO(candidateSqlTask.getSqlTask());
                sqlTasks.add(generateSqlTaskDTO);
            }
            existingTest.setSqlTasks(sqlTasks);
        }

        if (!candidate.getCandidateCodeTasks().isEmpty()) {
            for (CandidateCodeTask candidateCodeTask : candidate.getCandidateCodeTasks()) {
                GenerateCodeTaskDTO generateCodeTaskDTO = new GenerateCodeTaskDTO(candidateCodeTask.getCodeTask());
                generateCodeTaskDTO.setCodeProvided(candidateCodeTask.getCodeProvided());
                codeTasks.add(generateCodeTaskDTO);
            }
            existingTest.setCodeTasks(codeTasks);
        }

        existingTest.setTasks(simpleTasks);
        return existingTest;
    }

    private static int getRandom(int max) {
        return (int) ((Math.random() * (max)));
    }

    private static List<Integer> getListOfRandoms(int max, int len) {
        List<Integer> randoms = new ArrayList<>();
        int i = 0;
        while (i < len) {
            int currentRandom = getRandom(max);
            if (!randoms.contains(currentRandom)) {
                randoms.add(currentRandom);
                i++;
            }
        }
        return randoms;
    }
}
