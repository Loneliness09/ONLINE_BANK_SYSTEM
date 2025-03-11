package org.example;

import java.math.BigDecimal;
import java.sql.Timestamp;

// 内部类用于表示交易记录
public class TransactionRecord {
    private final int transactionId;
    private final int accountId;
    private final int targetAccountId;
    private final BigDecimal amount;
    private final String transactionType;
    private final Timestamp transactionDate;

    public TransactionRecord(int transactionId, int accountId, int targetAccountId, BigDecimal amount, String transactionType, Timestamp transactionDate) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.targetAccountId = targetAccountId;
        this.amount = amount;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
    }

    public void printInfo() {
        System.out.print("交易ID: ");
        System.out.print(this.transactionId);
        System.out.print("\t");

        System.out.print("账户ID: ");
        System.out.print(this.accountId);
        System.out.print("\t");

        System.out.print("对方账户ID: ");
        System.out.print(this.targetAccountId);
        System.out.print("\t");

        System.out.print("金额: ");
        System.out.print(this.amount);
        System.out.print("\t");

        System.out.print("类型: ");
        System.out.print(this.transactionType);
        System.out.print("\t");

        System.out.print("日期: ");
        System.out.print(this.transactionDate);
        System.out.print("\n");
    }

    @Override
    public String toString() {
        return "交易ID: " + transactionId +
                " 账户ID: " + accountId +
                " 对方账户ID: " + targetAccountId +
                " 金额: " + amount +
                " 类型: " + transactionType +
                " 日期: " + transactionDate;
    }
}
