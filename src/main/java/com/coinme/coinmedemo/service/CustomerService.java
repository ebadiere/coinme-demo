package com.coinme.coinmedemo.service;

import com.coinme.coinmedemo.dao.CustomerDao;
import com.coinme.coinmedemo.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerDao customerDao;

    @Autowired
    public CustomerService(@Qualifier("fakeDao") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public int addCustomer(Customer customer){
        return customerDao.insertCustomer(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerDao.selectAllCustomers();
    }

    public Optional<Customer> getCustomerById(int id){
        return customerDao.selectCustomerById(id);
    }

    public int deleteCustomer(int id){
        return customerDao.deleteCustomerById(id);
    }

    public int updateCustomer(int id, Customer newCustomer){
        return customerDao.updateCustomerById(id, newCustomer);
    }

}
