package com.mybank.mybankapi.service;

import com.mybank.accountservice.model.AccountTotalResponse;
import com.mybank.api.model.AccountTotalRequestApiModel;
import com.mybank.api.model.CurrencyCodeApiModel;
import com.mybank.mybankapi.exception.AccountServiceClientException;
import com.mybank.mybankapi.exception.BankAccountNotFoundException;
import com.mybank.mybankapi.integration.accountservice.AccountServiceClient;
import com.mybank.mybankapi.mapper.ResponseMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountServiceClient accountServiceClient;

    @Mock
    private ResponseMapper responseMapper;

    @InjectMocks
    private AccountService accountService;

    private AccountTotalRequestApiModel fakeRequest;

    @BeforeEach
    void setUp() {
        fakeRequest = AccountTotalRequestApiModel.builder()
                .iban("EE251298943438713614")
                .currencyCode(CurrencyCodeApiModel.SEK)
                .build();
    }

    @Test
    void getBankAccountTotal() {
        // 200 OK
        AccountTotalResponse fakeResponse = new AccountTotalResponse();
        when(accountServiceClient.getBankAccountTotal(any(), any())).thenReturn(fakeResponse);

        accountService.getBankAccountTotal(fakeRequest);

        verify(responseMapper, times(1)).toApiModel(fakeResponse);
    }

    @Test
    void getBankAccountTotal_BankAccountNotFoundException() {
        // AccountService returns 422
        when(accountServiceClient.getBankAccountTotal(any(), any()))
                .thenThrow(new AccountServiceClientException(HttpStatus.UNPROCESSABLE_ENTITY));
        assertThrows(BankAccountNotFoundException.class, () -> accountService.getBankAccountTotal(fakeRequest));
    }
}