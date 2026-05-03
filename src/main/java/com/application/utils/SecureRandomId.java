package com.application.utils;

import java.security.SecureRandom;

public class SecureRandomId {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String randomString() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        int charactersLength = CHARACTERS.length();
        for (int i = 0; i < charactersLength; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(charactersLength)));
        }

        return sb.toString();
    }
}
