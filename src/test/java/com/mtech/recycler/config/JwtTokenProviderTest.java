package com.mtech.recycler.config;

import com.mtech.recycler.dto.Role;
import io.jsonwebtoken.security.SignatureException;
import org.joda.time.DateTimeUtils;
import org.joda.time.Instant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

    class JwtTokenProviderTest {

    private String email;

    private String expectedToken;

    private String instantExpected;

    JwtTokenProvider tokenProvider;


    @BeforeEach
    void init() {
        email = "test@test.com";
        instantExpected = "2023-04-06T00:00:00Z";
        expectedToken = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwiaWF0IjoxNjgwNzM5MjAwLCJleHAiOjYxNjgwNzM5MTQwLCJST0xFIjoiQ1VTVE9NRVIifQ.KFxeNjCd24xQRa5872QHgfLvUZ8SCs1tqPdjoXGRMCeuuBJJCwwYgGNj9euun5tA";
        tokenProvider = new JwtTokenProvider("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", 999999999);
    }

    @AfterEach
    void cleanup() {
        DateTimeUtils.setCurrentMillisSystem();
    }

    @Test()
    void testJwtTokenProvider_GenerateValidToken() {
        // Arrange
        long setup = Instant.parse(instantExpected).getMillis();
        DateTimeUtils.setCurrentMillisFixed(setup);

        // Act
        String result = tokenProvider.generateToken(email, Role.CUSTOMER.toString());

        // Assert
        Assertions.assertEquals(expectedToken, result);
    }


    @Test()
    void testJwtTokenProvider_GetUserNameFromToken() {
        // Arrange
        String expectedEmail = "test@test.com";

        // Act
        String result = tokenProvider.getUserNameFromJWT(expectedToken);

        // Assert
        Assertions.assertEquals(expectedEmail, result);
    }

    @Test()
    void testJwtTokenProvider_GetRoleFromToken() {
        // Arrange
        String expectedRole = Role.CUSTOMER.toString();

        // Act
        String result = tokenProvider.getRoleFromJWT(expectedToken);

        // Assert
        Assertions.assertEquals(expectedRole, result);
    }

    @Test()
    void testJwtTokenProvider_ShouldThrowExceptionFromInvalidToken() {
        // Arrange
        String jwtToken = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwiaWF0IjoxNjgwNzM5MjAwLCJleHAiOjYxNjgwNzM5MTQwfQ.ZhWiPQ1h04wWZP_6ALVVGpW9U_Zj5DgLYvOn4sLoyPgjVr5tXNIxAipThXVCmy";

        // Act
        Assertions.assertThrows(SignatureException.class, () -> tokenProvider.getUserNameFromJWT(jwtToken));
    }

    @Test()
    void testJwtTokenProvider_ValidToken() {
        // Arrange

        // Act
        boolean result = tokenProvider.validateToken(expectedToken);

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
