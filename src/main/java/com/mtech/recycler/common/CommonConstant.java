package com.mtech.recycler.common;

public class CommonConstant {

    public static class Message {
        public static final String SUCCESSFUL_REQUEST = "The request has been successfully processed";
    }

    public static class ErrorMessage {
        public static final String INVAID_REQUEST = "Invalid request";
        public static final String WRONG_USER_NAME_OR_PASSWORD = "Wrong user name or password";

        public static final String REFRESH_TOKEN_EXPIRED = "The refresh token was expired. Please login again.";
    }

    public static class ReturnCode {
        public static final String SUCCESS = "00";

        public static final String WRONG_USER_NAME_OR_PASSWORD = "01";
    }
}
