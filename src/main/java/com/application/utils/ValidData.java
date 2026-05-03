package com.application.utils;

public class ValidData {
    private static final String regexEmail = "";
    private static final String regexPhoneNumber = "";

    public static boolean validEmail(String email) {
        return email.matches(regexEmail);
    }

    public static boolean validPhoneNumber(String phoneNumber) {
        return phoneNumber.matches(regexPhoneNumber);
    }
}