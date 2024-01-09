package com.example.xpresspaymentapi.configuration.mapper;


import com.example.xpresspaymentapi.model.dto.airtime.AirtimeVtuRequest;
import com.example.xpresspaymentapi.model.entity.User;
import com.example.xpresspaymentapi.model.entity.VtuAirtimeTransaction;

public class VtuAirtimeTransactionBuilder {
    public static VtuAirtimeTransaction mapResponseToVtuAirtimeTransaction(AirtimeVtuRequest airtimeVtuRequest,
                                                                           User user) {
        return VtuAirtimeTransaction.builder().amount(airtimeVtuRequest.getDetails().getAmount())
                .mobileNumber(airtimeVtuRequest.getDetails().getPhoneNumber()).user(user).build();
    }
}
