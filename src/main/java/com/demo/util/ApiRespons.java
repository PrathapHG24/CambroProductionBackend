package com.demo.util;

public class ApiRespons {
    private final String message;
    private final String error;

    public ApiRespons(String message, String error) {
        this.message = message;
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }
}