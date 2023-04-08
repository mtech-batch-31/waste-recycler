package com.mtech.recycler.helper;

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
    @ValueSource(strings = {"password", "A1@a1@a","12345"})
    void givenInvalidPassword_isValidPasswordFalse(String password) {
        Assertions.assertFalse(Utilities.isValidPassword(password));
    }



    @Test
    void test_asJsonString() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@mail");
        Assertions.assertEquals(Utilities.asJsonString(registerRequest),"{\"email\":\"test@mail\",\"password\":null,\"firstName\":null,\"lastName\":null,\"contactNumber\":null,\"address\":null,\"postalCode\":null}");
    }
}
