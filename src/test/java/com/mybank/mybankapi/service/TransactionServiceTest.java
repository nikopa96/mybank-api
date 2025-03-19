package com.mybank.mybankapi.service;

import com.mybank.accountservice.model.AccountBalanceProperties;
import com.mybank.api.model.CurrencyCodeApiModel;
import com.mybank.api.model.OnlinePaymentRequestApiModel;
import com.mybank.api.model.TransactionRequestApiModel;
import com.mybank.mybankapi.exception.AccountServiceClientException;
import com.mybank.mybankapi.exception.OnlinePaymentException;
import com.mybank.mybankapi.exception.TransactionException;
import com.mybank.mybankapi.exception.TransactionServiceClientException;
import com.mybank.mybankapi.integration.accountservice.AccountServiceClient;
import com.mybank.mybankapi.integration.transactionservice.TransactionServiceClient;
import com.mybank.mybankapi.mapper.ResponseMapper;
import com.mybank.transactionservice.model.OnlinePaymentResponse;
import com.mybank.transactionservice.model.TransactionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private AccountServiceClient accountServiceClient;

    @Mock
    private TransactionServiceClient transactionServiceClient;

    @Mock
    private ResponseMapper responseMapper;

    @InjectMocks
    private TransactionService transactionService;

    private TransactionRequestApiModel fakeRequest;

    @BeforeEach
    void setUp() {
        fakeRequest = TransactionRequestApiModel.builder()
                .iban("EE251298943438713614")
                .currencyCode(CurrencyCodeApiModel.EUR)
                .build();
    }

    @Test
    void testDebitMoneyFromBankAccount() {
        // 200 OK
        when(accountServiceClient.getAccountBalanceProperties(anyString(), anyString()))
                .thenReturn(AccountBalanceProperties.builder().bankAccountBalanceUuid(UUID.randomUUID()).build());

        TransactionResponse fakeResponse = new TransactionResponse();
        when(transactionServiceClient.debitMoneyFromBankAccount(any())).thenReturn(fakeResponse);

        transactionService.debitMoneyFromBankAccount(fakeRequest);

        verify(responseMapper, times(1)).toApiModel(fakeResponse);
    }

    @Test
    void testDebitMoneyFromBankAccount_AccountServiceClientException() {
        // AccountService returns 404
        when(accountServiceClient.getAccountBalanceProperties(anyString(), anyString()))
                .thenThrow(new AccountServiceClientException(HttpStatus.NOT_FOUND));
        assertThrows(TransactionException.class, () -> transactionService.debitMoneyFromBankAccount(fakeRequest));
    }

    @Test
    void testDebitMoneyFromBankAccount_TransactionServiceClientException() {
        // TransactionService returns 422
        when(accountServiceClient.getAccountBalanceProperties(anyString(), anyString()))
                .thenReturn(AccountBalanceProperties.builder().bankAccountBalanceUuid(UUID.randomUUID()).build());
        when(transactionServiceClient.debitMoneyFromBankAccount(any()))
                .thenThrow(new TransactionServiceClientException(HttpStatus.UNPROCESSABLE_ENTITY));
        assertThrows(TransactionException.class, () -> transactionService.debitMoneyFromBankAccount(fakeRequest));
    }

    @Test
    void testDepositMoneyFromBankAccount() {
        // 200 OK
        when(accountServiceClient.getAccountBalanceProperties(anyString(), anyString()))
                .thenReturn(AccountBalanceProperties.builder().bankAccountBalanceUuid(UUID.randomUUID()).build());

        TransactionResponse fakeResponse = new TransactionResponse();
        when(transactionServiceClient.depositMoneyToBankAccount(any())).thenReturn(fakeResponse);

        transactionService.depositMoneyToBankAccount(fakeRequest);

        verify(responseMapper, times(1)).toApiModel(fakeResponse);
    }

    @Test
    void testDepositMoneyFromBankAccount_AccountServiceClientException() {
        // AccountService returns 404
        when(accountServiceClient.getAccountBalanceProperties(anyString(), anyString()))
                .thenThrow(new AccountServiceClientException(HttpStatus.NOT_FOUND));
        assertThrows(TransactionException.class, () -> transactionService.depositMoneyToBankAccount(fakeRequest));
    }

    @Test
    void testDepositMoneyFromBankAccount_TransactionServiceClientException() {
        // TransactionService returns 422
        when(accountServiceClient.getAccountBalanceProperties(anyString(), anyString()))
                .thenReturn(AccountBalanceProperties.builder().bankAccountBalanceUuid(UUID.randomUUID()).build());
        when(transactionServiceClient.depositMoneyToBankAccount(any()))
                .thenThrow(new TransactionServiceClientException(HttpStatus.UNPROCESSABLE_ENTITY));
        assertThrows(TransactionException.class, () -> transactionService.depositMoneyToBankAccount(fakeRequest));
    }

    @Test
    void testMakeOnlinePayment() {
        // 200 OK
        when(accountServiceClient.getAccountBalanceProperties(anyString(), anyString()))
                .thenReturn(AccountBalanceProperties.builder().bankAccountBalanceUuid(UUID.randomUUID()).build());

        OnlinePaymentResponse fakeResponse = new OnlinePaymentResponse();
        when(transactionServiceClient.makeOnlinePayment(any())).thenReturn(fakeResponse);

        var fakeOnlinePaymentRequest = OnlinePaymentRequestApiModel.builder()
                .transactionInfo(fakeRequest)
                .callbackURL("http.us")
                .build();
        transactionService.makeOnlinePayment(fakeOnlinePaymentRequest);

        verify(responseMapper, times(1)).toApiModel(fakeResponse);
    }

    @Test
    void testMakeOnlinePayment_AccountServiceClientException() {
        // AccountService returns 404
        var fakeOnlinePaymentRequest = OnlinePaymentRequestApiModel.builder()
                .transactionInfo(fakeRequest)
                .callbackURL("http.us")
                .build();

        when(accountServiceClient.getAccountBalanceProperties(anyString(), anyString()))
                .thenThrow(new AccountServiceClientException(HttpStatus.NOT_FOUND));
        assertThrows(OnlinePaymentException.class,
                () -> transactionService.makeOnlinePayment(fakeOnlinePaymentRequest));
    }

    @Test
    void testMakeOnlinePayment_TransactionServiceClientException() {
        // TransactionService returns 422
        var fakeOnlinePaymentRequest = OnlinePaymentRequestApiModel.builder()
                .transactionInfo(fakeRequest)
                .callbackURL("http.us")
                .build();

        when(accountServiceClient.getAccountBalanceProperties(anyString(), anyString()))
                .thenReturn(AccountBalanceProperties.builder().bankAccountBalanceUuid(UUID.randomUUID()).build());
        when(transactionServiceClient.makeOnlinePayment(any()))
                .thenThrow(new TransactionServiceClientException(HttpStatus.UNPROCESSABLE_ENTITY));
        assertThrows(OnlinePaymentException.class,
                () -> transactionService.makeOnlinePayment(fakeOnlinePaymentRequest));
    }
}