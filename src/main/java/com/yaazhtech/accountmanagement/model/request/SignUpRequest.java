package com.yaazhtech.accountmanagement.model.request;

import lombok.Data;




@Data
public class SignUpRequest {
    private String name;
    private String email;
    private String phoneNo;
    private String aadharNo;
    private String password;
    private String panNo;
}
