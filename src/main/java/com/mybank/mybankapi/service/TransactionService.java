package com.mybank.mybankapi.service;

import com.mybank.api.model.TransactionRequestApiModel;
import com.mybank.api.model.TransactionResponseApiModel;
import com.mybank.mybankapi.integration.accountservice.AccountServiceClient;
import com.mybank.mybankapi.integration.transactionservice.TransactionServiceClient;
import com.mybank.mybankapi.mapper.ResponseMapper;
import com.mybank.transactionservice.model.TransactionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountServiceClient accountServiceClient;
    private final TransactionServiceClient transactionServiceClient;
    private final ResponseMapper responseMapper;

    public TransactionResponseApiModel debitMoneyFromBankAccount(TransactionRequestApiModel request) {
        var debitClientRequest = createClientRequest(request);
        var debitClientResponse = transactionServiceClient.debitMoneyFromBankAccount(debitClientRequest);

        return responseMapper.toApiModel(debitClientResponse);
    }

    public TransactionResponseApiModel depositMoneyToBankAccount(TransactionRequestApiModel request) {
        var depositClientRequest = createClientRequest(request);
        var depositClientResponse = transactionServiceClient.depositMoneyToBankAccount(depositClientRequest);

        return responseMapper.toApiModel(depositClientResponse);
    }

    private TransactionRequest createClientRequest(TransactionRequestApiModel request) {
        var accountBalanceProperties = accountServiceClient
                .getAccountBalanceProperties(request.getIban(), request.getCurrencyCode().getValue());

        return TransactionRequest.builder()
                .bankAccountBalanceUuid(accountBalanceProperties.getBankAccountBalanceUuid())
                .amount(request.getAmount())
                .build();
    }
}
