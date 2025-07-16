package com.yaazhtech.accountmanagement.runner;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Configuration
@Slf4j
public class DynamoDBEnvironment {

    @Autowired
    Environment env;

}
