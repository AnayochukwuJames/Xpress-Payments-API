package com.example.xpresspaymentapi.model.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PurchaseAirtimeResponse implements Serializable {
    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("mobile_number")
    private String mobileNumber;

    @JsonProperty("transaction_status")
    private String transactionStatus;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}
