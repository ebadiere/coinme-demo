package com.coinme.coinmedemo.api;

import com.coinme.coinmedemo.model.Account;
import com.coinme.coinmedemo.model.Customer;
import com.coinme.coinmedemo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/api/v1/account")
@RestController
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Account> createAccount(@RequestBody Account account){
        Account account1 = accountService.createAccount(account.getCustomerNumber(), account.getDeposit());
        return new ResponseEntity<>(account1, HttpStatus.CREATED);
    }

    @PutMapping(path = "{accountNumber}")
    public void depositAccount(@PathVariable("accountNumber") UUID accountNumber, @RequestBody Float deposit){
        accountService.deposit(accountNumber, deposit);
    }

    @GetMapping(path = "{accountNumber}")
    public Float getAccountBalance(@PathVariable("accountNumber") UUID accountNumber){
        return accountService.getBalance(accountNumber);
    }
}
