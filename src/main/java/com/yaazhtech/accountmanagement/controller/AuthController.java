package com.yaazhtech.accountmanagement.controller;

import com.yaazhtech.accountmanagement.data.PupilAccount;
import com.yaazhtech.accountmanagement.model.request.LoginRequest;
import com.yaazhtech.accountmanagement.model.request.OtpVerificationRequest;
import com.yaazhtech.accountmanagement.model.request.SignUpRequest;
import com.yaazhtech.accountmanagement.model.response.ApiResponse;
import com.yaazhtech.accountmanagement.model.response.TokenResponse;
import com.yaazhtech.accountmanagement.security.JwtTokenProvider;
import com.yaazhtech.accountmanagement.service.AccountService;
import com.yaazhtech.accountmanagement.service.OtpService;
import com.yaazhtech.accountmanagement.service.EmailService;
import com.yaazhtech.accountmanagement.util.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.security.crypto.password.PasswordEncoder;
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


    @CrossOrigin
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public ResponseEntity<?> createSignup(@RequestBody @Valid SignUpRequest signUpRequest) throws MessagingException {
        System.out.println("✅ Signup request received: " + signUpRequest.getEmail());

        if (accountService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ApiResponse("User already exists with this email", null));
        }

        PupilAccount pupilAccount = new PupilAccount();
        pupilAccount.setName(signUpRequest.getUserName());
        pupilAccount.setEmail(signUpRequest.getEmail());
        pupilAccount.setPhoneNo(signUpRequest.getPhoneNumber());
        pupilAccount.setPassword(signUpRequest.getPassword());
        pupilAccount.setId(UUID.randomUUID().toString());
        pupilAccount.setCreatedAt(ZonedDateTime.now().toString());
        pupilAccount.setRole(String.valueOf(Role.USER));
        pupilAccount.setActive(false);



        String otp = otpService.generateOTP(signUpRequest.getEmail());
        pupilAccount.setOtpData(otp);
        emailService.sendOtpEmail(signUpRequest.getEmail(), otp);
        accountService.savePupil(pupilAccount);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse("✅ OTP sent to email", null));
    }

    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/validate-otp")
    public ResponseEntity<?> validateOtp(@Valid @RequestBody OtpVerificationRequest otpVerificationRequest) {
        boolean isValid = otpService.validateOTP(otpVerificationRequest.getEmail(), otpVerificationRequest.getOtp());

        if (!isValid) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new TokenResponse("Invalid or expired OTP", null));
        }

        PupilAccount user = accountService.findByEmail(otpVerificationRequest.getEmail());
        user.setActive(true);
        accountService.savePupil(user);

        String token = jwtTokenProvider.generateToken(user.getEmail());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new TokenResponse("OTP validated successfully", token));
    }


    // ✅ LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        boolean isAuthenticated = accountService.login(request.getEmail(), request.getPassword());
        if (isAuthenticated) {
            return ResponseEntity.ok().body(new ApiResponse("Login successful!", null));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("Invalid credentials!", null));
    }




    // ✅ RESET PASSWORD
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String email, @RequestParam String newPassword) {
        accountService.resetPassword(email, (newPassword));
        return ResponseEntity.ok().body(new ApiResponse("Password reset successfully!", null));
    }
}
