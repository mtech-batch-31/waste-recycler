package com.mtech.recycler.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mtech.recycler.model.RegisterRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class UtilitiesTest {


    @Test
    void givenValidEmail_isValidEmailTrue() {
        Assertions.assertTrue(Utilities.isValidEmail("test@mail.com"));
    }

    @Test
    void givenValidEmail_isValidEmailFalse() {
        Assertions.assertFalse(Utilities.isValidEmail("test@mail"));
    }


    @Test
    void givenValidPassword_isValidPasswordTrue() {
        Assertions.assertTrue(Utilities.isValidPassword("P@ssw0rd"));
    }


    @ParameterizedTest
    @ValueSource(strings = {"password", "A1@a1@a", "12345"})
    void givenInvalidPassword_isValidPasswordFalse(String password) {
        Assertions.assertFalse(Utilities.isValidPassword(password));
    }

    @ParameterizedTest
    @ValueSource(strings = {"+6561234567", "6561234567", "+6599876543", "6599876543", "+6580123456", "6580123456", "60123456", "80123456", "90123456"})
    void givenValidContact_isValidContactNumberTrue(String contactNumber) {
        Assertions.assertTrue(Utilities.isValidContactNumber(contactNumber));
    }

    @ParameterizedTest
    @ValueSource(strings = {"12345", "+6512345678", "6512345678", "+6591234567890", "6591234567890", "91234567890"})
    void givenInvalidContact_isValidContactNumberFalse(String contactNumber) {
        Assertions.assertFalse(Utilities.isValidContactNumber(contactNumber));
    }

    @ParameterizedTest
    @ValueSource(strings = {"123456"})
    void givenValidPostalCode_isValidPostalCodeTrue(String postalCode) {
        Assertions.assertTrue(Utilities.isValidPostalCode(postalCode));
    }

    @ParameterizedTest
    @ValueSource(strings = {"12345", "1234567", "A12345"})
    void givenInvalidPostalCode_isValidPostalCodeFalse(String postalCode) {
        Assertions.assertFalse(Utilities.isValidPostalCode(postalCode));
    }

    @Test
    void test_asJsonString() throws JsonProcessingException {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@mail");
        Assertions.assertEquals(Utilities.asJsonString(registerRequest), "{\"email\":\"test@mail\",\"password\":null,\"firstName\":null,\"lastName\":null,\"contactNumber\":null,\"address\":null,\"postalCode\":null}");
    }
}
