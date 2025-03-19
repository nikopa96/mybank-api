package com.mybank.mybankapi.integration.accountservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybank.accountservice.model.AccountBalanceProperties;
import com.mybank.accountservice.model.AccountTotalRequest;
import com.mybank.accountservice.model.AccountTotalResponse;
import com.mybank.accountservice.model.CurrencyCode;
import com.mybank.mybankapi.exception.AccountServiceClientException;
import com.mybank.mybankapi.integration.RestClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

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

        try {
            var response = restClientService.get(
                    accountServiceUrl,
                    AccountServiceConstants.URL_GET_ACCOUNT_BALANCE_BY_CURRENCY,
                    uriVariables,
                    new ParameterizedTypeReference<>() {}
            );
            return objectMapper.convertValue(response.getBody(), AccountBalanceProperties.class);
        } catch (HttpStatusCodeException e) {
            throw new AccountServiceClientException(HttpStatus.valueOf(e.getStatusCode().value()));
        }
    }

    public AccountTotalResponse getBankAccountTotal(String iban, String currency) {
        var request = new AccountTotalRequest(iban, CurrencyCode.valueOf(currency));

        try {
            var response = restClientService.post(
                    accountServiceUrl,
                    AccountServiceConstants.URL_GET_ACCOUNT_TOTAL,
                    request,
                    new ParameterizedTypeReference<>() {}
            );
            return objectMapper.convertValue(response.getBody(), AccountTotalResponse.class);
        } catch (HttpStatusCodeException e) {
            throw new AccountServiceClientException(HttpStatus.valueOf(e.getStatusCode().value()));
        }
    }
}
