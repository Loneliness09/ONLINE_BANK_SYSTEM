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
            System.out.println("数据库连接成功.");
        }
        catch (SQLException e) {
            System.out.println("数据库连接失败: " + e.getMessage());
        }
    }

    public int login(String email, String passwd) {
        String sql = "SELECT customer_id FROM Customers WHERE email = ? AND passwd = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, passwd); // 在实际应用中，密码应该被加密并匹配存储的哈希值
            ResultSet rs = pstmt.executeQuery();
            // 如果查询有结果，则表示登录成功
            if(rs.next()) {
                System.out.println("用户登录成功.");
                return rs.getInt("customer_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        return 0;
    }

    public int accountLogin(int accountID, String passwd) {
        String sql = "SELECT account_id FROM Accounts WHERE account_id = ? AND passwd = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, Integer.toString(accountID));
            pstmt.setString(2, passwd); // 在实际应用中，密码应该被加密并匹配存储的哈希值
            ResultSet rs = pstmt.executeQuery();
            // 如果查询有结果，则表示登录成功
            if(rs.next()) {
                System.out.println("账户登录成功.");
                return rs.getInt("account_id");
            }
        } catch (SQLException e) {
            System.out.println("账户登录失败.");
            e.printStackTrace();
            return 0;
        }
        System.out.println("账户登录失败.");
        return 0;
    }

    public String getCustomerName(int customerID) {
        String sql = "SELECT customer_name FROM Customers WHERE customer_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, Integer.toString(customerID));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("customer_name");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public String getCustomerEmail(int customerID) {
        String sql = "SELECT email FROM Customers WHERE customer_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, Integer.toString(customerID));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("email");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public int addCustomer(String customerName, String email, String passwd) {
        String sql = "INSERT INTO Customers (customer_name, email, passwd) VALUES (?, ?, ?)";

        int customerId = 0; // 默认值，表示未成功添加客户
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, customerName);
            pstmt.setString(2, email);
            pstmt.setString(3, passwd);
            pstmt.executeUpdate();

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                System.out.println("用户注册成功.");
                customerId = generatedKeys.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("用户注册失败: " + e.getMessage());
        }
        return customerId;
    }

    public boolean deleteCustomer(int customerId) {
        String sql = "DELETE FROM Customers WHERE customer_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) System.out.println("用户注销成功.");
            else System.out.println("用户注销失败.");
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int createAccount(int customerId, String passwd) {
        String sql = "INSERT INTO Accounts (customer_id, passwd) VALUES (?, ?)";
        int accountId = 0;
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, customerId);
            pstmt.setString(2, passwd);
            pstmt.executeUpdate();

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                System.out.println("账户创建成功.");
                accountId = generatedKeys.getInt(1); // 获取第一个生成的键，即账户ID
            }
        } catch (SQLException e) {
            System.out.println("账户创建失败: " + e.getMessage());
        }
        return accountId;
    }

    public boolean deleteAccount(int accountId) {
        System.out.println(getAccountBalance(accountId).toString());
        if (Objects.equals(Double.valueOf(getAccountBalance(accountId).toString()), 0.0)) {
            if (deleteTransactions(accountId)) System.out.println("交易记录删除成功.");
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
                        System.out.println("ID为 " + accountId + " 的账户已注销.");
                    } else {
                        System.out.println("没有找到ID为 " + accountId + " 的账户");
                        return false;
                    }
                } else {
                    System.out.println("不可注销ID为 " + accountId + " 的账户, 因为它还有交易记录.");
                    return false;
                }
            } catch (SQLException e) {
                System.out.println("账户注销错误: " + e.getMessage());
                return false;
            }
            return true;
        } else {
            System.out.println("不可以注销ID为 " + accountId + " 的账户, 因为它还有存款.");
            return false;
        }
    }

    public boolean deposit(int accountId, BigDecimal amount) {
        String sql = "UPDATE Accounts SET balance = balance + ? WHERE account_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBigDecimal(1, amount);
            pstmt.setInt(2, accountId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                recordTransaction(accountId, null, amount, "deposit");
                System.out.print("存入 ");
                System.out.print(amount);
                System.out.println("元 成功.");
                return true;
            } else {
                System.out.println("账户不存在.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("存款失败: " + e.getMessage());
            return false;
        }
    }

    public boolean withdraw(int accountId, BigDecimal amount) {
        String sql = "UPDATE Accounts SET balance = balance - ? WHERE account_id = ? AND balance >= ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBigDecimal(1, amount);
            pstmt.setInt(2, accountId);
            pstmt.setBigDecimal(3, amount);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                recordTransaction(accountId, null, amount, "withdraw");
                System.out.print("取出 ");
                System.out.print(amount);
                System.out.println("元 成功.");
            } else {
                System.out.println("余额不足 或 账户不存在.");
            }
            return true;
        } catch (SQLException e) {
            System.out.println("取款失败: " + e.getMessage());
            return true;
        }
    }

    public boolean transfer(int sourceAccountId, int targetAccountId, BigDecimal amount) {
        String sql = "UPDATE Accounts SET balance = balance - ? WHERE account_id = ? AND balance >= ?";

        try {
            conn.setAutoCommit(false); // Start transaction

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setBigDecimal(1, amount);
            pstmt.setInt(2, sourceAccountId);
            pstmt.setBigDecimal(3, amount);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("余额不足 或 账户不存在.");
                return false;
            }

            sql = "UPDATE Accounts SET balance = balance + ? WHERE account_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setBigDecimal(1, amount);
            pstmt.setInt(2, targetAccountId);
            affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("对方账户不存在.");
                return false;
            }

            recordTransaction(sourceAccountId, targetAccountId, amount, "transfer");
            conn.commit(); // Commit transaction
            System.out.println("转账成功, 对方开户名: " + recordCustomerName(targetAccountId));
        } catch (SQLException e) {
            System.out.println("转账失败: " + e.getMessage());
            return false;
        }
        return true;
    }

    private void recordTransaction(int accountId, Integer targetAccountId, BigDecimal amount, String transactionType) {
        String sql = "INSERT INTO Transactions (account_id, target_account_id, amount, transaction_type, transaction_date) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, accountId);
            if (targetAccountId != null) {
                pstmt.setInt(2, targetAccountId);
            } else {
                pstmt.setNull(2, java.sql.Types.INTEGER);
            }
            pstmt.setBigDecimal(3, amount);
            pstmt.setString(4, transactionType);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("交易记录生成失败: " + e.getMessage());
        }
    }

    public boolean deleteTransactions(int accountId) {
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
            System.out.println("查询账户列表失败: " + e.getMessage());
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
            System.out.println("查询余额失败: " + e.getMessage());
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
            }
        } catch (SQLException e) {
            System.out.println("查询交易记录失败: " + e.getMessage());
        }
        return transactionList;
    }

    public String recordCustomerName(int accountID) {
        String sql = "SELECT c.customer_name " +
                "FROM Accounts a " +
                "JOIN Customers c ON a.customer_id = c.customer_id " +
                "WHERE a.account_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, accountID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("customer_name");
            }
        } catch (SQLException e) {
            System.out.println("查询开户名失败: " + e.getMessage());
        }
        return "";
    }

    // 确保在不需要时关闭数据库连接
    public void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("数据库连接关闭失败: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SQLQuery sqlQuery = new SQLQuery();

        // 测试添加客户
        int customerId = sqlQuery.addCustomer("John Doe", "abcaaddyy9tiar@qq.com", "123456");
        System.out.println("注册用户ID: " + customerId);

        // 测试创建账户
        int accountId = sqlQuery.createAccount(customerId, "123456");
        System.out.println("创建账户ID: " + accountId);

        // 测试存款
        sqlQuery.deposit(accountId, BigDecimal.valueOf(500.0));

        BigDecimal balance = sqlQuery.getAccountBalance(accountId);
        System.out.println("账户余额: " + balance);

        // 测试取款
        sqlQuery.withdraw(accountId, BigDecimal.valueOf(200.0));

        // 测试转账
        int targetAccountId = sqlQuery.createAccount(customerId, "222222");
        boolean transferSuccess = sqlQuery.transfer(accountId, targetAccountId, BigDecimal.valueOf(300.0));
        if (transferSuccess) System.out.println("转账: " + "300.00" + "元 成功.");

        // 测试查找客户对应的账户列表
        List<Integer> customerAccounts = sqlQuery.getCustomerAccounts(customerId);
        System.out.println("用户账户列表: " + customerAccounts);

        // 测试查询账户余额
        balance = sqlQuery.getAccountBalance(accountId);
        System.out.println("账户余额: " + balance);

        // 测试查询交易记录
        List<TransactionRecord> transactionRecords = sqlQuery.getTransactionRecords(accountId);
        System.out.println("账户ID " + accountId + " 交易记录:");
        for (TransactionRecord record : transactionRecords) {
            record.printInfo();
        }

//        sqlQuery.deposit(accountId, 0);

        // 测试删除账户
        boolean deleteAccountSuccess = sqlQuery.deleteAccount(accountId);
        System.out.println("账户注销: " + deleteAccountSuccess);

        // 关闭数据库连接
        sqlQuery.closeConnection();
    }
}

