package com.nadeem;

public class RebalanceStatement{

    public RebalanceStatement(String[][] transfers, Double operationalFee ) {
        this.transfers = transfers;
        this.operationalFee = operationalFee;
    }

    String [] [] transfers;
    Double operationalFee; 
    
}
