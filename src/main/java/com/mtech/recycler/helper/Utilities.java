package com.mtech.recycler.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtech.recycler.entity.RecycleRequest;
import com.mtech.recycler.model.PricingRequest;
import com.mtech.recycler.model.SubmitRequest;
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

    public static String asJsonString(final Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }


    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean isValidEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.matches();
    }

    private static final Pattern VALID_PASSWORD_REGEX = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}");

    public static boolean isValidPassword(String password) {
        Matcher matcher = VALID_PASSWORD_REGEX.matcher(password);
        return matcher.matches();
    }

    private static final Pattern VALID_CONTACT_NUMBER_REGEX = Pattern.compile("^(\\+?65)?[689]\\d{7}$");

    public static boolean isValidContactNumber(String contactNumber) {
        Matcher matcher = VALID_CONTACT_NUMBER_REGEX.matcher(contactNumber);
        return matcher.matches();
    }

    private static final Pattern VALID_SG_POSTAL_CODE = Pattern.compile("^\\d{6}$");

    public static boolean isValidPostalCode(String postalCode) {
        Matcher matcher = VALID_SG_POSTAL_CODE.matcher(postalCode);
        return matcher.matches();
    }

    public static PricingRequest convertSubmitRequestToPricingRequest(SubmitRequest submitRequest) {
        PricingRequest pricingRequest = new PricingRequest();
        pricingRequest.setPromoCode(submitRequest.getPromoCode());
        pricingRequest.setData(submitRequest.getData());
        return pricingRequest;
    }
}
