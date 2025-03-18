package com.mybank.mybankapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ClientIntegrationException extends RuntimeException {

    private final HttpStatus statusCode;

    public ClientIntegrationException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
