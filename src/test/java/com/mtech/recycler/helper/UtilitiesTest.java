package com.mtech.recycler.helper;

import com.mtech.recycler.model.RegisterRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
    void test_asJsonString() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@mail");
        Assertions.assertEquals(Utilities.asJsonString(registerRequest),"{\"email\":\"test@mail\",\"password\":null,\"firstName\":null,\"lastName\":null,\"contactNumber\":null,\"address\":null,\"postalCode\":null}");
    }
}
