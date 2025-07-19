package com.yaazhtech.accountmanagement.controller;

import com.yaazhtech.accountmanagement.data.PupilAccount;
import com.yaazhtech.accountmanagement.model.request.ForgotPasswordRequest;
import com.yaazhtech.accountmanagement.model.request.LoginRequest;
import com.yaazhtech.accountmanagement.model.request.SignUpRequest;
import com.yaazhtech.accountmanagement.model.response.ApiResponse;
import com.yaazhtech.accountmanagement.service.AuthService;
import com.yaazhtech.accountmanagement.util.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.time.ZonedDateTime;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("account/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ SIGNUP with OTP SEND
    @PostMapping("/signup")
    public ResponseEntity<?> createSignup(@RequestBody @Valid SignUpRequest signUpRequest) throws MessagingException {
        PupilAccount pupilAccount = new PupilAccount();
        pupilAccount.setName(signUpRequest.getUserName());
        pupilAccount.setEmail(signUpRequest.getEmail());
        pupilAccount.setPhoneNo(signUpRequest.getPhone());
        pupilAccount.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        pupilAccount.setId(UUID.randomUUID().toString());
        pupilAccount.setCreatedAt(ZonedDateTime.now().toString());
        pupilAccount.setRole(String.valueOf(Role.USER));
        authService.createUser(pupilAccount);
        authService.sendOtp(signUpRequest.getEmail());
        return ResponseEntity.ok().body(new ApiResponse("Signup successful! OTP sent to your email.", pupilAccount));
    }

    // ✅ LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        boolean isAuthenticated = authService.login(request.getEmail(), request.getPassword());
        if (isAuthenticated) {
            return ResponseEntity.ok().body(new ApiResponse("Login successful!", null));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("Invalid credentials!", null));
    }

    // ✅ SEND OTP (FORGOT PASSWORD FLOW)
    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody ForgotPasswordRequest request) throws MessagingException {
        authService.sendOtp(request.getEmail());
        return ResponseEntity.ok().body(new ApiResponse("OTP sent successfully!", null));
    }

    // ✅ VERIFY OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        boolean isValid = authService.verifyOtp(email, otp);
        if (isValid) {
            return ResponseEntity.ok().body(new ApiResponse("OTP verified successfully!", null));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Invalid OTP!", null));
        }
    }

    // ✅ RESET PASSWORD
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String email, @RequestParam String newPassword) {
        authService.resetPassword(email, passwordEncoder.encode(newPassword));
        return ResponseEntity.ok().body(new ApiResponse("Password reset successfully!", null));
    }
}
