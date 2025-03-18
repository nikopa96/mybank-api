package com.mybank.mybankapi.mapper;

import com.mybank.accountservice.model.AccountTotalResponse;
import com.mybank.api.model.AccountTotalResponseApiModel;
import com.mybank.api.model.OnlinePaymentResponseApiModel;
import com.mybank.api.model.TransactionResponseApiModel;
import com.mybank.transactionservice.model.OnlinePaymentResponse;
import com.mybank.transactionservice.model.TransactionResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResponseMapper {

    AccountTotalResponseApiModel toApiModel(AccountTotalResponse model);

    TransactionResponseApiModel toApiModel(TransactionResponse model);

    OnlinePaymentResponseApiModel toApiModel(OnlinePaymentResponse model);
}
