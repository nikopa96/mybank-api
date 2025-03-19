package com.mybank.mybankapi.integration.transactionservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybank.mybankapi.exception.TransactionServiceClientException;
import com.mybank.mybankapi.integration.RestClientService;
import com.mybank.transactionservice.model.*;
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

        validateTransactionResponseStatusCode(response);
        return objectMapper.convertValue(response.getBody(), TransactionResponse.class);
    }

    public TransactionResponse depositMoneyToBankAccount(TransactionRequest request) {
        var response = restClientService.post(
                transactionServiceUrl,
                TransactionServiceConstants.METHOD_DEPOSIT_MONEY,
                request,
                new ParameterizedTypeReference<>() {}
        );

        validateTransactionResponseStatusCode(response);
        return objectMapper.convertValue(response.getBody(), TransactionResponse.class);
    }

    public OnlinePaymentResponse makeOnlinePayment(OnlinePaymentRequest request) {
        var response = restClientService.post(
                transactionServiceUrl,
                TransactionServiceConstants.METHOD_MAKE_ONLINE_PAYMENT,
                request,
                new ParameterizedTypeReference<>() {}
        );

        validateOnlinePaymentResponseStatusCode(response);
        return objectMapper.convertValue(response.getBody(), OnlinePaymentResponse.class);
    }

    private void validateTransactionResponseStatusCode(ResponseEntity<Object> response) {
        HttpStatus httpStatus = HttpStatus.valueOf(response.getStatusCode().value());

        if (response.getStatusCode().isSameCodeAs(HttpStatus.UNPROCESSABLE_ENTITY)) {
            var failedResponse = objectMapper.convertValue(response.getBody(), FailedTransactionResponse.class);
            throw new TransactionServiceClientException(httpStatus, failedResponse);
        }
        if (response.getStatusCode().isSameCodeAs(HttpStatus.BAD_REQUEST)) {
            throw new TransactionServiceClientException(httpStatus);
        }
    }

    private void validateOnlinePaymentResponseStatusCode(ResponseEntity<Object> response) {
        HttpStatus httpStatus = HttpStatus.valueOf(response.getStatusCode().value());

        if (response.getStatusCode().isSameCodeAs(HttpStatus.UNPROCESSABLE_ENTITY)) {
            var failedResponse = objectMapper.convertValue(response.getBody(), FailedOnlinePaymentResponse.class);
            throw new TransactionServiceClientException(httpStatus, failedResponse);
        }
        if (response.getStatusCode().isSameCodeAs(HttpStatus.BAD_REQUEST)) {
            throw new TransactionServiceClientException(httpStatus);
        }
    }
}
