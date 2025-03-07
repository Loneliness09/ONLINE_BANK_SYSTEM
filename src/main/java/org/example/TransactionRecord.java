package org.example;

import java.math.BigDecimal;
import java.sql.Timestamp;

// 内部类用于表示交易记录
public class TransactionRecord {
    private int transactionId;
    private int accountId;
    private int targetAccountId;
    private BigDecimal amount;
    private String transactionType;
    private Timestamp transactionDate;

    public TransactionRecord(int transactionId, int accountId, int targetAccountId, BigDecimal amount, String transactionType, Timestamp transactionDate) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.targetAccountId = targetAccountId;
        this.amount = amount;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
    }

    public void printInfo() {
        System.out.print("ID: ");
        System.out.print(this.transactionId);
        System.out.print("\t");

        System.out.print("ACCOUNT_ID: ");
        System.out.print(this.accountId);
        System.out.print("\t");

        System.out.print("TARGET_ID: ");
        System.out.print(this.targetAccountId);
        System.out.print("\t");

        System.out.print("AMOUNT: ");
        System.out.print(this.amount);
        System.out.print("\t");

        System.out.print("TYPE: ");
        System.out.print(this.transactionType);
        System.out.print("\t");

        System.out.print("DATE: ");
        System.out.print(this.transactionDate);
        System.out.print("\n");
    }
}
