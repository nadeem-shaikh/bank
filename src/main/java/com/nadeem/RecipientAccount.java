package com.nadeem;
public class RecipientAccount {

    private String accountId;
    private Double credit;

    public RecipientAccount(String accountId, Double credit) {
        this.accountId = accountId;
        this.credit = credit;
    }


    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }



    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }
    
}
