package com.internship.evaluation.utils;

public class StringUtils {

    public static void replaceString(StringBuilder source, String template, String str) {
        int index = source.indexOf(template);
        while (-1 != index) {
            source.replace(index, index + template.length(), str);
            index = source.indexOf(template);
        }
    }

    private StringUtils(){}
}
