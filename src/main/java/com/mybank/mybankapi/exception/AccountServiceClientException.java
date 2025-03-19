package com.mybank.mybankapi.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public class AccountServiceClientException extends RuntimeException {

    private final HttpStatus statusCode;
}
