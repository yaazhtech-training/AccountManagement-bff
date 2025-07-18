package com.yaazhtech.accountmanagement.controller;


import com.itextpdf.text.BadElementException;
import com.yaazhtech.accountmanagement.data.PupilAccount;
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
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.UUID;

@RestController
@RequestMapping("account/auth")
public class AuthController {
    //api endponit =>controller
    @Autowired
    private AuthService authService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @CrossOrigin
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public ResponseEntity<?> createSignup(@RequestBody SignUpRequest signUpRequest) throws MessagingException, IOException, BadElementException {
        PupilAccount pupilAccount = new PupilAccount();
        pupilAccount.setName(signUpRequest.getUserName());
        pupilAccount.setEmail(signUpRequest.getEmail());
        pupilAccount.setPhoneNo(signUpRequest.getPhone());
        pupilAccount.setCreatedAt(ZonedDateTime.now().toString());
        pupilAccount.setRole(String.valueOf(Role.USER));
        pupilAccount.setPupilEmail(signUpRequest.getEmail());
        pupilAccount.setId(UUID.randomUUID().toString());
        pupilAccount.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        authService.createUser(pupilAccount);
        return ResponseEntity.ok().body(new ApiResponse("Signup created successfully", pupilAccount));
    }
}
