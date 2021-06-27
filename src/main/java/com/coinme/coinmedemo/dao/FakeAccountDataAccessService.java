package com.coinme.coinmedemo.dao;

import com.coinme.coinmedemo.model.Account;
import com.coinme.coinmedemo.model.AccountTransfer;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("fakeAccountDao")
public class FakeAccountDataAccessService implements AccountDao{

    private static List<Account> DB = new ArrayList<>();

    @Override
    public Account createBankAccount(int customerId, Float balance) {
        UUID accountNumber = UUID.randomUUID();
        Account account = new Account(accountNumber, customerId, balance);
        DB.add(account);
        return account;
    }

    @Override
    public Account deposit(UUID accountNumber, Float deposit){
        Optional<Account>  accountMaybe = DB.stream()
                .filter(acct -> acct.getAccountNumber() == accountNumber)
                .findFirst();

        if(accountMaybe.isEmpty()){
            return null;
        }

        Account account = accountMaybe.get();
        Float balance = account.getBalance();
        balance = balance + deposit;
        account.setBalance(balance);

        return account;
    }

    @Override
    public Float getAccountBalance(UUID accountNumber) {
        Optional<Account>  accountMaybe = DB.stream()
                .filter(acct -> acct.getAccountNumber().equals(accountNumber))
                .findFirst();

        if(accountMaybe.isEmpty()){
            return Float.valueOf(0);
        }

        Account account = accountMaybe.get();

        return account.getBalance();
    }

    @Override
    public List<AccountTransfer> getTransferHistory(int accountNumber) {
        return null;
    }
}
