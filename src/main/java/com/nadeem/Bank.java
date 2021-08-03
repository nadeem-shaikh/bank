package com.nadeem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Intro: we are creating a software solution for a bank and you are being
 * commissioned to build one function out of the entire system, but this
 * function will construct an internal transaction object, which moves the funds
 * when some accounts are getting closed.
 * 
 * Requirements: from times to times the bank will want to run a big internal
 * operation which closes multiple reserve accounts and moves all th funds into
 * multiple recipient accounts according to the corresponding credit, and the
 * task of the function is to detail how exactly the funds wil move between the
 * accounts and also calculate the cost of the operation itself.
 */

public class Bank {

    public static void main(String[] args) {

        try {

            System.out.println(" \n Welcome to the Bank of Nadeem \n");
            Gson gson = new Gson();
            List<ClosingAccount> closingAccounts = gson.fromJson( args[0], new TypeToken<ArrayList<ClosingAccount>>(){}.getType());
            List<RecipientAccount> recipientAccounts = gson.fromJson( args[1], new TypeToken<ArrayList<RecipientAccount>>(){}.getType());
            System.out.println("closingAccounts  - " + new Gson().toJson(closingAccounts));
            System.out.println("recipientAccounts - " + new Gson().toJson(recipientAccounts));
            newRebalancingTx(closingAccounts, recipientAccounts);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * 
     * @param closingAccounts   - closingAccounts is an array of objects like {
     *                          accountId: string, amount: number }
     * @param recipientAccounts - recipientAccounts is an array of objects like {
     *                          accountId: string, credit: number }.
     * @throws Exception
     */
    private static void newRebalancingTx(List<ClosingAccount> closingAccounts,List<RecipientAccount> recipientAccounts)
            throws Exception {
        System.out.println(" \n **** Rebalancing Accounts **** \n ");
        try {
            String validationResponse = validateInputsForRebalance(closingAccounts, recipientAccounts);
            if (!validationResponse.equals("No Errors Found")) {
                System.out.println("\n " + validationResponse + "\n");
                return;
            }
            List<RebalanceTransactionRecord> rebalanceTransactionRecords = genRebalanceTransactionRecords( closingAccounts, recipientAccounts);
            int trSize = rebalanceTransactionRecords.size();
            String[][] transferRecords = new String[trSize][];
            for (int i = 0; i < trSize; i++) {
                transferRecords[i] = new String[] { rebalanceTransactionRecords.get(i).getFromAccountId(),
                        rebalanceTransactionRecords.get(i).getToAccountId(),
                        rebalanceTransactionRecords.get(i).getAmount().toString(), };
            }

            Double operationalFee = rebalanceTransactionRecords.get(trSize - 1).operationalFee;
            RebalanceStatement rs = new RebalanceStatement(transferRecords, operationalFee);
            System.out.println(new Gson().toJson(rs) + " \n \n");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static List<RebalanceTransactionRecord> genRebalanceTransactionRecords(List<ClosingAccount> closingAccounts,List<RecipientAccount> recipientAccounts) throws Exception {
        List<RebalanceTransactionRecord> rebalanceTransactionRecords = new ArrayList<RebalanceTransactionRecord>();
        try {


            int numberOfCAs = closingAccounts.size();
            for (int i = 0; i < numberOfCAs; i++) {
                Double closingAccountBalance = closingAccounts.get(i).getAmount();

                int numberOfRAs = recipientAccounts.size();
                for (int j = 0; j < numberOfRAs; j++) {
                    if (recipientAccounts.get(j).getCredit() != 0.0) {

                        RebalanceTransactionRecord rtr = new RebalanceTransactionRecord();
                        rtr.setFromAccountId(closingAccounts.get(i).getAccountId());
                        if (Double.compare(closingAccountBalance, recipientAccounts.get(j).getCredit()) > 0) {
                            rtr.setAmount(recipientAccounts.get(j).getCredit());
                            closingAccountBalance = closingAccountBalance - recipientAccounts.get(j).getCredit();
                            rtr.setToAccountId(recipientAccounts.get(j).getAccountId());
                            rebalanceTransactionRecords.add(rtr);
                            recipientAccounts.get(j).setCredit(0.0);
                        }

                        if (Double.compare(closingAccountBalance, recipientAccounts.get(j).getCredit()) == 0) {

                            rtr.setAmount(recipientAccounts.get(j).getCredit());
                            closingAccountBalance = closingAccountBalance - recipientAccounts.get(j).getCredit();
                            rtr.setToAccountId(recipientAccounts.get(j).getAccountId());
                            rebalanceTransactionRecords.add(rtr);
                            recipientAccounts.get(j).setCredit(0.0);
                        }
                    }
                }
                // IF Balance is left in the closing account after sending amounts to all the
                // receiving Accounts
                // Then remaining closing account balance is sent to the reserve
                if (closingAccountBalance > 0) {
                    RebalanceTransactionRecord rtr = new RebalanceTransactionRecord();
                    rtr.setFromAccountId(closingAccounts.get(i).getAccountId());
                    Double operationalFee = 0.0;
                    // An operational Fee is calculated only for the last transaction sent to
                    // reserve
                    if (i == (numberOfCAs - 1)) {
                        // Operational Fee is calculates as transfers.length * 10
                        operationalFee = rebalanceTransactionRecords.size() * 10.0;
                        rtr.setOperationalFee(operationalFee);
                    }

                    // Operaional Fee is deducted from the closing account balance before sending it
                    // to reserve
                    if (closingAccountBalance - operationalFee > 0) {
                        rtr.setAmount(closingAccountBalance - operationalFee);
                    } else
                        throw new Exception("not enough funds for rebalance");

                    // As all the amount from closing account is sent, set it to 0
                    closingAccountBalance = 0.0;
                    rebalanceTransactionRecords.add(rtr);
                }

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return rebalanceTransactionRecords;

    }

    /**
     * Validate the closingAccounts and recipientAccounts with conditions : 1. A
     * transfer cannot have a zero or negative value, such transaction will not be
     * accepted and will cause an error. 2. If there are not enough money in total
     * in all closed accounts to fulfill total credit in recipients - an error must
     * be raised: “not enough funds for rebalance“. If total value of closed
     * accounts is not enough to fulfill the total recipient credit AND the
     * operational fee - an error must be raised: “not enough funds for rebalance“.
     * 
     * @param closingAccounts
     * @param recipientAccounts
     * @return
     */
    private static String validateInputsForRebalance( List<ClosingAccount> closingAccounts,List<RecipientAccount> recipientAccounts) {
        String validationResponse = "No Errors Found";
        Double totalClosingAccountsAmount = 0.0;
        Double totalRecipientAccountsCredit = 0.0;

        try {
            totalClosingAccountsAmount = closingAccounts.stream().collect(Collectors.summingDouble(ca -> ca.getAmount()));
            totalRecipientAccountsCredit = recipientAccounts.stream().collect(Collectors.summingDouble(ra -> ra.getCredit()));

            // If the total amount in Closing Amounts is less than the sum of credits in Recipient Accouns
            // Then generate error
            if (Double.compare(totalClosingAccountsAmount, totalRecipientAccountsCredit) < 0)
            validationResponse = "not enough funds for rebalance";

            // Having a negative or 0 credit in any of the receipients will lead to trasnfer
            // having a zero or negative value
            // Validate if credit for the recipients if negative or zero
            if (recipientAccounts.stream().anyMatch(ra -> ra.getCredit() <= 0.0))
            {
                validationResponse = "Error in inputs. A transfer cannot have a zero or negative value";
                return validationResponse;
            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return validationResponse;
    }
}
