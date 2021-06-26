package com.coinme.coinmedemo.api;

import com.coinme.coinmedemo.model.Customer;
import com.coinme.coinmedemo.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/customer")
@RestController
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public void addCustomer(@RequestBody Customer customer){
        customerService.addCustomer(customer);
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping(path = "{id}")
    public Customer getCustomerById(@PathVariable("id") int id) {
        return customerService.getCustomerById(id)
                .orElse(null);
    }

    @DeleteMapping(path = "{id}")
    public void deleteCustomerById(@PathVariable("id") int id) {
        customerService.deleteCustomer(id);
    }

    @PutMapping(path = "{id}")
    public void updateCustomer(@PathVariable("id") int id, @RequestBody Customer customerToUpdate) {
        customerService.updateCustomer(id, customerToUpdate);
    }
}
