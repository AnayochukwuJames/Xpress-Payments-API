package com.example.xpresspaymentapi.model.dto.airtime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AirtimeVtuRequest implements Serializable {

    @JsonProperty(value = "requestId", required = true)
    private Long requestId;

    @JsonProperty(value = "uniqueCode", required = true)
    private String uniqueCode;

    @JsonProperty("details")
    private Details details;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Details implements Serializable {
        @JsonProperty(value = "phoneNumber", required = true)
        private String phoneNumber;

        @JsonProperty(value = "amount", required = true)
        private BigDecimal amount;
    }
}
