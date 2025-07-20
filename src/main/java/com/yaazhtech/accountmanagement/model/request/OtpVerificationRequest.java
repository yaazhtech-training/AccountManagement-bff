package com.yaazhtech.accountmanagement.model.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class OtpVerificationRequest {

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "OTP cannot be empty")
    @Size(min = 6, max = 6, message = "OTP must be 6 digits") // Assuming a 6-digit OTP
    private String otp;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}