package com.application.utils;

public class ValidData {
    private static final String regexEmail = "^[\\w.+\\-]+@[\\w\\-]+(\\.[a-zA-Z]{2,})+$";
    private static final String regexPhoneNumber = "^(0[3-9]\\d{8})$";
    private static final String regexFirstName = "^[\\p{L} ]{1,20}$";
    private static final String regexLastName = "^[\\p{L} ]{1,50}$";
    private static final String regexUsername = "^[a-zA-Z0-9_]{3,100}$";
    private static final String regexPassword = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$!%*?&]).{8,}$";
    private static final String regexPosterPath = "^.+\\.(jpg|jpeg|png|webp)$";
    private static final String regexAgeRating = "^(P|C13|C16|C18)$";
    private static final String regexRoomName = "^[a-zA-Z0-9 \\-]{1,20}$";
    private static final String regexSeatName = "^[A-Z]\\d{1,2}$";
    private static final String regexMemberName = "^[\\p{L} ]{1,100}$";
    private static final String regexGenreName = "^[\\p{L}a-zA-Z\\- ]{1,100}$";

    public static boolean validEmail(String email) {
        return email.matches(regexEmail);
    }

    public static boolean validPhoneNumber(String phoneNumber) {
        return phoneNumber.matches(regexPhoneNumber);
    }

    public static boolean validFirstName(String firstName) {
        return firstName.matches(regexFirstName);
    }

    public static boolean validLastName(String lastName) {
        return lastName.matches(regexLastName);
    }

    public static boolean validUsername(String username) {
        return username.matches(regexUsername);
    }

    public static boolean validPassword(String password) {
        return password.matches(regexPassword);
    }

    public static boolean validPosterPath(String posterPath) {
        return posterPath.matches(regexPosterPath);
    }

    public static boolean validAgeRating(String ageRating) {
        return ageRating.matches(regexAgeRating);
    }

    public static boolean validRoomName(String roomName) {
        return roomName.matches(regexRoomName);
    }

    public static boolean validSeatName(String seatName) {
        return seatName.matches(regexSeatName);
    }

    public static boolean validMemberName(String memberName) {
        return memberName.matches(regexMemberName);
    }

    public static boolean validGenreName(String genreName) {
        return genreName.matches(regexGenreName);
    }
}
