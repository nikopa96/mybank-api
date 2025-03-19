package com.mybank.mybankapi.integration;

import com.mybank.api.model.AccountTotalRequestApiModel;
import com.mybank.api.model.CurrencyCodeApiModel;
import com.mybank.api.model.OnlinePaymentRequestApiModel;
import com.mybank.api.model.TransactionRequestApiModel;
import io.restassured.http.ContentType;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;

import java.io.IOException;
import java.io.InputStreamReader;

import static io.restassured.RestAssured.given;

@EnableWireMock({
        @ConfigureWireMock(name = "account-service", port = 8081),
        @ConfigureWireMock(name = "transaction-service", port = 8082)
})
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class ApiIntegrationTest {

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

//    @Test
//    void testGetBankAccountTotal_status422() throws IOException, JSONException {
//        AccountTotalRequestApiModel request = AccountTotalRequestApiModel.builder()
//                .iban("EE251298943438713615")
//                .currencyCode(CurrencyCodeApiModel.EUR)
//                .build();
//
////        var response = given().contentType(ContentType.JSON)
////                .param()
////                .when()
////                .get("/accounts/balances/total");
//
//        given().contentType(ContentType.JSON)
//                .body(request)
//                .when()
//                .post("/accounts/balances/total")
//                .then()
//                .assertThat()
//                .statusCode(422);
//    }

    @Test
    void testDepositMoneyToBankAccount_status200() throws IOException, JSONException {
        String expectedResult = readString("transaction-deposit-200.json");

        TransactionRequestApiModel request = TransactionRequestApiModel
                .builder()
                .iban("EE251298943438713614")
                .holderName("ADAM SMITH")
                .amount(1234.56)
                .currencyCode(CurrencyCodeApiModel.USD)
                .build();

        var response = given().contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/transactions/deposit");

        var actualBody = response.body();

        JSONAssert.assertEquals(expectedResult, actualBody.asString(), JSONCompareMode.LENIENT);
    }

    @Test
    void testDepositMoneyToBankAccount_status422() throws IOException, JSONException {
        String expectedResult = readString("transaction-deposit-422.json");

        TransactionRequestApiModel request = TransactionRequestApiModel
                .builder()
                .iban("EE251298943438713614")
                .holderName("ADAM SMITH")
                .amount(1234000.56)
                .currencyCode(CurrencyCodeApiModel.USD)
                .build();

        var response = given().contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/transactions/deposit");

        var actualBody = response.body();

        JSONAssert.assertEquals(expectedResult, actualBody.asString(), JSONCompareMode.LENIENT);
    }

    @Test
    void testDebitMoneyToBankAccount_status200() throws IOException, JSONException {
        String expectedResult = readString("transaction-debit-200.json");

        TransactionRequestApiModel request = TransactionRequestApiModel
                .builder()
                .iban("EE251298943438713614")
                .holderName("ADAM SMITH")
                .amount(689.56)
                .currencyCode(CurrencyCodeApiModel.USD)
                .build();

        var response = given().contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/transactions/debit");

        var actualBody = response.body();

        JSONAssert.assertEquals(expectedResult, actualBody.asString(), JSONCompareMode.LENIENT);
    }

    @Test
    void testDebitMoneyToBankAccount_status422() throws IOException, JSONException {
        String expectedResult = readString("transaction-debit-422.json");

        TransactionRequestApiModel request = TransactionRequestApiModel
                .builder()
                .iban("EE251298943438713614")
                .holderName("ADAM SMITH")
                .amount(50000000.56)
                .currencyCode(CurrencyCodeApiModel.USD)
                .build();

        var response = given().contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/transactions/debit");

        var actualBody = response.body();

        JSONAssert.assertEquals(expectedResult, actualBody.asString(), JSONCompareMode.LENIENT);
    }

    @Test
    void testMakeOnlinePayment_status200() throws IOException, JSONException {
        String expectedResult = readString("transaction-onlinepayment-200.json");

        TransactionRequestApiModel transactionInfo = TransactionRequestApiModel
                .builder()
                .iban("EE251298943438713614")
                .holderName("JOHN DOE")
                .amount(555.56)
                .currencyCode(CurrencyCodeApiModel.USD)
                .build();

        OnlinePaymentRequestApiModel request = OnlinePaymentRequestApiModel
                .builder()
                .transactionInfo(transactionInfo)
                .callbackURL("https://httpstat.us/200")
                .build();

        var response = given().contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/transactions/onlinepayment");

        var actualBody = response.body();

        JSONAssert.assertEquals(expectedResult, actualBody.asString(), JSONCompareMode.LENIENT);
    }

    @Test
    void testMakeOnlinePayment_status422() throws IOException, JSONException {
        String expectedResult = readString("transaction-onlinepayment-422.json");

        TransactionRequestApiModel transactionInfo = TransactionRequestApiModel
                .builder()
                .iban("EE251298943438713614")
                .holderName("JOHN DOE")
                .amount(1000.00)
                .currencyCode(CurrencyCodeApiModel.USD)
                .build();

        OnlinePaymentRequestApiModel request = OnlinePaymentRequestApiModel
                .builder()
                .transactionInfo(transactionInfo)
                .callbackURL("https://httpstat.us/200")
                .build();

        var response = given().contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/transactions/onlinepayment");

        var actualBody = response.body();

        JSONAssert.assertEquals(expectedResult, actualBody.asString(), JSONCompareMode.LENIENT);
    }

    private static String readString(String classpath) throws IOException {
        return FileCopyUtils.copyToString(new InputStreamReader(new ClassPathResource("__files/" + classpath)
                .getInputStream()));
    }
}