package com.coinme.coinmedemo.dao;

import com.coinme.coinmedemo.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {

    int insertCustomer(Customer customer);

    List<Customer> selectAllCustomers();

    Optional<Customer> selectCustomerById(int id);

    int deleteCustomerById(int id);

    int updateCustomerById(int id, Customer customer);
}
