package com.internship.evaluation.codetask;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.internship.evaluation.utils.FileUtils.*;
import static com.internship.evaluation.utils.StringUtils.replaceString;

@Service
@Slf4j
public class ExecFileBuilder {

    @Value("${code.task.template.filename}")
    private String templateFileName;

    @Value("${code.task.template.classname.tag}")
    private String classNameTag;

    @Value("${code.task.template.methodtemplate.tag}")
    private String methodTemplateTag;

    @Value("${code.task.template.methodcall.tag}")
    private String methodCallTag;

    @Value("${code.task.temp.folder}")
    private String tempFolder;

    // need to be replaced with users method from DB
    public static String userMethodMock = "public static void sum(int a, int b) {\n        System.out.println(a+b);\n    }";

    public static String userMethodCallMock = "sum(2,3);";

    public File buildEvaluationFile(String taskName, String username) throws IOException {
        log.info("Building evaluation file for user - {} and task - {} ", username, taskName);

        StringBuilder sb = readFromFile(obtainFilePath(templateFileName));
        replaceString(sb, classNameTag, buildEvaluationClassName(taskName, username));
        replaceString(sb, methodTemplateTag, userMethodMock);
        replaceString(sb, methodCallTag, userMethodCallMock);

        File file = new File(tempFolder + buildEvaluationFileName(taskName, username));

        log.info("Writing data to evaluation file - {}", file);
        // need to figure out, do we create this file in temp folder, or send it directly somewhere???
        writeToFile(file, sb.toString());
        return file;
    }

    private String buildEvaluationFileName(String taskName, String username) {
        return buildEvaluationClassName(taskName, username) + ".java";
    }

    private String buildEvaluationClassName(String taskName, String username) {
        return taskName + '_' +
                username + '_' +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd_MM_yyyy_hh_mm_ss"));
    }
}