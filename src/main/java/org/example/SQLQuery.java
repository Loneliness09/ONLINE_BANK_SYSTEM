package org.example;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SQLQuery {
    Connection conn = null;
    public SQLQuery() {
        try {
            conn = DatabaseConnection.getConnection();
        }
        catch (SQLException e) {
            System.out.println("Error adding customer: " + e.getMessage());
        }
    }

    public int addCustomer(String customerName, String email, String passwd) {
        String sql = "INSERT INTO Customers (customer_name, email, passwd) VALUES (?, ?, ?)";
        String selectSQL = "SELECT customer_id FROM Customers WHERE email = ?";
        int customerId = -1; // 默认值，表示未成功添加客户
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             PreparedStatement pstmtSelect = conn.prepareStatement(selectSQL)) {
            pstmt.setString(1, customerName);
            pstmt.setString(2, email);
            pstmt.setString(3, passwd);
            pstmt.executeUpdate();
            System.out.println("Customer added successfully.");
            pstmtSelect.setString(1, email);
            ResultSet rs = pstmtSelect.executeQuery();
            if (rs.next()) {
                customerId = rs.getInt("customer_id");
            }
        } catch (SQLException e) {
            System.out.println("Error adding customer: " + e.getMessage());
        }
        return customerId;
    }

    public int createAccount(int customerId, String passwd) {
        String sql = "INSERT INTO Accounts (customer_id, passwd) VALUES (?, ?)";
        int accountId = -1;
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, customerId);
            pstmt.setString(2, passwd);
            pstmt.executeUpdate();
            System.out.println("Account created successfully.");
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                accountId = generatedKeys.getInt(1); // 获取第一个生成的键，即账户ID
            }
        } catch (SQLException e) {
            System.out.println("Error creating account: " + e.getMessage());
        }
        return accountId;
    }

    public boolean deleteAccount(int accountId) {
        System.out.println(getAccountBalance(accountId).toString());
        if (Objects.equals(Double.valueOf(getAccountBalance(accountId).toString()), 0.0)) {
            if (deleteAccountTransactions(accountId)) System.out.println("Transactions deleted.");
            // SQL 语句用于删除账户
            String deleteAccountSql = "DELETE FROM Accounts WHERE account_id = ?";
            // SQL 语句用于检查账户是否有交易记录
            String checkTransactionsSql = "SELECT COUNT(*) FROM Transactions WHERE account_id = ?";

            try (PreparedStatement checkStmt = conn.prepareStatement(checkTransactionsSql);
                 PreparedStatement deleteStmt = conn.prepareStatement(deleteAccountSql)) {

                // 设置参数并检查账户是否有交易记录
                checkStmt.setInt(1, accountId);
                int transactionCount = 0;
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        transactionCount = rs.getInt(1);
                    }
                }

                // 如果账户没有交易记录，则允许删除
                if (transactionCount == 0) {
                    // 设置参数并执行删除操作
                    deleteStmt.setInt(1, accountId);
                    int affectedRows = deleteStmt.executeUpdate();
                    if (affectedRows > 0) {
                        System.out.println("Account with ID " + accountId + " has been successfully deleted.");
                    } else {
                        System.out.println("No account found with ID " + accountId);
                        return false;
                    }
                } else {
                    System.out.println("Cannot delete account with ID " + accountId + " because it has associated transactions.");
                    return false;
                }
            } catch (SQLException e) {
                System.out.println("Error deleting account: " + e.getMessage());
                return false;
            }
            return true;
        } else {
            System.out.println("Cannot delete account with ID because it also has deposits.");
            return false;
        }
    }

    public void deposit(int accountId, double amount) {
        String sql = "UPDATE Accounts SET balance = balance + ? WHERE account_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, accountId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                recordTransaction(accountId, null, amount, "deposit");
                System.out.print("Deposit ");
                System.out.print(amount);
                System.out.println(" successful.");
            } else {
                System.out.println("Account not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error depositing: " + e.getMessage());
        }
    }

    public void withdraw(int accountId, double amount) {
        String sql = "UPDATE Accounts SET balance = balance - ? WHERE account_id = ? AND balance >= ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, accountId);
            pstmt.setDouble(3, amount);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                recordTransaction(accountId, null, amount, "withdraw");
                System.out.print("Withdrawal ");
                System.out.print(amount);
                System.out.println(" successful.");
            } else {
                System.out.println("Insufficient funds or account not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error withdrawing: " + e.getMessage());
        }
    }

    public boolean transfer(int sourceAccountId, int targetAccountId, double amount) {
        String sql = "UPDATE Accounts SET balance = balance - ? WHERE account_id = ? AND balance >= ?";
        try {
            conn.setAutoCommit(false); // Start transaction

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, sourceAccountId);
            pstmt.setDouble(3, amount);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Insufficient funds or source account not found.");
                return false;
            }

            sql = "UPDATE Accounts SET balance = balance + ? WHERE account_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, targetAccountId);
            affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Target account not found.");
                return false;
            }

            recordTransaction(sourceAccountId, targetAccountId, amount, "transfer");
            conn.commit(); // Commit transaction
            System.out.println("Transfer successful.");
        } catch (SQLException e) {
            System.out.println("Error transferring: " + e.getMessage());
            return false;
        }
        return true;
    }

    private void recordTransaction(int accountId, Integer targetAccountId, double amount, String transactionType) {
        String sql = "INSERT INTO Transactions (account_id, target_account_id, amount, transaction_type, transaction_date) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, accountId);
            if (targetAccountId != null) {
                pstmt.setInt(2, targetAccountId);
            } else {
                pstmt.setNull(2, java.sql.Types.INTEGER);
            }
            pstmt.setDouble(3, amount);
            pstmt.setString(4, transactionType);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error recording transaction: " + e.getMessage());
        }
    }

    public boolean deleteAccountTransactions(int accountId) {
        String deleteSQL = "DELETE FROM Transactions WHERE account_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            pstmt.setInt(1, accountId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0; // 如果有行受影响，则认为删除成功
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // 发生异常，删除失败
        }
    }

    // 查找客户对应的账户列表
    public List<Integer> getCustomerAccounts(int customerId) {
        List<Integer> accountList = new ArrayList<>();
        String sql = "SELECT account_id FROM Accounts WHERE customer_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                accountList.add(rs.getInt("account_id"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving customer accounts: " + e.getMessage());
        }
        return accountList;
    }

    // 查询账户余额
    public BigDecimal getAccountBalance(int accountId) {
        BigDecimal balance = new BigDecimal(0);
        String sql = "SELECT balance FROM Accounts WHERE account_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, accountId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                balance = rs.getBigDecimal("balance");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving account balance: " + e.getMessage());
        }
        return balance;
    }

    // 查询交易记录（包括交易类型）
    public List<TransactionRecord> getTransactionRecords(int accountId) {
        List<TransactionRecord> transactionList = new ArrayList<>();
        String sql = "SELECT * FROM Transactions WHERE account_id = ? OR target_account_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, accountId);
            pstmt.setInt(2, accountId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                TransactionRecord record = new TransactionRecord(
                        rs.getInt("transaction_id"),
                        rs.getInt("account_id"),
                        rs.getInt("target_account_id"),
                        rs.getBigDecimal("amount"),
                        rs.getString("transaction_type"),
                        rs.getTimestamp("transaction_date")
                );
                transactionList.add(record);
//                record.printInfo();
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving transaction records: " + e.getMessage());
        }
        return transactionList;
    }

    // 内部类用于表示交易记录
    public static class TransactionRecord {
        private int transactionId;
        private int accountId;
        private int targetAccountId;
        private BigDecimal amount;
        private String transactionType;
        private java.sql.Timestamp transactionDate;

        public TransactionRecord(int transactionId, int accountId, int targetAccountId, BigDecimal amount, String transactionType, java.sql.Timestamp transactionDate) {
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
            System.out.print( this.transactionDate);
            System.out.print("\n");
        }
    }

    // 确保在不需要时关闭数据库连接
    public void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Error closing database connection: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SQLQuery sqlQuery = new SQLQuery();

        // 测试添加客户
        int customerId = sqlQuery.addCustomer("John Doe", "abcaayytiar@qq.com", "123456");
        System.out.println("Added customer with ID: " + customerId);

        // 测试创建账户
        int accountId = sqlQuery.createAccount(customerId, "123456");
        System.out.println("Created account with ID: " + accountId);

        // 测试存款
        sqlQuery.deposit(accountId, 500.0);

        BigDecimal balance = sqlQuery.getAccountBalance(accountId);
        System.out.println("Account balance: " + balance);

        // 测试取款
        sqlQuery.withdraw(accountId, 200.0);

        // 测试转账
        int targetAccountId = sqlQuery.createAccount(customerId, "222222");
        boolean transferSuccess = sqlQuery.transfer(accountId, targetAccountId, 300.0);
        if (transferSuccess) System.out.println("Transfer amount: " + "300.00" + " success.");

        // 测试查找客户对应的账户列表
        List<Integer> customerAccounts = sqlQuery.getCustomerAccounts(customerId);
        System.out.println("Customer accounts: " + customerAccounts);

        // 测试查询账户余额
        balance = sqlQuery.getAccountBalance(accountId);
        System.out.println("Account balance: " + balance);

        // 测试查询交易记录
        List<TransactionRecord> transactionRecords = sqlQuery.getTransactionRecords(accountId);
        System.out.println("Transaction records for account " + accountId + ":");
        for (TransactionRecord record : transactionRecords) {
            record.printInfo();
        }

//        sqlQuery.deposit(accountId, 0);

        // 测试删除账户
        boolean deleteAccountSuccess = sqlQuery.deleteAccount(accountId);
        System.out.println("Account deletion successful: " + deleteAccountSuccess);

        // 关闭数据库连接
        sqlQuery.closeConnection();
    }
}
