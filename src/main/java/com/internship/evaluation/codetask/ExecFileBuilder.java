package com.internship.evaluation.codetask;

import com.internship.evaluation.codetask.entity.ParameterNameAndType;
import com.internship.evaluation.model.dto.test.TestCaseDTO;
import com.internship.evaluation.model.dto.test.UserCodeTaskDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.internship.evaluation.utils.FileUtils.*;
import static com.internship.evaluation.utils.StringUtils.replaceString;

@Service
@Slf4j
public class ExecFileBuilder {

    private static final String NEW_ROW_SIGN = "\n";

    private static final String PARAMETER_PREFIX = "param";

    @Value("${code.task.template.filename}")
    private String templateFileName;

    @Value("${code.task.template.classname.tag}")
    private String classNameTag;

    @Value("${code.task.template.methodtemplate.tag}")
    private String methodTemplateTag;

    @Value("${code.task.template.methodcall.tag}")
    private String methodCallTag;

    @Value("${code.task.template.classtestcasename.tag}")
    private String classNameForTestCaseTag;

    @Value("${code.task.template.classtestcasename.value}")
    private String classNameForTestCaseValue;

    @Value("${code.task.template.parameters.tag}")
    private String parametersTag;

    @Value("${code.task.template.returntype.tag}")
    private String returnTypeTag;

    @Value("${code.task.template.constructor.tag}")
    private String constructorTag;

    @Value("${code.task.template.objectcreation.tag}")
    private String objectCreationTag;

    @Value("${code.task.temp.folder}")
    private String tempFolder;

    private String finalClassNameForTestCase;

    public File buildEvaluationFile(final UserCodeTaskDTO userCodeTaskDTO) throws IOException {
        log.info("Building evaluation file for user - {} and task - {} ", userCodeTaskDTO.getCandidateId(), userCodeTaskDTO.getQuestionId());

        this.finalClassNameForTestCase = buildTestCaseClassName(userCodeTaskDTO.getQuestionId(), userCodeTaskDTO.getCandidateId());

        String methodSignature = userCodeTaskDTO.getSignature();
        List<ParameterNameAndType> listOfParameters = getListOfParameters(methodSignature.substring(methodSignature.indexOf('(') + 1, methodSignature.indexOf(')')));
        String returnType = getReturnType(methodSignature);
        String methodName = getMethodName(methodSignature);

        StringBuilder sb = readFromFile(obtainFilePath(templateFileName));
        replaceString(sb, classNameTag, buildEvaluationClassName(userCodeTaskDTO.getQuestionId(), userCodeTaskDTO.getCandidateId()));
        replaceString(sb, methodTemplateTag, userCodeTaskDTO.getUserAnswer());
        replaceString(sb, classNameForTestCaseTag, finalClassNameForTestCase);
        replaceString(sb, objectCreationTag, buildObjects(userCodeTaskDTO, listOfParameters));
        replaceString(sb, methodCallTag, buildMethodCall(listOfParameters.size(), methodName));
        replaceString(sb, parametersTag, buildParameters(listOfParameters));
        replaceString(sb, returnTypeTag, returnType);
        replaceString(sb, constructorTag, buildConstructor(listOfParameters));

        File file = new File(tempFolder + buildEvaluationFileName(userCodeTaskDTO.getQuestionId(), userCodeTaskDTO.getCandidateId()));

        log.info("Writing data to evaluation file - {}", file);
        // need to figure out, do we create this file in temp folder, or send it directly somewhere???
        writeToFile(file, sb.toString());
        return file;
    }

    private String buildTestCaseClassName(final String questionId, final String candidateId) {
        return buildEvaluationClassName(questionId, candidateId) + '_' + classNameForTestCaseValue;
    }

    private String buildEvaluationFileName(final String questionId, final String candidateId) {
        return buildEvaluationClassName(questionId, candidateId) + ".java";
    }

    private String buildEvaluationClassName(final String questionId, final String candidateId) {
        return '_' + questionId + '_' +
                candidateId + '_' +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd_MM_yyyy_hh_mm_ss"));
    }

    private List<ParameterNameAndType> getListOfParameters(final String paramsFromSignature) {
        List<ParameterNameAndType> listOfParameters = new ArrayList<>();
        StringBuilder param = new StringBuilder();
        int numberOfGuillemets = 0;
        for (int i = 0; i < paramsFromSignature.length(); i++) {
            char currentChar = paramsFromSignature.charAt(i);
            if (currentChar == ',' && numberOfGuillemets == 0) {
                listOfParameters.add(getParameterNameAndType(param.toString()));
                param.setLength(0);
                continue;
            }
            param.append(currentChar);
            if (currentChar == '<') {
                numberOfGuillemets++;
            } else if (currentChar == '>') {
                numberOfGuillemets--;
            }
        }
        listOfParameters.add(getParameterNameAndType(param.toString()));
        return listOfParameters;
    }

    private ParameterNameAndType getParameterNameAndType(final String parameter) {
        int lastSpace = parameter.lastIndexOf(' ');
        return new ParameterNameAndType(parameter.substring(lastSpace + 1).trim(), parameter.substring(0, lastSpace).trim());
    }

    private String getReturnType(final String methodSignature) {
        String signature = methodSignature.substring(0, methodSignature.indexOf('('));
        signature = signature.replace("public", "").replace("static", "").trim();
        return signature.substring(0, signature.lastIndexOf(' ')).trim();
    }

    private String getMethodName(final String methodSignature) {
        String signature = methodSignature.substring(0, methodSignature.indexOf('('));
        signature = signature.replace("public", "").replace("static", "").trim();
        return signature.trim().substring(signature.lastIndexOf(' ')).trim();
    }

    private String buildParameters(final List<ParameterNameAndType> listOfParameters) {
        int parameterNumber = 1;
        StringBuilder stringBuilder = new StringBuilder();
        for (ParameterNameAndType listOfParameter : listOfParameters) {
            stringBuilder.append(listOfParameter.getParameterType());
            stringBuilder.append(' ');
            stringBuilder.append(PARAMETER_PREFIX);
            stringBuilder.append(parameterNumber++);
            stringBuilder.append(';');
            stringBuilder.append(NEW_ROW_SIGN);
        }
        return stringBuilder.toString();
    }

    private String buildConstructor(final List<ParameterNameAndType> listOfParameters) {
        AtomicInteger atomicInteger = new AtomicInteger(1);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(finalClassNameForTestCase);
        stringBuilder.append("(int questionId, int caseId, ");
        stringBuilder.append(listOfParameters.stream()
                .map(parameter -> parameter.getParameterType() + " " + PARAMETER_PREFIX + atomicInteger.getAndIncrement())
                .collect(Collectors.joining(", ")));
        stringBuilder.append(") {\n");
        stringBuilder.append("\t\tthis.questionId = questionId;\n");
        stringBuilder.append("\t\tthis.caseId = caseId;\n");
        for (int i = 1; i <= listOfParameters.size(); i++) {
            String parameterName = PARAMETER_PREFIX + i;
            stringBuilder.append("\t\tthis.");
            stringBuilder.append(parameterName);
            stringBuilder.append(" = ");
            stringBuilder.append(parameterName);
            stringBuilder.append(";");
            stringBuilder.append(NEW_ROW_SIGN);
        }
        stringBuilder.append("\t}");
        return stringBuilder.toString();
    }

    private String buildObjects(final UserCodeTaskDTO userCodeTask, final List<ParameterNameAndType> listOfParameters) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("List<").append(finalClassNameForTestCase).append("> parameters = new ArrayList<>();\n");
        for (TestCaseDTO testCase : userCodeTask.getTestCases()) {
            String[] arguments = testCase.getInput().split(";");
            stringBuilder.append("\t\tparameters.add(");
            stringBuilder.append(createObject(arguments, listOfParameters, userCodeTask.getQuestionId(), testCase.getCaseId()));
            stringBuilder.append(");\n");
        }
        return stringBuilder.toString();
    }

    private String createObject(final String[] arguments, final List<ParameterNameAndType> listOfParameters, final String questionId, final String caseId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("new ").append(finalClassNameForTestCase).append("(");
        stringBuilder.append(questionId).append(", ");
        stringBuilder.append(caseId);
        for (int i = 0; i < arguments.length; i++) {
            String type = listOfParameters.get(i).getParameterType();
            stringBuilder.append(", ");
            if (!type.contains("List") && !type.contains("Set")) {
                stringBuilder.append(String.format(getParameter(type), arguments[i].trim()));
            } else {
                stringBuilder.append(createListOrSet(type, arguments[i].trim()));
            }
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    private String getParameter(final String type) {
        switch (type) {
            case "byte":
                return "(byte) %s";
            case "short":
                return "(short) %s";
            case "int":
            case "double":
            case "boolean":
            case "Boolean":
                return "%s";
            case "long":
                return "%sL";
            case "float":
                return "%sf";
            case "char":
            case "Character":
                return "'%s'";
            case "Byte":
                return "Byte.valueOf(%s)";
            case "Short":
                return "Short.valueOf(%s)";
            case "Integer":
                return "Integer.valueOf(%s)";
            case "Long":
                return "Long.valueOf(%s)";
            case "Float":
                return "Float.valueOf(%s)";
            case "Double":
                return "Double.valueOf(%s)";
            case "String":
                return "\"%s\"";
            default:
                throw new IllegalArgumentException();
        }
    }

    private String createListOrSet(final String type, final String value) {
        StringBuilder stringBuilder = new StringBuilder();
        String instanceType = type.substring(0, type.indexOf('<'));
        String subType = type.substring(type.indexOf('<') + 1, type.lastIndexOf('>'));
        if (instanceType.equals("List")) {
            instanceType = "ArrayList";
        } else if (instanceType.equals("Set")) {
            instanceType = "HashSet";
        } else {
            instanceType = type;
        }
        stringBuilder.append("new ").append(instanceType).append("<>(Arrays.asList(");
        List<String> arrayValues = parseArray(value);
        for (int i = 0; i < arrayValues.size(); i++) {
            if (!subType.contains("List") && !subType.contains("Set")) {
                stringBuilder.append(String.format(getParameter(subType), arrayValues.get(i).trim()));
            } else {
                stringBuilder.append(createListOrSet(subType, arrayValues.get(i)));
            }
            if (i < arrayValues.size() - 1) {
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append("))");
        return stringBuilder.toString();
    }


    private List<String> parseArray(final String value) {
        List<String> values = new ArrayList<>();
        String valueInArray = value.trim();
        valueInArray = valueInArray.substring(1, valueInArray.length() - 1).trim();
        StringBuilder stringBuilder = new StringBuilder();
        int numberOfBrackets = 0;
        for (int i = 0; i < valueInArray.length(); i++) {
            char currentChar = valueInArray.charAt(i);
            if (currentChar == ',' && numberOfBrackets == 0) {
                values.add(stringBuilder.toString());
                stringBuilder.setLength(0);
                continue;
            }
            stringBuilder.append(currentChar);
            if (currentChar == '[') {
                numberOfBrackets++;
            } else if (currentChar == ']') {
                numberOfBrackets--;
            }
        }
        values.add(stringBuilder.toString());
        return values;
    }

    private String buildMethodCall(final int numberOfParameters, final String methodName) {
        final String nameOfIterable = "testCase";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("for(").append(finalClassNameForTestCase).append(" ").append(nameOfIterable).append(": parameters) {\n");
        stringBuilder.append("\t\t\ttry {\n");
        stringBuilder.append("\t\t\t").append(nameOfIterable).append(".result = ").append(methodName).append("(");
        for (int i = 1; i <= numberOfParameters; i++) {
            stringBuilder.append(nameOfIterable).append(".").append(PARAMETER_PREFIX).append(i);
            if (i != numberOfParameters) {
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append(");\n");
        stringBuilder.append("\t\t\tSystem.out.println(").append(nameOfIterable).append(");");
        stringBuilder.append("\n\t\t\t} catch (Throwable e) {} ");
        stringBuilder.append("\n\t\t}\n");
        return stringBuilder.toString();
    }
}