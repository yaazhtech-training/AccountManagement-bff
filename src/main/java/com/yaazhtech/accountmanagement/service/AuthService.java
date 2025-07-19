package com.yaazhtech.accountmanagement.service;

import com.yaazhtech.accountmanagement.data.PupilAccount;
import com.yaazhtech.accountmanagement.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final ConcurrentHashMap<String, String> otpStorage = new ConcurrentHashMap<>();

    public PupilAccount createUser(PupilAccount pupilAccount) {
        return userRepository.save(pupilAccount);
    }

    public boolean login(String email, String password) {
        Optional<PupilAccount> optionalUser = userRepository.findByEmail(email);
        return optionalUser.isPresent() && passwordEncoder.matches(password, optionalUser.get().getPassword());
    }

    public void sendOtp(String email) throws MessagingException {
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStorage.put(email, otp);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setSubject("OTP Verification");
        helper.setText("Your OTP is: " + otp, true);

        javaMailSender.send(message);
    }

    public boolean verifyOtp(String email, String otp) {
        return otp.equals(otpStorage.get(email));
    }

    public void resetPassword(String email, String newPassword) {
        Optional<PupilAccount> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            PupilAccount account = user.get();
            account.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(account);
        }
    }
}
