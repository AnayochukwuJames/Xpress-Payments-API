package com.example.xpresspaymentapi.service.impl;

import com.example.xpresspaymentapi.configuration.network.config.BillerServiceRequestConfigurer;
import com.example.xpresspaymentapi.configuration.network.handlers.PaymentRequestHandler;
import com.example.xpresspaymentapi.configuration.security.JwtAuthenticationFilter;
import com.example.xpresspaymentapi.configuration.utils.Constants;
import com.example.xpresspaymentapi.model.dto.airtime.AirtimeVtuResponse;
import com.example.xpresspaymentapi.model.dto.base.BaseResponse;
import com.example.xpresspaymentapi.model.dto.user.PurchaseAirtimeRequest;
import com.example.xpresspaymentapi.model.entity.Contact;
import com.example.xpresspaymentapi.model.entity.TelecomNetworkProvider;
import com.example.xpresspaymentapi.model.entity.User;
import com.example.xpresspaymentapi.model.repository.TelecomNetworkProviderRepository;
import com.example.xpresspaymentapi.model.repository.UserRepository;
import com.example.xpresspaymentapi.model.repository.VtuAirtimeTransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc(addFilters  = false)
@ExtendWith(MockitoExtension.class)
class PaymentsAirtimeServiceImplTest {
    @Mock
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @InjectMocks
    private  PaymentsAirtimeServiceImpl purchaseAirtime;
    @Mock
    private  UserRepository userRepository;

    @Mock
    private  TelecomNetworkProviderRepository telecomNetworkProviderRepository;
    @Mock
    private  VtuAirtimeTransactionRepository vtuAirtimeTransactionRepository;


    @Mock
    private  PaymentRequestHandler paymentRequestHandler;

    private PurchaseAirtimeRequest purchaseAirtimeRequest;
    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);

        BigDecimal amount = BigDecimal.valueOf(123.45);
        purchaseAirtimeRequest = new PurchaseAirtimeRequest("09876545678", amount, "MTN");
        // Set up the security context
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("jamesmike@gmail.com");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

    }

    @Test
    void purchaseAirtime() {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        User mockUser = new User();
        Contact contact = new Contact();
        contact.setEmailAddress("jamesmike@gmail.com");
        contact.setPhoneNumber("07066929216");
        mockUser.setContact(contact);
        mockUser.setPassword("Anayochukwujames12#}");

        when(userRepository.findUserByContact_EmailAddress(SecurityContextHolder.getContext().getAuthentication().getName())).thenReturn(Optional.of(mockUser));

        TelecomNetworkProvider mockProvider = new TelecomNetworkProvider();
        when(telecomNetworkProviderRepository.findTelecomNetworkProviderByProviderNameEqualsIgnoreCase("MTN"))
                .thenReturn(mockProvider);

        // Mock the response from paymentRequestHandler
        AirtimeVtuResponse airtimeVtuResponse = new AirtimeVtuResponse();
        airtimeVtuResponse.setResponseCode(Constants.BILLER_SUCCESS_CODE);
        when(paymentRequestHandler.sendNetworkPostRequest(
                eq(Constants.BillerServiceEndpoints.PURCHASE_AIRTIME),
                any(PurchaseAirtimeRequest.class),
                eq(AirtimeVtuResponse.class),
                anyMap()))
                .thenReturn(airtimeVtuResponse);

        BaseResponse<?> response = purchaseAirtime.purchaseAirtime(purchaseAirtimeRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getResponseCode());
    }

}