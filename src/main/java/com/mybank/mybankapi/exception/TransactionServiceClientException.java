package com.mybank.mybankapi.exception;

import com.mybank.transactionservice.model.FailedOnlinePaymentResponse;
import com.mybank.transactionservice.model.FailedTransactionResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class TransactionServiceClientException extends RuntimeException {

    private final HttpStatus statusCode;
    private FailedTransactionResponse failedTransactionResponse;
    private FailedOnlinePaymentResponse failedOnlinePaymentResponse;

    public TransactionServiceClientException(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    public TransactionServiceClientException(HttpStatus statusCode,
                                             FailedTransactionResponse failedTransactionResponse) {
        this.statusCode = statusCode;
        this.failedTransactionResponse = failedTransactionResponse;
    }

    public TransactionServiceClientException(HttpStatus statusCode,
                                             FailedOnlinePaymentResponse failedTransactionResponse) {
        this.statusCode = statusCode;
        this.failedOnlinePaymentResponse = failedTransactionResponse;
    }
}
