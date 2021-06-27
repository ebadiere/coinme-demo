package com.coinme.coinmedemo.dao;

import com.coinme.coinmedemo.model.Account;
import com.coinme.coinmedemo.model.AccountTransfer;
import com.coinme.coinmedemo.model.Customer;

import java.util.List;
import java.util.UUID;

public interface AccountDao {

    Account createBankAccount(int customerId, Float balance);

    Float getAccountBalance(UUID accountNumber);

    Account deposit(UUID accountNumber, Float deposit);

    List<AccountTransfer> getTransferHistory(int accountNumber);

}
