package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerLogin {

    private Connection conn;

    public CustomerLogin(Connection conn) {
        this.conn = conn;
    }

    // 客户注册
    public boolean register(String customerName, String email, String passwd) {
        String sql = "INSERT INTO Customers (customername, email, passwd) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customerName);
            pstmt.setString(2, email);
            pstmt.setString(3, passwd); // 在实际应用中，密码应该被加密
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 客户登录
    public boolean login(String email, String passwd) {
        String sql = "SELECT customerid FROM Customers WHERE email = ? AND passwd = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, passwd); // 在实际应用中，密码应该被加密并匹配存储的哈希值
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // 如果查询有结果，则表示登录成功
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 客户登出
    public void logout() {
        // 在命令行应用中，登出可能只是结束会话，没有实际的数据库操作
        System.out.println("Customer logged out.");
    }

    // 客户注销账户
    public boolean unregister(int customerId) {
        String sql = "DELETE FROM Customers WHERE customerid = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
