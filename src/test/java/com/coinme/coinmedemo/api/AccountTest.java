package com.coinme.coinmedemo.api;

import com.coinme.coinmedemo.model.Account;
import com.coinme.coinmedemo.model.Customer;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
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
    public void createANewBankAccountForCustomerWithInitialDeposit() throws Exception{
        loadCustomers();
        String idUrl = customerUrl +"2";
        URI uri = new URI(idUrl);

        ResponseEntity<Customer> response = restTemplate.getForEntity(uri, Customer.class);
        Assertions.assertEquals(200, response.getStatusCodeValue());

        JSONObject accountJsonObject = new JSONObject();
        accountJsonObject.put("customerNumber", 2);
        accountJsonObject.put("deposit", 100.00);

        uri = new URI(accountUrl);

        HttpEntity<String> request = new HttpEntity<>(accountJsonObject.toString(), headers);
        ResponseEntity<Account> resp = restTemplate.postForEntity(uri, request, Account.class);

        Assertions.assertEquals(resp.getStatusCodeValue(), 201);
        Account account = resp.getBody();

        assert account != null;
        Assertions.assertEquals(account.getBalance(), 100.00);

    }

    @Test
    public void aSingleCustomerMayHaveMultipleBankAccounts() throws Exception{

        loadCustomers();
        String idUrl = customerUrl +"3";
        URI uri = new URI(idUrl);

        ResponseEntity<Customer> response = restTemplate.getForEntity(uri, Customer.class);
        Assertions.assertEquals(200, response.getStatusCodeValue());

        JSONObject accountJsonObject = new JSONObject();
        accountJsonObject.put("customerNumber", 3);
        accountJsonObject.put("deposit", 100.00);

        uri = new URI(accountUrl);

        HttpEntity<String> request = new HttpEntity<>(accountJsonObject.toString(), headers);
        ResponseEntity<Account> resp = restTemplate.postForEntity(uri, request, Account.class);

        Assertions.assertEquals(resp.getStatusCodeValue(), 201);
        Account account = resp.getBody();

        assert account != null;
        Assertions.assertEquals(account.getBalance(), 100.00);

        // Now create a second account for this customer

        accountJsonObject = new JSONObject();
        accountJsonObject.put("customerNumber", 3);
        accountJsonObject.put("deposit", 200.00);

        uri = new URI(accountUrl);

        request = new HttpEntity<>(accountJsonObject.toString(), headers);
        resp = restTemplate.postForEntity(uri, request, Account.class);

        Assertions.assertEquals(resp.getStatusCodeValue(), 201);
        account = resp.getBody();

        assert account != null;
        Assertions.assertEquals(account.getBalance(), 200.00);

    }

    private static void loadCustomers() throws URISyntaxException {

        URI uri = new URI(customerUrl);
        ResponseEntity<Customer[]> response = restTemplate.getForEntity(uri, Customer[].class);
        Assertions.assertEquals(200, response.getStatusCodeValue());

        Customer[] customersLoaded = response.getBody();

        assert customersLoaded != null;
        if (customersLoaded.length == 0) {
            for (JSONObject customer : customers) {
                HttpEntity<String> request = new HttpEntity<>(customer.toString(), headers);
                ResponseEntity<String> resp = restTemplate.postForEntity(uri, request, String.class);

                Assertions.assertEquals(resp.getStatusCodeValue(), 200);
            }
        }
    }
}
