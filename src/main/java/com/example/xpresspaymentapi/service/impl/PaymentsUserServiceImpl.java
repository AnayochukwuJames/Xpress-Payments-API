package com.example.xpresspaymentapi.service.impl;

import com.example.xpresspaymentapi.configuration.exception.InvalidCredentialsException;
import com.example.xpresspaymentapi.configuration.mapper.UserMapper;
import com.example.xpresspaymentapi.configuration.security.JwtService;
import com.example.xpresspaymentapi.configuration.utils.PasswordUtils;
import com.example.xpresspaymentapi.model.dto.base.BaseResponse;
import com.example.xpresspaymentapi.model.dto.user.AuthenticationToken;
import com.example.xpresspaymentapi.model.dto.user.LoginRequest;
import com.example.xpresspaymentapi.model.dto.user.SignUpRequest;
import com.example.xpresspaymentapi.model.entity.User;
import com.example.xpresspaymentapi.model.repository.UserRepository;
import com.example.xpresspaymentapi.service.PaymentsUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentsUserServiceImpl implements PaymentsUserService {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final PasswordUtils passwordUtils;

    private final JwtService jwtService;

    @Override
    public BaseResponse<?> handleUserRegistration(final SignUpRequest signUpRequest) {
        BaseResponse<?> br;
        try {
            if (userExistsByEmailAddress(signUpRequest.getEmailAddress())) {
                br = new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), "user already exists", null);
            } else {
                User user = UserMapper.mapSignupRequestToUser(signUpRequest);
                user.setPassword(passwordUtils.hashedPassword(signUpRequest.getPassword()));
                userRepository.save(user);
                br = new BaseResponse<>(HttpStatus.OK.value(), "user registered successfully", null);
            }
        } catch (Exception e) {
            br = new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), "user registration failed", e.getMessage());
        }
        return br;
    }

    @Override
    public BaseResponse<?> handleUserLogin(final LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmailAddress(), loginRequest.getPassword()));
        } catch (Exception e) {
            throw new InvalidCredentialsException("wrong username or password");
        }
        User foundUser = userRepository.findUserByContact_EmailAddress(loginRequest.getEmailAddress()).get();
        auditLoginActivity(foundUser);

        AuthenticationToken authenticationToken = jwtService.generateToken(foundUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                foundUser.getContact().getEmailAddress(),foundUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new BaseResponse<>(HttpStatus.OK.value(), "user login successful", authenticationToken);
    }

    private boolean userExistsByEmailAddress(String emailAddress) {
        return userRepository.existsByContact_EmailAddress(emailAddress);
    }

    private void auditLoginActivity(User foundUser) {
        if (ObjectUtils.isEmpty(foundUser.getFirstLoginDate())) {
            foundUser.setFirstLoginDate(LocalDateTime.now());
        } else {
            foundUser.setLastLoginDate(LocalDateTime.now());
        }
        userRepository.save(foundUser);
    }
}
