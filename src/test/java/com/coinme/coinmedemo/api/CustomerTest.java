package com.coinme.coinmedemo.api;

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

@SpringBootTest
public class CustomerTest {

    private static int port = 8080;

    @Autowired
    private static TestRestTemplate restTemplate;

    private static ArrayList<JSONObject> customers = new ArrayList<>();
    private static HttpHeaders headers = new HttpHeaders();

    final private static String baseUrl = "http://localhost:" + port + "/api/v1/customer/";

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

        // In memory Fake DB Customer Data Access Service will hold 4 on initial startup
        Assert.assertEquals(customers.length, 4);

        for(Customer customer : customers){
            if (customer.getId() == 2){
                Assert.assertEquals(customer.getName(), "Branden Gibson");
            }
            int id = customer.getId();
            switch(id){
                case 1: Assert.assertEquals(customer.getName(), "Arisha Barron");
                        break;
                case 2: Assert.assertEquals(customer.getName(), "Branden Gibson");
                        break;
                case 3: Assert.assertEquals(customer.getName(), "Rhonda Church");
                        break;
                case 4: Assert.assertEquals(customer.getName(),"Georgina Hazel");
                        break;
            }
        }

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

    @Test
    public void deleteCustomerTest() throws Exception {
        loadCustomers();
        String idUrl = baseUrl+"2";
        URI uri = new URI(idUrl);

        restTemplate.delete(uri);

        uri = new URI(baseUrl);

        ResponseEntity<Customer[]> response = restTemplate.getForEntity(uri, Customer[].class);
        Assert.assertEquals(200, response.getStatusCodeValue());

        Customer[] customers = response.getBody();

        Assert.assertEquals(customers.length, 3);

        // Set back
        JSONObject customerJsonObject = new JSONObject();
        customerJsonObject.put("id", 2);
        customerJsonObject.put("name", "Branden Gibson");

        HttpEntity<String> request = new HttpEntity<String>(customerJsonObject.toString(), headers);
        ResponseEntity resp = restTemplate.postForEntity(uri, request, String.class);

        Assert.assertEquals(resp.getStatusCodeValue(), 200);

        response = restTemplate.getForEntity(uri, Customer[].class);
        Assert.assertEquals(200, response.getStatusCodeValue());

        customers = response.getBody();

        for(Customer customer : customers){
            if (customer.getId() == 2){
                Assert.assertEquals(customer.getName(), "Branden Gibson");
            }
        }

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
