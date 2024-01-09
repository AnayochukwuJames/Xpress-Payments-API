package com.example.xpresspaymentapi.service.impl;

import com.example.xpresspaymentapi.configuration.security.JwtAuthenticationFilter;
import com.example.xpresspaymentapi.configuration.security.JwtService;
import com.example.xpresspaymentapi.configuration.utils.PasswordUtils;
import com.example.xpresspaymentapi.model.dto.base.BaseResponse;
import com.example.xpresspaymentapi.model.dto.user.LoginRequest;
import com.example.xpresspaymentapi.model.dto.user.SignUpRequest;
import com.example.xpresspaymentapi.model.entity.Contact;
import com.example.xpresspaymentapi.model.entity.User;
import com.example.xpresspaymentapi.model.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc(addFilters  = false)
@ExtendWith(MockitoExtension.class)
class PaymentsUserServiceImplTest {
    @Mock
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private  PaymentsUserServiceImpl paymentsUserService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private  PasswordUtils passwordUtils;

    @Mock
    private  AuthenticationManager authenticationManager;

    @Test
    void handleUserRegistration() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest("john.doe@example.com", "password123");
        User mockedUser = new User();
        when(userRepository.existsByContact_EmailAddress(signUpRequest.getEmailAddress())).thenReturn(false);

        when(userRepository.save(any(User.class))).thenReturn(mockedUser);

        BaseResponse<?> response = paymentsUserService.handleUserRegistration(signUpRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getResponseCode());
        System.out.println(response.getResponseMessage());
        assertEquals("user registered successfully", response.getResponseMessage());
        assertNull(response.getPayload());
    }

    @Test
    void handleUserLogin() {
        LoginRequest loginRequest = new LoginRequest("john.doe@example.com", "password123");
        User mockedUser = new User();
        Contact contact = new Contact();
        contact.setEmailAddress(loginRequest.getEmailAddress());
        mockedUser.setContact(contact);
        mockedUser.setPassword("hashedPassword");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(mockedUser.getContact().getEmailAddress(), mockedUser.getPassword()));

        when(userRepository.findUserByContact_EmailAddress(loginRequest.getEmailAddress())).thenReturn(Optional.of(mockedUser));



        BaseResponse<?> response = paymentsUserService.handleUserLogin(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getResponseCode());
        assertEquals("user login successful", response.getResponseMessage());
        System.out.println(response.getResponseMessage());
        System.out.println(response);
//        assertNotNull(response.getPayload());


    }
}