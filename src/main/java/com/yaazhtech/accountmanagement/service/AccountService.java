package com.yaazhtech.accountmanagement.service;

import com.yaazhtech.accountmanagement.data.PupilAccount;

public interface AccountService {

        void savePupil(PupilAccount user);
        boolean existsByEmail(String email);

        PupilAccount findByEmail(String email);
        boolean login(String email, String password);
    void resetPassword(String email, String newPassword);
}
