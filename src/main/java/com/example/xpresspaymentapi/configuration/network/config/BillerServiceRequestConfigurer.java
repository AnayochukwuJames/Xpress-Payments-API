package com.example.xpresspaymentapi.configuration.network.config;

import com.example.xpresspaymentapi.configuration.exception.GenericException;
import com.example.xpresspaymentapi.model.dto.airtime.AirtimeVtuRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class BillerServiceRequestConfigurer {

    @Value("${biller.api.private.key}")
    private String billerServicePrivateKey;

    @Value("${biller.api.public.key}")
    private String billerServicePublicKey;

    public  Map<String, String> configureBillerRequestHeader(AirtimeVtuRequest airtimeVtuRequest) {
        Map<String, String> headers = new HashMap<>();
        String requestInJsonFormat = deserializeRequest(airtimeVtuRequest);
        headers.put("PaymentHash", calculateHMAC512(requestInJsonFormat));
        headers.put("Authorization", "Bearer " + billerServicePublicKey);
        headers.put("Channel", "API");
        headers.put("Content-Type", "application/json");
        return headers;
    }

    private static String deserializeRequest(AirtimeVtuRequest airtimeVtuRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(airtimeVtuRequest);
        } catch (JsonProcessingException e) {
            log.info("Error writing airtime request to json object");
            throw new GenericException("Error writing airtime request to json object");
        }
    }

    private String calculateHMAC512(String payload) {
        String HMAC_SHA512 = "HmacSHA512";
        SecretKeySpec secretKeySpec = new SecretKeySpec(billerServicePrivateKey.getBytes(), HMAC_SHA512);
        Mac mac = null;
        try {
            mac = Mac.getInstance(HMAC_SHA512);
            mac.init(secretKeySpec);
            return Hex.encodeHexString(mac.doFinal(payload.getBytes()));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            throw new GenericException("error initiating airtime purchase transaction");
        }
    }
}
