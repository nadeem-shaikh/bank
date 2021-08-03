package com.nadeem;

import org.junit.Test;

/**
 * Unit test for Bank Application.
 */
public class BankTest extends Bank
{
    /**
     * Test Case to demo scenario  - If there are not enough money in total in all closed accounts to fulfill total credit in recipients - an error must be raised: “not enough
     * funds for rebalance“.
    */
    @Test
    public void testNotEnoughMoneyInClosingAccounts()
    {
        System.out.println("****************************************************************");

        String closingAccounts= "[{accountId:acc1,amount:200.0},{accountId:acc2,amount:100.0}]";
        String recipientAccounts= "[{accountId:rec1,credit:200.0},{accountId:rec2,credit:400.0}]";

        String [] args = {closingAccounts,recipientAccounts};
        main(args);
    }
    /**
     *  Test Case such to demonstrate error - A transfer cannot have a zero value, such transaction will not be accepted and will cause an error.
     */
    @Test
    public void testTransferHasZero()
    {

        System.out.println("****************************************************************");

        String closingAccounts= "[{accountId:acc1,amount:1000.0},{accountId:acc2,amount:200.0}]";
        String recipientAccounts= "[{accountId:rec1,credit:0.0},{accountId:rec2,credit:200.0}]";

        String [] args = {closingAccounts,recipientAccounts};
        main(args);
    }

    /**
     *  Test Case such to demonstrate error - A transfer cannot have a negative value, such transaction will not be accepted and will cause an error.
     */
    @Test
    public void testTransferHasNegativeValue()
    {
        System.out.println("****************************************************************");

        String closingAccounts= "[{accountId:acc1,amount:1000.0},{accountId:acc2,amount:200.0}]";
        String recipientAccounts= "[{accountId:rec1,credit:-1.0},{accountId:rec2,credit:200.0}]";

        String [] args = {closingAccounts,recipientAccounts};
        main(args);
    }

    @Test
    public void testTransferSucessWithoutOperationFee()
    {
        System.out.println("****************************************************************");

        String closingAccounts= "[{accountId:acc1,amount:900.0}]";
        String recipientAccounts= "[{accountId:rec1,credit: 500.0},{accountId:rec2,credit:400.0}]";

        String [] args = {closingAccounts,recipientAccounts};
        main(args);
    }

    @Test
    public void testTransferSucessWithOperationFee()
    {
        System.out.println("****************************************************************");

        String closingAccounts= "[{accountId:acc2,amount:1000.0}]";
        String recipientAccounts= "[{accountId:rec1,credit: 500.0},{accountId:rec2,credit:400.0}]";

        String [] args = {closingAccounts,recipientAccounts};
        main(args);
    }

    @Test
    public void testTransferSucessWithOperationFeeWithTwoClosingAccounts()
    {
        System.out.println("****************************************************************");

        String closingAccounts= "[{accountId:acc1,amount:500.0},{accountId:acc2,amount:500.0}]";
        String recipientAccounts= "[{accountId:rec1,credit: 400.0}]";

        String [] args = {closingAccounts,recipientAccounts};
        main(args);
    }

    @Test
    public void testTransferSucessWithNotEnoughOperationFee()
    {
        System.out.println("****************************************************************");

        String closingAccounts= "[{accountId:acc1,amount:405.0}]";
        String recipientAccounts= "[{accountId:rec1,credit: 400.0}]";

        String [] args = {closingAccounts,recipientAccounts};
        main(args);
    }

}
