package com.yaazhtech.accountmanagement.service;



import com.yaazhtech.accountmanagement.config.MailConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MailConfig mailConfig;

    private static final String FROM_EMAIL = "rajan@yaazhtech.com";
    private static final String SUBJECT = "🔐 Your One-Time Password (OTP)";
    private static final String EXPIRY_NOTE = "⚠️ Please do not share this OTP with anyone.\n\nThis OTP will expire in 10 minutes.";

    public void sendOtpEmail(String toEmail, String otp) {
        if (!StringUtils.hasText(toEmail) || !StringUtils.hasText(otp)) {
            throw new IllegalArgumentException("Email or OTP must not be empty.");
        }

        String emailBody = buildOtpMessageBody(otp);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailConfig.getUsername());
        message.setTo(toEmail);
        message.setSubject(SUBJECT);
        message.setText(emailBody);
        javaMailSender.send(message);
    }

    private String buildOtpMessageBody(String otp) {
        return new StringBuilder()
                .append("Hello,\n\n")
                .append("✅ Your One-Time Password (OTP) is: ")
                .append(otp)
                .append("\n\n")
                .append(EXPIRY_NOTE)
                .append("\n\n")
                .append("Regards,\nYaazhtech Team")
                .toString();
    }
}
