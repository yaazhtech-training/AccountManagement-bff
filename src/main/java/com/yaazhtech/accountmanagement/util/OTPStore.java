package com.yaazhtech.accountmanagement.util;

import java.util.HashMap;
import java.util.Map;

public class OTPStore {
    private static final Map<String, String> otpMap = new HashMap<>();

    public static void storeOtp(String email, String otp) {
        otpMap.put(email, otp);
    }

    public static boolean verifyOtp(String email, String otp) {
        return otp.equals(otpMap.get(email));
    }
}
