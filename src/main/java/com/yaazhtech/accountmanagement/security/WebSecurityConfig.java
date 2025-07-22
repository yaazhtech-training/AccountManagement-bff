package com.yaazhtech.accountmanagement.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // ✅ Public/Unauthenticated endpoints
                .antMatchers(HttpMethod.POST, "/signup").permitAll()
                .antMatchers(
                        "/account/auth/**",
                        "/api/tnea/**",
                        "/", "/error", "/csrf",
                        "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**",
                        "/manage/**"
                ).permitAll()

                // ✅ Secure endpoints with role-based access

                .anyRequest().authenticated();

        // http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.cors().and().csrf().disable();
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Optional: Role constants (already imported above)
    public static final String ADMIN = "ADMIN";
    public static final String USER = "PUPIL";
    public static final String HR = "HR";
    public static final String EMPLOYEE = "EMPLOYEE";
    public static final String EMPLOYER = "EMPLOYER";
    public static final String CLIENT = "CLIENT";
    public static final String FINANCIER = "FINANCIER";
    public static final String MALLIGAMESS = "MALLIGAMESS";
    public static final String MUTHUMESS = "MUTHUMESS";
}

