package com.coinme.coinmedemo.api;

import com.coinme.coinmedemo.model.Account;
import com.coinme.coinmedemo.model.Customer;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.UUID;

@SpringBootTest
public class AccountTest {

    private static int port = 8080;

    @Autowired
    private static TestRestTemplate restTemplate;

    private static ArrayList<JSONObject> customers = new ArrayList<>();
    private static HttpHeaders headers = new HttpHeaders();

    final private static String customerUrl = "http://localhost:" + port + "/api/v1/customer/";
    final private static String accountUrl = "http://localhost:" + port + "/api/v1/account/";

    @BeforeAll
    public static void runBeforeAllTestMethods() throws JSONException {
        restTemplate = new TestRestTemplate();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String[] customerNames = {"Arisha Barron", "Branden Gibson", "Rhonda Church", "Georgina Hazel"};

        int customerId = 1;
        for(String customerName: customerNames){
            JSONObject customerJsonObject = new JSONObject();
            customerJsonObject.put("id", customerId);
            customerId++;
            customerJsonObject.put("name", customerName);
            customers.add(customerJsonObject);
        }
    }

    @Test
    public void createAccountWithInitialDepositAmount() throws Exception{
        loadCustomers();
        String idUrl = customerUrl +"2";
        URI uri = new URI(idUrl);

        ResponseEntity<Customer> response = restTemplate.getForEntity(uri, Customer.class);
        Assert.assertEquals(200, response.getStatusCodeValue());

        JSONObject accountJsonObject = new JSONObject();
        accountJsonObject.put("customerNumber", 2);
        accountJsonObject.put("deposit", 100.00);

        uri = new URI(accountUrl);

        HttpEntity<String> request = new HttpEntity<String>(accountJsonObject.toString(), headers);
        ResponseEntity<Account> resp = restTemplate.postForEntity(uri, request, Account.class);

        Assert.assertEquals(resp.getStatusCodeValue(), 200);
        Account account = resp.getBody();
        UUID accountNumber = account.getAccountNumber();

        uri = new URI(accountUrl + "/" + accountNumber);
//        ResponseEntity<Float> response = restTemplate.getForEntity(uri, Float.class);

    }

    private static void loadCustomers() throws URISyntaxException {

        URI uri = new URI(customerUrl);
        ResponseEntity<Customer[]> response = restTemplate.getForEntity(uri, Customer[].class);
        Assert.assertEquals(200, response.getStatusCodeValue());

        Customer[] customersLoaded = response.getBody();

        if (customersLoaded.length == 0) {
            for (JSONObject customer : customers) {
                HttpEntity<String> request = new HttpEntity<String>(customer.toString(), headers);
                ResponseEntity resp = restTemplate.postForEntity(uri, request, String.class);

                Assert.assertEquals(resp.getStatusCodeValue(), 200);
            }
        }
    }
}
