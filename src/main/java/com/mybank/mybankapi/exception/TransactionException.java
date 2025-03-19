package com.mybank.mybankapi.exception;

import com.mybank.transactionservice.model.FailedTransactionResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class TransactionException extends RuntimeException {

    private final FailedTransactionResponse failedResponse;
}
