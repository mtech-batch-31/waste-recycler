package com.mtech.recycler.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {

    public static boolean isMatchedPassword(String inputRawPassword, String encodedPasswordFromDatabase) {
        return encoder().matches(inputRawPassword, encodedPasswordFromDatabase);
    }

    public static String encodePassword(String inputRawPassword) {
        return encoder().encode(inputRawPassword);
    }

    private static PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean isValidEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.matches();
    }

    private static final String VALID_PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";

//    private static final Pattern VALID_PASSWORD_REGEX =
//            Pattern.compile("(?=.*[0-9])");

    public static boolean isValidPassword(String password) {
//        Matcher matcher = VALID_PASSWORD_REGEX.matcher(password);
        return password.matches(VALID_PASSWORD_REGEX);
//        return matcher.matches();
    }


}
