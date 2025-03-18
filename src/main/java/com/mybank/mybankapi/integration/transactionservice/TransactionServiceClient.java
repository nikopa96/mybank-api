package com.mybank.mybankapi.integration.transactionservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybank.mybankapi.exception.ClientIntegrationException;
import com.mybank.mybankapi.integration.RestClientService;
import com.mybank.transactionservice.model.OnlinePaymentRequest;
import com.mybank.transactionservice.model.OnlinePaymentResponse;
import com.mybank.transactionservice.model.TransactionRequest;
import com.mybank.transactionservice.model.TransactionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceClient {

    private final RestClientService restClientService;
    private final ObjectMapper objectMapper;

    @Value("${transaction-service.url}")
    private String transactionServiceUrl;

    public TransactionResponse debitMoneyFromBankAccount(TransactionRequest request) {
        var response = restClientService.post(
                transactionServiceUrl,
                TransactionServiceConstants.METHOD_DEBIT_MONEY,
                request,
                new ParameterizedTypeReference<>() {}
        );

        String errorMessage = String.format("Unable to debit money from an bank_account_balance: %s",
                request.getBankAccountBalanceUuid());
        validateResponseStatusCode(response, errorMessage);

        return objectMapper.convertValue(response.getBody(), TransactionResponse.class);
    }

    public TransactionResponse depositMoneyToBankAccount(TransactionRequest request) {
        var response = restClientService.post(
                transactionServiceUrl,
                TransactionServiceConstants.METHOD_DEPOSIT_MONEY,
                request,
                new ParameterizedTypeReference<>() {}
        );

        String errorMessage = String.format("Unable to deposit money to an bank_account_balance: %s",
                request.getBankAccountBalanceUuid());
        validateResponseStatusCode(response, errorMessage);

        return objectMapper.convertValue(response.getBody(), TransactionResponse.class);
    }

    public OnlinePaymentResponse makeOnlinePayment(OnlinePaymentRequest request) {
        var response = restClientService.post(
                transactionServiceUrl,
                TransactionServiceConstants.METHOD_MAKE_ONLINE_PAYMENT,
                request,
                new ParameterizedTypeReference<>() {}
        );

        String errorMessage = String.format("Unable to deposit money to an bank_account_balance: %s",
                request.getTransactionInfo().getBankAccountBalanceUuid());
        validateResponseStatusCode(response, errorMessage);

        return objectMapper.convertValue(response.getBody(), OnlinePaymentResponse.class);
    }

    private static void validateResponseStatusCode(ResponseEntity<Object> response, String errorMessage) {
        if (!response.getStatusCode().is2xxSuccessful()) {
            HttpStatus httpStatus = HttpStatus.valueOf(response.getStatusCode().value());
            throw new ClientIntegrationException(errorMessage, httpStatus);
        }
    }
}
