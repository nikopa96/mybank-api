package com.mybank.mybankapi.service;

import com.mybank.accountservice.model.AccountTotalResponse;
import com.mybank.api.model.AccountTotalRequestApiModel;
import com.mybank.api.model.AccountTotalResponseApiModel;
import com.mybank.mybankapi.integration.accountservice.AccountServiceClient;
import com.mybank.mybankapi.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountServiceClient accountServiceClient;
    private final AccountMapper accountMapper;

    public AccountTotalResponseApiModel getBankAccountTotal(AccountTotalRequestApiModel request) {
        AccountTotalResponse clientResponse = accountServiceClient
                .getBankAccountTotal(request.getIban(), request.getCurrencyCode().getValue());

        return accountMapper.toApiModel(clientResponse);
    }
}
