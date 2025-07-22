package com.yaazhtech.accountmanagement.service;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    private static final int EXPIRATION_MINUTES = 10;

    // Store email → OTP and its generation time
    private final Map<String, OtpData> otpStore = new ConcurrentHashMap<>();

    /**
     * Generate a 6-digit OTP for the given email and store it.
     */
    public String generateOTP(String email) {
        String otp = String.valueOf((int) (Math.random() * 900000) + 100000); // 100000 - 999999
        otpStore.put(email, new OtpData(otp, ZonedDateTime.now()));
        return otp;
    }

    /**
     * Validate the given OTP for the email.
     * Returns true if OTP matches and is within 10 minutes.
     */
    public boolean validateOTP(String email, String inputOtp) {
        OtpData data = otpStore.get(email);
        if (data == null) return false;

        Duration duration = Duration.between(data.getGeneratedTime(), ZonedDateTime.now());
        if (duration.toMinutes() > EXPIRATION_MINUTES) {
            otpStore.remove(email); // remove expired OTP
            return false;
        }

        if (data.getOtp().equals(inputOtp)) {
            otpStore.remove(email); // remove after successful use
            return true;
        }

        return false;
    }

    // Optional: clear OTP manually
    public void clearOTP(String email) {
        otpStore.remove(email);
    }

    // Inner class to hold OTP and timestamp
    private static class OtpData {
        private final String otp;
        private final ZonedDateTime generatedTime;

        public OtpData(String otp, ZonedDateTime generatedTime) {
            this.otp = otp;
            this.generatedTime = generatedTime;
        }

        public String getOtp() {
            return otp;
        }

        public ZonedDateTime getGeneratedTime() {
            return generatedTime;
        }
    }
}

