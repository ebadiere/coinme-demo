package com.coinme.coinmedemo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class Account {

    private final UUID accountNumber;
    private final int customerNumber;

    private Float balance;
    private Float deposit;


    public Account(@JsonProperty("accountNumber") UUID accountNumber,
                   @JsonProperty("customerNumber") int customerNumber,
                   @JsonProperty("balance") Float balance) {
        this.accountNumber = accountNumber;
        this.customerNumber = customerNumber;
        this.balance = balance;
    }

    public UUID getAccountNumber() {
        return accountNumber;
    }

    public int getCustomerNumber() {
        return customerNumber;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(Float balance){
        this.balance = balance;
    }

    public Float getDeposit() {
        return deposit;
    }

}
