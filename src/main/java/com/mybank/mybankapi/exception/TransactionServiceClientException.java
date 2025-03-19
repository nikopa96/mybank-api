package com.mybank.mybankapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class TransactionServiceClientException extends RuntimeException {

    private final HttpStatus statusCode;
    private Object failedResponse;

    public TransactionServiceClientException(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    public TransactionServiceClientException(HttpStatus statusCode, Object failedResponse) {
        this.statusCode = statusCode;
        this.failedResponse = failedResponse;
    }
}
