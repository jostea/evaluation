package com.internship.evaluation.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@Slf4j
public class FileUtils {

    public static File obtainFile(String fileName) throws FileNotFoundException {
        try {
            return new ClassPathResource(fileName).getFile();
        } catch (IOException e) {
            log.error("File - " + fileName + " is null");
            throw new FileNotFoundException(fileName + " file was not found in classpath");
        }
    }

    public static StringBuilder readFromFile(File file) {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader buff = new BufferedReader(new FileReader(file))) {
            String line = buff.readLine();
            while (null != line) {
                sb.append(line).append("\n");
                line = buff.readLine();
            }
        } catch (IOException e) {
            log.error("Error happened while extracting data from the file - " + file, e);
        }

        return sb;
    }

    public static void writeToFile(File file, String str) {
        try(BufferedWriter buff = new BufferedWriter(new FileWriter(file))) {
            buff.write(str);
        } catch (IOException e) {
            log.error("Error happened while writing to file " + file.getAbsolutePath() + " " + file.getName() + " next string:\n" + str, e);
        }
    }

    private FileUtils(){}
}
