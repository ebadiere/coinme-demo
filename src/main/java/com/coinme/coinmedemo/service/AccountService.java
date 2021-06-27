package com.coinme.coinmedemo.service;

import com.coinme.coinmedemo.dao.AccountDao;
import com.coinme.coinmedemo.model.Account;
import com.coinme.coinmedemo.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.awt.geom.FlatteningPathIterator;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountDao accountDao;

    @Autowired
    public AccountService(@Qualifier("fakeAccountDao") AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public Account createAccount(int customerId, Float balance){
        Account account = accountDao.createBankAccount(customerId, balance);
        return account;
    }

    public Float getBalance(UUID accountNumber){
        return accountDao.getAccountBalance(accountNumber);
    }

    public Account deposit(UUID accountNumber, Float deposit){
        return accountDao.deposit(accountNumber, deposit);
    }
}
