package com.mybank.mybankapi.mapper;

import com.mybank.accountservice.model.AccountTotalResponse;
import com.mybank.api.model.AccountTotalResponseApiModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountTotalResponseApiModel toApiModel(AccountTotalResponse model);
}
