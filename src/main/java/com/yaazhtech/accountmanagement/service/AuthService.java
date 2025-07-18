package com.yaazhtech.accountmanagement.service;

import com.yaazhtech.accountmanagement.data.PupilAccount;
import com.yaazhtech.accountmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;



    public PupilAccount createUser(PupilAccount pupilAccount) {
         return userRepository.save(pupilAccount);
    }
}
