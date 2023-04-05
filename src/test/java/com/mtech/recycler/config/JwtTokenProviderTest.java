package com.mtech.recycler.config;

import io.jsonwebtoken.security.SignatureException;
import org.joda.time.DateTimeUtils;
import org.joda.time.Instant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SuppressWarnings("SpringBootApplicationProperties")
@SpringBootTest
@TestPropertySource(properties = {
        "app.jwtSecret=aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
        "app.jwtExpirationInMinutes=5"
})
public class JwtTokenProviderTest {

    private String email;

    private String instantExpected;


    private Instant instant;

    @Autowired
    JwtTokenProvider tokenProvider;


    @BeforeEach
    void init() {
        email = "test@test.com";
        instantExpected = "2023-04-06T00:00:00Z";
    }

    @AfterEach
    void cleanup() {
        DateTimeUtils.setCurrentMillisSystem();
    }

    @Test()
    void testJwtTokenProvider_GenerateValidToken() {
        // Arrange
        String expectedToken = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwiaWF0IjoxNjgwNzM5MjAwLCJleHAiOjE2ODA3Mzk1MDB9.rj98WMsyOofORav8-mwr8oXrm2S7IIUFrAL7PWL8c9rF1hAhzarxbLglDXhIr2bh";
        long setup = Instant.parse(instantExpected).getMillis();
        DateTimeUtils.setCurrentMillisFixed(setup);

        // Act
        String result = tokenProvider.generateToken(email);

        // Assert
        Assertions.assertEquals(expectedToken, result);
    }


    @Test()
    void testJwtTokenProvider_GetUserNameFromToken() {
        // Arrange
        //String jwtToken = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwiaWF0IjoxNjgwNzM5MjAwLCJleHAiOjE2ODA3Mzk1MDB9.rj98WMsyOofORav8-mwr8oXrm2S7IIUFrAL7PWL8c9rF1hAhzarxbLglDXhIr2bh";
        String jwtToken = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwiaWF0IjoxNjgwNzM5MjAwLCJleHAiOjE2ODA3Mzk1MDAsIlJvbGUiOiJVU0VSIiwiSVMtU1RBRkYiOnRydWV9.4_eEZiE_1koEGJd2-IjFVmDBvCSSx0Vuv25_o8AXINOgpBB9qnfeyUrTpV-ha5oa";
        String expectedEmail = "test@test.com";

        // Act
        String result = tokenProvider.getUserNameFromJWT(jwtToken);

        // Assert
        Assertions.assertEquals(expectedEmail, result);
    }

    @Test()
    void testJwtTokenProvider_ShouldThrowExceptionFromInvalidToken() {
        // Arrange
        String jwtToken = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwiaWF0IjoxNjgwNzM5MjAwLCJleHAiOjE2ODA3Mzk1MDB9.rj98WMsyOofORav8-mwr8oXrm2S7IIUFrAL7PWL8c9rF1hAhzarxbLglDXhIrcbh";

        // Act
        Assertions.assertThrows(SignatureException.class, () -> {
            tokenProvider.getUserNameFromJWT(jwtToken);
        });
    }

    @Test()
    void testJwtTokenProvider_ValidToken() {
        // Arrange
        String jwtToken = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwiaWF0IjoxNjgwNzM5MjAwLCJleHAiOjE2ODA3Mzk1MDB9.rj98WMsyOofORav8-mwr8oXrm2S7IIUFrAL7PWL8c9rF1hAhzarxbLglDXhIr2bh";

        // Act
        boolean result = tokenProvider.validateToken(jwtToken);

        // Assert
        Assertions.assertTrue(result);
    }

    @Test()
    void testJwtTokenProvider_InvalidToken() {
        // Arrange
        String jwtToken = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwiaWF0IjoxNjgwNzM5MjAwLCJleHAiOjE2ODA3Mzk1MDB9.rj98WMsyOofORav8-mwr8oXrm2S7IIUFrAL7PWL8c9rF1hAhzarxbLglDXhIrcbh";

        // Act
        boolean result = tokenProvider.validateToken(jwtToken);

        // Assert
        Assertions.assertFalse(result);
    }
}
