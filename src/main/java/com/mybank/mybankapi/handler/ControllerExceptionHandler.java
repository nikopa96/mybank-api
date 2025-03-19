package com.mybank.mybankapi.handler;

import com.mybank.api.model.FailedOnlinePaymentResponseApiModel;
import com.mybank.api.model.FailedTransactionResponseApiModel;
import com.mybank.mybankapi.exception.BankAccountNotFoundException;
import com.mybank.mybankapi.exception.OnlinePaymentException;
import com.mybank.mybankapi.exception.TransactionException;
import com.mybank.mybankapi.mapper.ResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class ControllerExceptionHandler {

    private final ResponseMapper responseMapper;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Void> handleMethodArgumentNotValidException() {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BankAccountNotFoundException.class)
    public ResponseEntity<Void> handleBankAccountNotFoundException() {
        return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<FailedTransactionResponseApiModel> handleTransactionException(TransactionException e) {
        return new ResponseEntity<>(responseMapper.toApiModel(e.getFailedResponse()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(OnlinePaymentException.class)
    public ResponseEntity<FailedOnlinePaymentResponseApiModel> handleOnlinePaymentException(OnlinePaymentException e) {
        return new ResponseEntity<>(responseMapper.toApiModel(e.getFailedResponse()), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
