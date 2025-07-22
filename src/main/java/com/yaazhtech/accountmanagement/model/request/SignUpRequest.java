package com.yaazhtech.accountmanagement.model.request;

import lombok.Data;




@Data
public class SignUpRequest {
    private String userName;
    private String email;
    private String phoneNumber;
    private String password;
}
