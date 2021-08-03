# Building a transaction

**Intro**: we are creating a software solution for a bank and you are being commissioned to build one function out of the entire system, but this function will construct an internal transaction object, which moves the funds when some accounts are getting closed.

**Requirements**: from times to times the bank will want to run a big internal operation which closes multiple reserve accounts and moves all the
funds into multiple recipient accounts according to the corresponding credit, and the task of the function is to detail how exactly the funds will move between the accounts and also calculate the cost of the operation itself.

**Tasks**: implement function newRebalancingTx(closingAccounts, recipientAccounts). Where closingAccounts is an array of objects like { accountId: string, amount: number }, recipientAccounts is an array of objects like { accountId: string, credit: number }. And the function is supposed to return an object of this format:

{
transfers: [
[fromAccountId, toAccountId, value],
],
operationalFee: number,
}

The transfers array must contain a listing of how the money must be moved from all closed accounts into all new accounts. For example, if we have one closing account with ID acc1 and amount 1000 and two receipients with IDs rec1 and rec2 and credit values 500 and 400 - the first two elements in the transfers array will be: [acc1, rec1, 500], [acc1, rec2, 400].

A transfer cannot have a zero or negative value, such transaction will not be accepted and will cause an error.

If there are more money on a closed account than required for one receipient - then the remainder should go into the next receipient account.

If there are not enough money in total in all closed accounts to fulfill total credit in recipients - an error must be raised: "not enough funds for rebalance".

If there is more money available in closed accounts than total recipients credit - the remainder should be transferred from each account with a remainder to a new reserve which is added to transfers with account ID null. For example, if closed acc1 and acc2 with amounts 500 and 500 and one recipient rec1 with credit 400 - then transfers would be [[acc1, rec1, 400], [acc1, null, 100], [acc2, null, 500]].

Here's the catch, though, for any rebalancing transaction a special operational-fee must be included, which depends on the number of transfers and is calculated as transfers.length * 10. This amount must be subtracted from the remainder transferred into reserve. For example, if closed acc1 and acc2 with amounts 500 and 500 and one recipient rec1 with credit 400 - then the full correct response will be:

{
transfers: [[acc1, rec1, 400], [acc1, null, 100], [acc2, null, 480]],
operationalFee: 20,
}

So the end goal of the produced tx is to make sure every single bit of money on the input matches exactly to all the outputs plus the operational fee.

If total value of closed accounts is not enough to fulfill the total recipient credit AND the operational fee - an error must be raised: "not enough funds for rebalance".


# Project Setup

**Project has been tested using**:

MAC OS 11.0.1

JAVA 1.8

Apache Maven 3.6.0

**Project Usage**

$ mvn clean

$ mvn compile

This will execute all the test cases which contains the test input values

$ mvn test 

Command to pass the arguments via command line to test custom inputs

$ mvn compile exec:java -Dexec.mainClass="com.nadeem.Bank" -Dexec.args="'[{accountId:acc1,amount:200.0}]' '[{accountId:rec1,credit:100.0},{accountId:rec2,credit:100.0}]'"
