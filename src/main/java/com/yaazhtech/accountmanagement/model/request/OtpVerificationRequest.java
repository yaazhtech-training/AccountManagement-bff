package com.yaazhtech.accountmanagement.model.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
@Data
public class OtpVerificationRequest {

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "OTP cannot be empty")
    @Size(min = 6, max = 6, message = "OTP must be 6 digits") // Assuming a 6-digit OTP
    private String otp;



}