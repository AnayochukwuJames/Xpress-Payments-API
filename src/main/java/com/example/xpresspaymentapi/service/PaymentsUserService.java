package com.example.xpresspaymentapi.service;


import com.example.xpresspaymentapi.model.dto.base.BaseResponse;
import com.example.xpresspaymentapi.model.dto.user.LoginRequest;
import com.example.xpresspaymentapi.model.dto.user.SignUpRequest;

public interface PaymentsUserService {
    BaseResponse<?> handleUserRegistration(SignUpRequest signUpRequest);

    BaseResponse<?> handleUserLogin(LoginRequest loginRequest);
}
