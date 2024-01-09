package com.example.xpresspaymentapi.model.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class PurchaseAirtimeRequest {

    @JsonProperty(value = "phone_number", required = true)
    private final String phoneNumber;

    @JsonProperty(value = "amount", required = true)
    @PositiveOrZero(message = "Amount must be zero or positive")
    @DecimalMin(value = "0.00", message = "Amount must not be negative")
    @DecimalMax(value = "50000.00", message = "Amount must not exceed 50000 NGN")
    private final BigDecimal amount;

    @JsonProperty(value = "network_provider", required = true)
    @NotEmpty(message = "network provider cannot be empty")
    private final String provider;
}
