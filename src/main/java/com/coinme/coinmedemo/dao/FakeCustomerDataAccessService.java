package com.coinme.coinmedemo.dao;

import com.coinme.coinmedemo.model.Customer;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("fakeDao")
public class FakeCustomerDataAccessService implements CustomerDao {

    private static List<Customer> DB = new ArrayList<>();

    @Override
    public int insertCustomer(Customer customer) {
        DB.add(new Customer(customer.getId(), customer.getName()));
        return 1;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return DB;
    }

    @Override
    public Optional<Customer> selectCustomerById(int id) {
        return DB.stream()
                .filter(customer -> customer.getId() == id)
                .findFirst();
    }

    @Override
    public int deleteCustomerById(int id) {
        Optional<Customer> customerMaybe = selectCustomerById(id);
        if(customerMaybe.isEmpty()) {
            return 0;
        }
        DB.remove(customerMaybe.get());
        return 1;
    }

    @Override
    public int updateCustomerById(int id, Customer customer) {
        return selectCustomerById(id)
                .map(c -> {
                    int indexOfCustomerToUpdate = DB.indexOf(c);
                    if (indexOfCustomerToUpdate >= 0) {
                        DB.set(indexOfCustomerToUpdate, new Customer(id, customer.getName()));
                        return 1;
                    }
                    return 0;
                })
                .orElse(0);
    }

}
