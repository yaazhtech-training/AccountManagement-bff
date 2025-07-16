package com.yaazhtech.accountmanagement.model.response;


import com.yaazhtech.accountmanagement.data.PupilAccount;

public class ApiResponse {
    private String message;
    private PupilAccount data;

    public ApiResponse() {}

    public ApiResponse(String message, PupilAccount data) {
        this.message = message;
        this.data = data;
    }

    // Getters and Setters

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PupilAccount getData() {
        return data;
    }

    public void setData(PupilAccount data) {
        this.data = data;
    }
}

