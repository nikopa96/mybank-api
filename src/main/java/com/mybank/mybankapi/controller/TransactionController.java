package com.mybank.mybankapi.controller;

import com.mybank.api.TransactionApi;
import com.mybank.api.model.OnlinePaymentRequestApiModel;
import com.mybank.api.model.OnlinePaymentResponseApiModel;
import com.mybank.api.model.TransactionRequestApiModel;
import com.mybank.api.model.TransactionResponseApiModel;
import com.mybank.mybankapi.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TransactionController implements TransactionApi {

    private final TransactionService transactionService;

    @Override
    public ResponseEntity<TransactionResponseApiModel> debitMoneyFromBankAccount(TransactionRequestApiModel request) {
        return ResponseEntity.ok(transactionService.debitMoneyFromBankAccount(request));
    }

    @Override
    public ResponseEntity<TransactionResponseApiModel> depositMoneyToBankAccount(TransactionRequestApiModel request) {
        return ResponseEntity.ok(transactionService.depositMoneyToBankAccount(request));
    }

    @Override
    public ResponseEntity<OnlinePaymentResponseApiModel> makeOnlinePayment(OnlinePaymentRequestApiModel request) {
        return ResponseEntity.ok(transactionService.makeOnlinePayment(request));
    }
}
