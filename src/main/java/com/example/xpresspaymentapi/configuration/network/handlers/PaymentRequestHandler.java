package com.example.xpresspaymentapi.configuration.network.handlers;

import com.example.xpresspaymentapi.configuration.exception.GenericException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentRequestHandler {

    private final RestTemplate restTemplate;

    public Object sendNetworkPostRequest(String url, Object requestPayload, Class<?> responseClass, Map<String, String> headers) {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setAll(headers);
            HttpEntity<Object> requestEntity = new HttpEntity<>(requestPayload, httpHeaders);
            ResponseEntity<?> responseEntity = restTemplate.postForEntity(url, requestEntity, responseClass);
            return responseEntity.getBody();
        } catch (Exception e) {
            log.info("netowrk request failed: " + e.getMessage());
            throw new GenericException("netowrk request failed");
        }
    }
}
