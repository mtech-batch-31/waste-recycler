package com.mtech.recycler.constant;

public class CommonConstant {

    public static class JwtKey {
        public static final String ROLE = "ROLE";
    }

    public static class Message {
        public static final String SUCCESSFUL_REQUEST = "The request has been successfully processed";
    }

    public static class ErrorMessage {
        public static final String INVALID_REQUEST = "Invalid request";
        public static final String WRONG_USER_NAME_OR_PASSWORD = "Wrong user name or password";

        public static final String INVALID_PROMOTION_CODE = "Invalid promotion code";

        public static final String EXPIRED_PROMOTION_CODE = "Your promotion code is expired";
    }

    public static class ReturnCode {
        public static final String SUCCESS = "00";

        public static final String WRONG_USER_NAME_OR_PASSWORD = "01";
    }
}
