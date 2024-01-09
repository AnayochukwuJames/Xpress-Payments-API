package com.example.xpresspaymentapi.controller;

import com.example.xpresspaymentapi.model.dto.base.BaseResponse;
import com.example.xpresspaymentapi.model.dto.user.PurchaseAirtimeRequest;
import com.example.xpresspaymentapi.service.PaymentsAirtimeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/vtu/airtime", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType
        .APPLICATION_JSON_VALUE)
public class PaymentsAirtimeController {

    private final PaymentsAirtimeService paymentsAirtimeService;

    @PostMapping("/purchase")
    public ResponseEntity<?> purchaseAirtime(@RequestBody @Valid PurchaseAirtimeRequest purchaseAirtimeRequest
                                             ) {
        BaseResponse<?> br = paymentsAirtimeService.purchaseAirtime(purchaseAirtimeRequest);
        if (br.getResponseCode() == HttpStatus.OK.value()) {
            return ResponseEntity.ok().body(br);
        } else {
            return ResponseEntity.badRequest().body(br);
        }
    }
}
