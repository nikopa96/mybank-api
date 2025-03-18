package com.mybank.mybankapi.controller;

import com.mybank.api.AccountApi;
import com.mybank.api.model.AccountTotalRequestApiModel;
import com.mybank.api.model.AccountTotalResponseApiModel;
import com.mybank.mybankapi.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController implements AccountApi {

    private final AccountService accountService;

    @Override
    public ResponseEntity<AccountTotalResponseApiModel> getBankAccountTotal(AccountTotalRequestApiModel request) {
        return ResponseEntity.ok(accountService.getBankAccountTotal(request));
    }
}
