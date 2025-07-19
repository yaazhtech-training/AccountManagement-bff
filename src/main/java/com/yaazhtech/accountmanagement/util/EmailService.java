package com.yaazhtech.accountmanagement.util;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailService {

    public void sendOtp(String email) {
        String otp = generateOtp();
        OTPStore.storeOtp(email, otp);

        // Send email using any real mail sender
        System.out.println("Sending OTP to " + email + ": " + otp);
        // You can integrate with JavaMailSender here
    }

    private String generateOtp() {
        int otp = 100000 + new Random().nextInt(900000);
        return String.valueOf(otp);
    }
}
