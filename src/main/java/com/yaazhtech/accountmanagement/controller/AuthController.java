package com.yaazhtech.accountmanagement.controller;

import com.yaazhtech.accountmanagement.data.PupilAccount;
import com.yaazhtech.accountmanagement.model.request.LoginRequest;
import com.yaazhtech.accountmanagement.model.request.OtpVerificationRequest;
import com.yaazhtech.accountmanagement.model.request.SignUpRequest;
import com.yaazhtech.accountmanagement.model.response.ApiResponse;
import com.yaazhtech.accountmanagement.model.response.TokenResponse;
import com.yaazhtech.accountmanagement.security.JwtTokenProvider;
import com.yaazhtech.accountmanagement.service.AccountService;
import com.yaazhtech.accountmanagement.service.EmailService;
import com.yaazhtech.accountmanagement.service.OtpService;
import com.yaazhtech.accountmanagement.util.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.ZonedDateTime;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("account/auth")
public class AuthController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;



    // ✅ SIGNUP - sends OTP
    @PostMapping("/signup")
    public ResponseEntity<?> createSignup(@RequestBody @Valid SignUpRequest signUpRequest) {
        System.out.println("📨 Signup request received for: " + signUpRequest.getEmail());

        if (accountService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ApiResponse("User already exists with this email", null));
        }


        // Create new user
        PupilAccount pupilAccount = new PupilAccount();
        pupilAccount.setId(UUID.randomUUID().toString());
        pupilAccount.setName(signUpRequest.getUserName());
        pupilAccount.setEmail(signUpRequest.getEmail());
        pupilAccount.setPhoneNo(signUpRequest.getPhoneNumber());
        pupilAccount.setPassword(signUpRequest.getPassword());  // Make sure you hash this!
        pupilAccount.setCreatedAt(ZonedDateTime.now().toString());
        pupilAccount.setRole(Role.USER.name());
        pupilAccount.setActive(false);

        // Generate and send OTP
        String otp = otpService.generateOTP(signUpRequest.getEmail());
        pupilAccount.setOtpData(otp);

        // Save the user before sending OTP
        accountService.savePupil(pupilAccount);

        // Send OTP to email
        emailService.sendOtpEmail(signUpRequest.getEmail(), otp);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse("✅ OTP sent to your email", null));
    }


    // ✅ OTP Verification
    @PostMapping("/validate-otp")
    public ResponseEntity<?> validateOtp(@Valid @RequestBody OtpVerificationRequest otpRequest) {
        System.out.println("🔐 Verifying OTP for: " + otpRequest.getEmail());

        boolean isValid = otpService.validateOTP(otpRequest.getEmail(), otpRequest.getOtp());

        if (!isValid) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new TokenResponse("❌ Invalid or expired OTP", null));
        }

        PupilAccount user = accountService.findByEmail(otpRequest.getEmail());

        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new TokenResponse("User not found", null));
        }

        user.setActive(true);
        user.setOtpData(null);  // clear OTP after successful verification
        accountService.savePupil(user);

        String token = jwtTokenProvider.generateToken(user.getEmail());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new TokenResponse("✅ OTP validated successfully", token));
    }

    // ✅ Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        boolean isAuthenticated = accountService.login(request.getEmail(), request.getPassword());

        if (!isAuthenticated) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse("Invalid credentials!", null));
        }

        PupilAccount user = accountService.findByEmail(request.getEmail());

        if (!user.isActive()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse("Account not verified. Please complete OTP verification.", null));
        }

        String token = jwtTokenProvider.generateToken(user.getEmail());

        return ResponseEntity.ok().body(new TokenResponse("✅ Login successful", token));
    }

    // ✅ Password Reset
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String email, @RequestParam String newPassword) {
        accountService.resetPassword(email, newPassword);
        return ResponseEntity.ok().body(new ApiResponse("🔁 Password reset successfully!", null));
    }
}
