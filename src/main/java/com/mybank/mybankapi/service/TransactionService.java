package com.mybank.mybankapi.service;

import com.mybank.api.model.OnlinePaymentRequestApiModel;
import com.mybank.api.model.OnlinePaymentResponseApiModel;
import com.mybank.api.model.TransactionRequestApiModel;
import com.mybank.api.model.TransactionResponseApiModel;
import com.mybank.mybankapi.exception.AccountServiceClientException;
import com.mybank.mybankapi.exception.OnlinePaymentException;
import com.mybank.mybankapi.exception.TransactionException;
import com.mybank.mybankapi.exception.TransactionServiceClientException;
import com.mybank.mybankapi.integration.accountservice.AccountServiceClient;
import com.mybank.mybankapi.integration.transactionservice.TransactionServiceClient;
import com.mybank.mybankapi.mapper.ResponseMapper;
import com.mybank.transactionservice.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final AccountServiceClient accountServiceClient;
    private final TransactionServiceClient transactionServiceClient;
    private final ResponseMapper responseMapper;

    public TransactionResponseApiModel debitMoneyFromBankAccount(TransactionRequestApiModel request) {
        try {
            var debitClientRequest = createClientRequest(request);
            var debitClientResponse = transactionServiceClient.debitMoneyFromBankAccount(debitClientRequest);

            return responseMapper.toApiModel(debitClientResponse);

        } catch (AccountServiceClientException e) {
            throw handleAccountServiceException(request);

        } catch (TransactionServiceClientException e) {
            log.error("Failed to debit money from account by IBAN: {}", request.getIban());
            throw new TransactionException((FailedTransactionResponse) e.getFailedResponse());
        }
    }

    public TransactionResponseApiModel depositMoneyToBankAccount(TransactionRequestApiModel request) {
        try {
            var depositClientRequest = createClientRequest(request);
            var depositClientResponse = transactionServiceClient.depositMoneyToBankAccount(depositClientRequest);

            return responseMapper.toApiModel(depositClientResponse);

        } catch (AccountServiceClientException e) {
            throw handleAccountServiceException(request);

        } catch (TransactionServiceClientException e) {
            log.error("Failed to deposit money for account by IBAN: {}", request.getIban());
            throw new TransactionException((FailedTransactionResponse) e.getFailedResponse());
        }
    }

    public OnlinePaymentResponseApiModel makeOnlinePayment(OnlinePaymentRequestApiModel request) {
        try {
            var transactionInfo = createClientRequest(request.getTransactionInfo());

            var depositClientRequest = OnlinePaymentRequest.builder()
                    .transactionInfo(transactionInfo)
                    .callbackURL(request.getCallbackURL())
                    .build();
            var depositClientResponse = transactionServiceClient.makeOnlinePayment(depositClientRequest);

            return responseMapper.toApiModel(depositClientResponse);

        } catch (AccountServiceClientException e) {
            throw handleAccountServiceException(request.getTransactionInfo());

        } catch (TransactionServiceClientException e) {
            log.error("Unable to make online payment from an account by iban {}", request.getTransactionInfo()
                    .getIban());
            throw new OnlinePaymentException((FailedOnlinePaymentResponse) e.getFailedResponse());
        }
    }

    private TransactionRequest createClientRequest(TransactionRequestApiModel request) {
        var accountBalanceProperties = accountServiceClient
                .getAccountBalanceProperties(request.getIban(), request.getCurrencyCode().getValue());

        return TransactionRequest.builder()
                .bankAccountBalanceUuid(accountBalanceProperties.getBankAccountBalanceUuid())
                .amount(request.getAmount())
                .build();
    }

    private TransactionException handleAccountServiceException(TransactionRequestApiModel request) {
        log.error("Account not found by IBAN: {}", request.getIban());

        var failedResponse = FailedTransactionResponse.builder()
                .failureReason(FailedTransactionReason.ACCOUNT_BALANCE_NOT_FOUND)
                .success(false)
                .build();

        return new TransactionException(failedResponse);
    }
}
