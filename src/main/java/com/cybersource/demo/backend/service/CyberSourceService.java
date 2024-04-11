package com.cybersource.demo.backend.service;

import com.cybersource.ws.client.Client;
import com.cybersource.ws.client.ClientException;
import com.cybersource.ws.client.FaultException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
public class CyberSourceService {

    private final static String PLACEHOLDER = "PLACEHOLDER";

    @Autowired
    @Qualifier("merchantProperties")
    private Properties merchantProperties;


    @Value("classpath:/templates/stepup-result.html")
    private Resource stepUpResultHtml;

    private String stepUpResultHtmlContent;

    @PostConstruct
    private void loadStepUpResultHtml() throws IOException {
        Reader reader = new InputStreamReader(stepUpResultHtml.getInputStream());
        stepUpResultHtmlContent = FileCopyUtils.copyToString(reader);
    }

    public String doAuth() throws FaultException, ClientException {

        // Transaction Data
        HashMap<String, String> requestMap = new HashMap<String, String>();
        requestMap.put("ccAuthService_run", "true");

        requestMap.put("merchantReferenceCode", String.valueOf(System.currentTimeMillis()));
        requestMap.put("billTo_firstName", "Lynne");
        requestMap.put("billTo_lastName", "LL");
        requestMap.put("billTo_street1", "1295 Charleston Road");
        requestMap.put("billTo_city", "Mountain View");
        requestMap.put("billTo_state", "CA");
        requestMap.put("billTo_postalCode", "94043");
        requestMap.put("billTo_country", "US");
        requestMap.put("billTo_email", "nobody@cybersource.com");
        requestMap.put("billTo_ipAddress", "10.7.7.7");
        requestMap.put("billTo_phoneNumber", "650-965-6000");
        requestMap.put("shipTo_firstName", "Jane");
        requestMap.put("shipTo_lastName", "Doe");
        requestMap.put("shipTo_street1", "100 Elm Street");
        requestMap.put("shipTo_city", "San Mateo");
        requestMap.put("shipTo_state", "CA");
        requestMap.put("shipTo_postalCode", "94401");
        requestMap.put("shipTo_country", "US");
        requestMap.put("card_accountNumber", "4111111111111111");
        requestMap.put("card_cvNumber", "123");
        requestMap.put("card_cardType", "001");
        requestMap.put("card_expirationMonth", "12");
        requestMap.put("card_expirationYear", "2025");
        requestMap.put("purchaseTotals_currency", "USD");
        requestMap.put("item_0_unitPrice", "12");
        requestMap.put("item_1_unitPrice", "56");
        //requestMap.put("merchantID", "your_merchant_id");

        Map<String, String> replyMap = Client.runTransaction(requestMap, merchantProperties);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonObject = gson.toJsonTree(replyMap).getAsJsonObject();
        return jsonObject.toString();
    }

    public String doSetup() throws FaultException, ClientException {
        return doSetup("4000000000002701", "2025", "12");
    }

    public String doSetup(String cardNumber, String year, String month) throws FaultException, ClientException {

        HashMap<String, String> requestMap = new HashMap<String, String>();
        requestMap.put("payerAuthSetupService_run", "true");

        requestMap.put("billTo_postalCode", "94043");
        requestMap.put("billTo_state", "CA");
        requestMap.put("card_accountNumber", cardNumber);
        requestMap.put("card_cardType", "001");
        requestMap.put("card_expirationMonth", month);
        requestMap.put("card_expirationYear", year);
        requestMap.put("billTo_city", "Mountain View");
        requestMap.put("billTo_country", "US");
        requestMap.put("billTo_email", "nobody@cybersource.com");
        requestMap.put("billTo_firstName", "Lynne");
        requestMap.put("billTo_lastName", "LL");
        requestMap.put("shipTo_street1", "100 Elm Street");
        requestMap.put("merchantReferenceCode", "0001");

        Map<String, String> replyMap = Client.runTransaction(requestMap, merchantProperties);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonObject = gson.toJsonTree(replyMap).getAsJsonObject();
        return jsonObject.toString();
    }

    public String doEnroll(String cardNumber, String referenceId) throws FaultException, ClientException {

        HashMap<String, String> requestMap = new HashMap<String, String>();
        requestMap.put("payerAuthEnrollService_run", "true");

        requestMap.put("card_accountNumber", cardNumber);
        requestMap.put("payerAuthEnrollService_referenceID", referenceId);
        requestMap.put("payerAuthEnrollService_returnURL", "http://localhost:8080/api/v1/cybersource");

        requestMap.put("billTo_country", "US");
        requestMap.put("billTo_email", "nobody@cybersource.com");
        requestMap.put("billTo_firstName", "Lynne");
        requestMap.put("billTo_lastName", "LL");
        requestMap.put("billTo_street1", "1295 Charleston Road");
        requestMap.put("billTo_city", "Mountain View");
        requestMap.put("billTo_state", "CA");
        requestMap.put("billTo_postalCode", "94043");
        requestMap.put("billTo_ipAddress", "10.7.7.7");
        requestMap.put("billTo_phoneNumber", "650-965-6000");
        requestMap.put("card_cvNumber", "123");
        requestMap.put("card_cardType", "001");
        requestMap.put("card_expirationMonth", "12");
        requestMap.put("card_expirationYear", "2025");
        requestMap.put("merchantReferenceCode", "0001");
        requestMap.put("purchaseTotals_currency", "USD");
        requestMap.put("purchaseTotals_grandTotalAmount", "666");


        Map<String, String> replyMap = Client.runTransaction(requestMap, merchantProperties);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonObject = gson.toJsonTree(replyMap).getAsJsonObject();
        return jsonObject.toString();
    }

    public String doValidate(String transactionId) throws FaultException, ClientException{

        HashMap<String, String> requestMap = new HashMap<String, String>();
        requestMap.put("payerAuthValidateService_authenticationTransactionID", transactionId);
        requestMap.put("payerAuthValidateService_run", "true");

        requestMap.put("card_cvNumber", "123");
        requestMap.put("card_cardType", "001");
        requestMap.put("card_expirationMonth", "12");
        requestMap.put("card_expirationYear", "2025");
        requestMap.put("merchantReferenceCode", "0001");
        requestMap.put("purchaseTotals_currency", "USD");
        requestMap.put("purchaseTotals_grandTotalAmount", "666");

        Map<String, String> replyMap = Client.runTransaction(requestMap, merchantProperties);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonObject = gson.toJsonTree(replyMap).getAsJsonObject();

        return stepUpResultHtmlContent.replace(PLACEHOLDER, jsonObject.toString());
    }
}
