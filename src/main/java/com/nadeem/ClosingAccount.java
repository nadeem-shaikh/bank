package com.nadeem;
public class ClosingAccount {

    private String accountId;
    private Double amount;

    public ClosingAccount(String accountId, Double amount) {
        this.accountId = accountId;
        this.amount = amount;
    }


    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
    
}
