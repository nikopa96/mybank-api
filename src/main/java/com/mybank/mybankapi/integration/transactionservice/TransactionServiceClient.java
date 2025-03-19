package com.mybank.mybankapi.integration.transactionservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybank.mybankapi.exception.TransactionServiceClientException;
import com.mybank.mybankapi.integration.RestClientService;
import com.mybank.transactionservice.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

@Service
@RequiredArgsConstructor
public class TransactionServiceClient {

    private final RestClientService restClientService;
    private final ObjectMapper objectMapper;

    @Value("${transaction-service.url}")
    private String transactionServiceUrl;

    public TransactionResponse debitMoneyFromBankAccount(TransactionRequest request) {
        try {
            var response = restClientService.post(
                    transactionServiceUrl,
                    TransactionServiceConstants.METHOD_DEBIT_MONEY,
                    request,
                    new ParameterizedTypeReference<>() {}
            );
            return objectMapper.convertValue(response.getBody(), TransactionResponse.class);
        } catch (HttpStatusCodeException e) {
            throw handleHttpStatusCodeException(e, FailedTransactionResponse.class);
        }
    }

    public TransactionResponse depositMoneyToBankAccount(TransactionRequest request) {
        try {
            var response = restClientService.post(
                    transactionServiceUrl,
                    TransactionServiceConstants.METHOD_DEPOSIT_MONEY,
                    request,
                    new ParameterizedTypeReference<>() {}
            );
            return objectMapper.convertValue(response.getBody(), TransactionResponse.class);
        } catch (HttpStatusCodeException e) {
            throw handleHttpStatusCodeException(e, FailedTransactionResponse.class);
        }
    }

    public OnlinePaymentResponse makeOnlinePayment(OnlinePaymentRequest request) {
        try {
            var response = restClientService.post(
                    transactionServiceUrl,
                    TransactionServiceConstants.METHOD_MAKE_ONLINE_PAYMENT,
                    request,
                    new ParameterizedTypeReference<>() {}
            );
            return objectMapper.convertValue(response.getBody(), OnlinePaymentResponse.class);
        } catch (HttpStatusCodeException e) {
            throw handleHttpStatusCodeException(e, FailedOnlinePaymentResponse.class);
        }
    }

    private <T> TransactionServiceClientException handleHttpStatusCodeException(HttpStatusCodeException e,
                                                                                Class<T> responseType) {
        HttpStatus httpStatus = HttpStatus.valueOf(e.getStatusCode().value());

        if (httpStatus.isSameCodeAs(HttpStatus.UNPROCESSABLE_ENTITY)) {
            T responseBody = e.getResponseBodyAs(responseType);
            return new TransactionServiceClientException(httpStatus, responseBody);
        }

        return new TransactionServiceClientException(httpStatus);
    }
}
