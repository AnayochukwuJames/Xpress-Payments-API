package com.example.xpresspaymentapi.service.impl;

import com.example.xpresspaymentapi.configuration.exception.AirtimePurchaseException;
import com.example.xpresspaymentapi.configuration.exception.GenericException;
import com.example.xpresspaymentapi.configuration.exception.InvalidRequestException;
import com.example.xpresspaymentapi.configuration.mapper.VtuAirtimeTransactionBuilder;
import com.example.xpresspaymentapi.configuration.network.config.BillerServiceRequestConfigurer;
import com.example.xpresspaymentapi.configuration.network.handlers.PaymentRequestHandler;
import com.example.xpresspaymentapi.configuration.utils.BillerTransactionIdGenerator;
import com.example.xpresspaymentapi.configuration.utils.Constants;
import com.example.xpresspaymentapi.model.dto.airtime.AirtimeVtuRequest;
import com.example.xpresspaymentapi.model.dto.airtime.AirtimeVtuResponse;
import com.example.xpresspaymentapi.model.dto.base.BaseResponse;
import com.example.xpresspaymentapi.model.dto.user.PurchaseAirtimeRequest;
import com.example.xpresspaymentapi.model.dto.user.PurchaseAirtimeResponse;
import com.example.xpresspaymentapi.model.entity.TelecomNetworkProvider;
import com.example.xpresspaymentapi.model.entity.User;
import com.example.xpresspaymentapi.model.entity.VtuAirtimeTransaction;
import com.example.xpresspaymentapi.model.enums.TransactionStatus;
import com.example.xpresspaymentapi.model.repository.TelecomNetworkProviderRepository;
import com.example.xpresspaymentapi.model.repository.UserRepository;
import com.example.xpresspaymentapi.model.repository.VtuAirtimeTransactionRepository;
import com.example.xpresspaymentapi.service.PaymentsAirtimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentsAirtimeServiceImpl implements PaymentsAirtimeService {

    private final BillerServiceRequestConfigurer billerServiceRequestConfigurer;

    private final UserRepository userRepository;

    private final TelecomNetworkProviderRepository telecomNetworkProviderRepository;

    private final VtuAirtimeTransactionRepository vtuAirtimeTransactionRepository;

    private final PaymentRequestHandler paymentRequestHandler;

    @Override
    public BaseResponse<?> purchaseAirtime(PurchaseAirtimeRequest purchaseAirtimeRequest) {
        BaseResponse<?> br = null;
       try {
           System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
           Optional<User> foundUser = userRepository.findUserByContact_EmailAddress(SecurityContextHolder.getContext().getAuthentication().getName());
           if (foundUser.isPresent()) {
               AirtimeVtuRequest airtimeVtuRequest = new AirtimeVtuRequest();
               airtimeVtuRequest.setRequestId(BillerTransactionIdGenerator.generateTransactionId());
               TelecomNetworkProvider telecomNetworkProvider = telecomNetworkProviderRepository
                       .findTelecomNetworkProviderByProviderNameEqualsIgnoreCase(purchaseAirtimeRequest.getProvider());
               System.out.println(telecomNetworkProvider.getProviderName());
              if (!ObjectUtils.isEmpty(telecomNetworkProvider)) {
                  String uniqueCode = telecomNetworkProvider.getUniqueCode();
                  if (!ObjectUtils.isEmpty(uniqueCode)) {
                      airtimeVtuRequest.setUniqueCode(uniqueCode);
                      AirtimeVtuRequest.Details details = new AirtimeVtuRequest.Details();
                      details.setAmount(purchaseAirtimeRequest.getAmount());
                      details.setPhoneNumber(purchaseAirtimeRequest.getPhoneNumber());
                      airtimeVtuRequest.setDetails(details);

                      Map<String, String> headers = billerServiceRequestConfigurer.configureBillerRequestHeader(airtimeVtuRequest);
                      AirtimeVtuResponse airtimeVtuResponse = (AirtimeVtuResponse) paymentRequestHandler
                              .sendNetworkPostRequest(Constants.BillerServiceEndpoints.PURCHASE_AIRTIME, airtimeVtuRequest,
                                      AirtimeVtuResponse.class, headers);


                      VtuAirtimeTransaction vtuAirtimeTransaction = VtuAirtimeTransactionBuilder.mapResponseToVtuAirtimeTransaction(
                              airtimeVtuRequest, foundUser.get());
                      if (!ObjectUtils.isEmpty(details)){
                          if (airtimeVtuResponse.getResponseCode().equals(Constants.BILLER_SUCCESS_CODE)) {
                              vtuAirtimeTransaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
                          } else {
                              vtuAirtimeTransaction.setTransactionStatus(TransactionStatus.FAILED);
                          }
                          vtuAirtimeTransactionRepository.save(vtuAirtimeTransaction);

                          PurchaseAirtimeResponse purchaseAirtimeResponse = PurchaseAirtimeResponse.builder()
                                  .mobileNumber(vtuAirtimeTransaction.getMobileNumber())
                                  .transactionStatus(vtuAirtimeTransaction.getTransactionStatus().toString())
                                  .amount(vtuAirtimeTransaction.getAmount()).createdAt(vtuAirtimeTransaction
                                          .getCreatedAt()).build();
                          br = BaseResponse.builder().responseCode(HttpStatus.OK.value()).responseMessage("airtime purchase successful")
                                  .payload(purchaseAirtimeResponse).build();
                      } else {
                          throw new GenericException("invalid response from biller airtime service");
                      }
                  } else {
                      throw new GenericException("unique code does not exist for provider");
                  }
              } else {
                  throw new InvalidRequestException("invalid network provider for airtime transaction");
              }
           } else {
               throw new UsernameNotFoundException("user does not exist");
           }
       } catch (Exception e) {
            log.info("[ AIRTIME-SERVICE ] -- error: {}", e.getMessage());
            throw new AirtimePurchaseException("error purchasing airtime: {}", e);
       }
        return br;
    }
}
