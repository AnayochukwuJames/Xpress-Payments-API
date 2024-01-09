package com.example.xpresspaymentapi.service;


import com.example.xpresspaymentapi.model.dto.base.BaseResponse;
import com.example.xpresspaymentapi.model.dto.user.PurchaseAirtimeRequest;

import java.security.Principal;

public interface PaymentsAirtimeService {
    BaseResponse<?> purchaseAirtime(PurchaseAirtimeRequest purchaseAirtimeRequest);
}
