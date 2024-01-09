package com.example.xpresspaymentapi.controller;

import com.example.xpresspaymentapi.model.dto.base.BaseResponse;
import com.example.xpresspaymentapi.model.dto.user.LoginRequest;
import com.example.xpresspaymentapi.model.dto.user.SignUpRequest;
import com.example.xpresspaymentapi.service.PaymentsUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType
        .APPLICATION_JSON_VALUE)
public class PaymentsUserController {

    private final PaymentsUserService paymentsUserService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody @Valid SignUpRequest signUpRequest) {
        BaseResponse<?> br;
        br = paymentsUserService.handleUserRegistration(signUpRequest);
        if (br.getResponseCode() == HttpStatus.OK.value()) {
            return ResponseEntity.ok().body(br);
        } else {
            return ResponseEntity.badRequest().body(br);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody @Valid LoginRequest loginRequest) {
        BaseResponse<?> br = paymentsUserService.handleUserLogin(loginRequest);
        if (br.getResponseCode() == HttpStatus.OK.value()) {
            return ResponseEntity.ok().body(br);
        } else {
            return ResponseEntity.badRequest().body(br);
        }
    }
}
