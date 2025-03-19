package com.mybank.mybankapi.integration;

import com.mybank.api.model.AccountTotalRequestApiModel;
import com.mybank.api.model.CurrencyCodeApiModel;
import io.restassured.http.ContentType;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;

import java.io.IOException;
import java.io.InputStreamReader;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@EnableWireMock(@ConfigureWireMock(port = 8081))
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AccountApiIntegrationTest {

    @Test
    void testGetBankAccountTotal_status200() throws IOException, JSONException {
        String expectedResult = readString("get-bank-account-total.json");

        AccountTotalRequestApiModel request = AccountTotalRequestApiModel.builder()
                .iban("EE251298943438713614")
                .currencyCode(CurrencyCodeApiModel.EUR)
                .build();

        var response = given().contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/accounts/balances/total");

        var actualBody = response.body();

        JSONAssert.assertEquals(expectedResult, actualBody.asString(), JSONCompareMode.LENIENT);
    }

    @Test
    void testGetBankAccountTotal_status422() throws IOException, JSONException {
        AccountTotalRequestApiModel request = AccountTotalRequestApiModel.builder()
                .iban("EE251298943438713615")
                .currencyCode(CurrencyCodeApiModel.EUR)
                .build();

//        var response = given().contentType(ContentType.JSON)
//                .param()
//                .when()
//                .get("/accounts/balances/total");

        given().contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/accounts/balances/total")
                .then()
                .assertThat()
                .statusCode(422);
    }

    private static String readString(String classpath) throws IOException {
        return FileCopyUtils.copyToString(new InputStreamReader(new ClassPathResource("__files/" + classpath)
                .getInputStream()));
    }
}