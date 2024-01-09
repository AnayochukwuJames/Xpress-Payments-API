package com.example.xpresspaymentapi.service;


import com.example.xpresspaymentapi.model.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface PaymentsUserDetailsService extends UserDetailsService {
    User resolveUserByEmailAddress(String emailAddress);
    User resolveUserById(Long userId);
}
