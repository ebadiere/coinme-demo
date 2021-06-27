package com.coinme.coinmedemo.model;

public class AccountTransfer {

    private final int fromAccountNumber;
    private final String toAccountNumber;
    private final float amount;

    public AccountTransfer(int fromAccountNumber, String toAccountNumber, float amount) {
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.amount = amount;
    }

    public int getFromAccountNumber() {
        return fromAccountNumber;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }

    public float getAmount() {
        return amount;
    }
}
