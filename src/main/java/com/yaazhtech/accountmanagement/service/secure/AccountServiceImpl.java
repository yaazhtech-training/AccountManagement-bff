package com.yaazhtech.accountmanagement.service.secure;

import com.yaazhtech.accountmanagement.data.PupilAccount;
import com.yaazhtech.accountmanagement.repository.AccountRepository;
import com.yaazhtech.accountmanagement.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository pupilUserRepository;


    @Override
    public void savePupil(PupilAccount user) {
        pupilUserRepository.save(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return pupilUserRepository.existsByEmail(email);
    }

    @Override
    public PupilAccount findByEmail(String email) {
        return pupilUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }
    @Override
    public boolean login(String email, String password) {
        Optional<PupilAccount> optionalUser = pupilUserRepository.findByEmail(email);
        return optionalUser.isPresent() && password.equals(optionalUser.get().getPassword());
    }


    @Override
    public void resetPassword(String email, String newPassword) {
        Optional<PupilAccount> user = pupilUserRepository.findByEmail(email);
        if (user.isPresent()) {
            PupilAccount account = user.get();
            account.setPassword((newPassword));
            pupilUserRepository.save(account);
        }
    }

}

