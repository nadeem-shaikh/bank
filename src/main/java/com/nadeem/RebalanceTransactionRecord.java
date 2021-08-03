package com.nadeem;

public class RebalanceTransactionRecord {

    String fromAccountId;
    String toAccountId;
    Double amount;
    Double operationalFee;

    
    public Double getOperationalFee() {
        return operationalFee;
    }
    public void setOperationalFee(Double operationalFee) {
        this.operationalFee = operationalFee;
    }
    public String getFromAccountId() {
        return fromAccountId;
    }
    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId = fromAccountId;
    }
    public String getToAccountId() {
        return toAccountId;
    }
    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }
    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    
}
