package com.project.filesupload.model;

import java.security.SecureRandom;

public class RandomCodeGenerator {
    private static final SecureRandom random = new SecureRandom();
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 10;

    public static String generatedCode(){
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++){
            int rendomIndex = random.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(rendomIndex));
        }
        return code.toString();
    }
}
