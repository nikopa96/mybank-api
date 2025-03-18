package com.mybank.mybankapi.integration.accountservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybank.accountservice.model.AccountBalanceProperties;
import com.mybank.accountservice.model.AccountTotalRequest;
import com.mybank.accountservice.model.AccountTotalResponse;
import com.mybank.accountservice.model.CurrencyCode;
import com.mybank.mybankapi.exception.ClientIntegrationException;
import com.mybank.mybankapi.integration.RestClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AccountServiceClient {

    private final RestClientService restClientService;
    private final ObjectMapper objectMapper;

    @Value("${account-service.url}")
    private String accountServiceUrl;

    public AccountBalanceProperties getAccountBalanceProperties(String iban, String currency) {
        var uriVariables = Map.of("iban", iban, "currencyCode", currency);

        var response = restClientService.get(
                accountServiceUrl,
                AccountServiceConstants.URL_GET_ACCOUNT_BALANCE_BY_CURRENCY,
                uriVariables,
                new ParameterizedTypeReference<>() {}
        );

        validateResponseStatusCode(response);
        return objectMapper.convertValue(response.getBody(), AccountBalanceProperties.class);
    }

    public AccountTotalResponse getBankAccountTotal(String iban, String currency) {
        var request = new AccountTotalRequest(iban, CurrencyCode.valueOf(currency));

        var response = restClientService.post(
                accountServiceUrl,
                AccountServiceConstants.URL_GET_ACCOUNT_TOTAL,
                request,
                new ParameterizedTypeReference<>() {}
        );

        validateResponseStatusCode(response);
        return objectMapper.convertValue(response.getBody(), AccountTotalResponse.class);
    }

    private static void validateResponseStatusCode(ResponseEntity<Object> response) {
        if (!response.getStatusCode().is2xxSuccessful()) {
            HttpStatus httpStatus = HttpStatus.valueOf(response.getStatusCode().value());
            String message = "Unable to get response. Status: " + response.getStatusCode();

            throw new ClientIntegrationException(message, httpStatus);
        }
    }
}
