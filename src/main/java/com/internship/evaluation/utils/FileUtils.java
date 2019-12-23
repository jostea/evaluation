package com.internship.evaluation.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@Slf4j
public class FileUtils {

    public static String obtainFilePath(String fileName) throws FileNotFoundException {
        ClassLoader classLoader = FileUtils.class.getClassLoader();
        try {
            return classLoader.getResource(fileName).getPath();
        } catch (NullPointerException e) {
            log.error("Path for file - " + fileName + " is null");
            throw new FileNotFoundException(fileName + " file was not found in classpath");
        }
    }

    public static StringBuilder readFromFile(String filePath) {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader buff = new BufferedReader(new FileReader(filePath))) {
            String line = buff.readLine();
            while (null != line) {
                sb.append(line).append("\n");
                line = buff.readLine();
            }
        } catch (IOException e) {
            log.error("Error happened while extracting data from the file path - " + filePath, e);
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
