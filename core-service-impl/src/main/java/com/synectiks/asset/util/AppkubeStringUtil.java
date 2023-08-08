package com.synectiks.asset.util;

public class AppkubeStringUtil {

    public static String  removeLeadingAndTrailingDoubleQuotes(String input){
        String result = input.replaceAll("^\"+|\"+$", "");
        return result;
    }
}
