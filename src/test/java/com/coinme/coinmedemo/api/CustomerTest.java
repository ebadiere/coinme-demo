package com.coinme.coinmedemo.api;

import com.coinme.coinmedemo.model.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@SpringBootTest
public class CustomerTest {

    private static int port = 8080;

    @Autowired
    private static TestRestTemplate restTemplate;

    private static ArrayList<JSONObject> customers = new ArrayList<>();
    private static HttpHeaders headers = new HttpHeaders();

    private final ObjectMapper objectMapper = new ObjectMapper();

    final private static String baseUrl = "http://localhost:" + port + "/api/v1/customer/";

    public CustomerTest() throws URISyntaxException {
    }

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
    public void allRequiredCustomersTest() throws Exception {

        loadCustomers();

        URI uri = new URI(baseUrl);

        ResponseEntity<Customer[]> response = restTemplate.getForEntity(uri, Customer[].class);
        Assert.assertEquals(200, response.getStatusCodeValue());

        Customer[] customers = response.getBody();

        // In memory Fake DB Customer Data Access Serice will hold 4 when this is run on initial startup
        Assert.assertEquals(customers.length, 4);

        Assert.assertEquals(customers[0].getId(), 1);
        Assert.assertEquals(customers[0].getName(), "Arisha Barron");

        Assert.assertEquals(customers[1].getId(), 2);
        Assert.assertEquals(customers[1].getName(), "Branden Gibson");

        Assert.assertEquals(customers[2].getId(), 3);
        Assert.assertEquals(customers[2].getName(), "Rhonda Church");

        Assert.assertEquals(customers[3].getId(), 4);
        Assert.assertEquals(customers[3].getName(),"Georgina Hazel");

    }

    @Test
    public void getCustomerByIdTest() throws Exception {

        loadCustomers();
        String idUrl = baseUrl+"2";
        URI uri = new URI(idUrl);

        ResponseEntity<Customer> response = restTemplate.getForEntity(uri, Customer.class);
        Assert.assertEquals(200, response.getStatusCodeValue());

        Customer customer = response.getBody();

        Assert.assertEquals(customer.getName(), "Branden Gibson");
    }

    @Test
    public void updateCustomerTest() throws Exception {

        loadCustomers();
        String idUrl = baseUrl+"2";
        URI uri = new URI(idUrl);

        JSONObject customerJsonObject = new JSONObject();
        customerJsonObject.put("id", 2);
        customerJsonObject.put("name", "Branden Smith");

        HttpEntity<String> request = new HttpEntity<String>(customerJsonObject.toString(), headers);

        restTemplate.put(uri, request);

        ResponseEntity<Customer> response = restTemplate.getForEntity(uri, Customer.class);
        Assert.assertEquals(200, response.getStatusCodeValue());

        Customer customer = response.getBody();

        Assert.assertEquals(customer.getName(), "Branden Smith");

        // Set back
        customerJsonObject.put("id", 2);
        customerJsonObject.put("name", "Branden Gibson");

        request = new HttpEntity<String>(customerJsonObject.toString(), headers);

        restTemplate.put(uri, request);

        response = restTemplate.getForEntity(uri, Customer.class);
        Assert.assertEquals(200, response.getStatusCodeValue());

        customer = response.getBody();

        Assert.assertEquals(customer.getName(), "Branden Gibson");

    }

    private static void loadCustomers() throws URISyntaxException {

        URI uri = new URI(baseUrl);
        ResponseEntity<Customer[]> response = restTemplate.getForEntity(uri, Customer[].class);
        Assert.assertEquals(200, response.getStatusCodeValue());

        Customer[] customersLoaded = response.getBody();

        if(customersLoaded.length == 0){
            for(JSONObject customer : customers){
                HttpEntity<String> request = new HttpEntity<String>(customer.toString(), headers);
                ResponseEntity resp = restTemplate.postForEntity(uri, request, String.class);

                Assert.assertEquals(resp.getStatusCodeValue(), 200);
            }
        }
    }
}
