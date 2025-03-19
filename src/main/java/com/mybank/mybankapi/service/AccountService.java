package com.mybank.mybankapi.service;

import com.mybank.accountservice.model.AccountTotalResponse;
import com.mybank.api.model.AccountTotalRequestApiModel;
import com.mybank.api.model.AccountTotalResponseApiModel;
import com.mybank.mybankapi.exception.AccountServiceClientException;
import com.mybank.mybankapi.integration.accountservice.AccountServiceClient;
import com.mybank.mybankapi.mapper.ResponseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountServiceClient accountServiceClient;
    private final ResponseMapper responseMapper;

    public AccountTotalResponseApiModel getBankAccountTotal(AccountTotalRequestApiModel request) {
        try {
            AccountTotalResponse clientResponse = accountServiceClient
                    .getBankAccountTotal(request.getIban(), request.getCurrencyCode().getValue());

            return responseMapper.toApiModel(clientResponse);
        } catch (AccountServiceClientException e) {
            log.error("Account not found by IBAN: {}", request.getIban());
            throw new IllegalArgumentException();
        }
    }
}
