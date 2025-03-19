package com.mybank.mybankapi.exception;

import com.mybank.transactionservice.model.FailedOnlinePaymentResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class OnlinePaymentException extends RuntimeException {

    private final FailedOnlinePaymentResponse failedResponse;
}
