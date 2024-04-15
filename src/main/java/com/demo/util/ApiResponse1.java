package com.demo.util;

import java.util.List;
import java.util.Map;

public class ApiResponse1 {
    private String status;
    private String message;
    private List<Map<String, Object>> data;

    public ApiResponse1() {
    }

    public ApiResponse1(String status, String message, List<Map<String, Object>> data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static ApiResponse1 success(String message, List<Map<String, Object>> data) {
        ApiResponse1 response = new ApiResponse1();
        response.setStatus("success");
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    public static ApiResponse1 error(String message) {
        ApiResponse1 response = new ApiResponse1();
        response.setStatus("error");
        response.setMessage(message);
        return response;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }
}
