package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomerLogin {
    private Connection conn;
    private SQLQuery query;

    private int customerID = 0;
    private String customerName;

    private List<Integer> accountList;
    static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    public CustomerLogin(SQLQuery query) {
        this.query = query;
        this.conn = this.query.conn;
    }

    // 打印信息
    public void println() {
        System.out.println("ID: " + Integer.toString(customerID));
        System.out.println("Name: " + customerName);
        System.out.print("Accounts: ");
        System.out.println(accountList);

    }
    // 客户注册
    public boolean register(String customerName, String email, String passwd) {
        Matcher matcher = pattern.matcher(email);

        if (!matcher.matches()) {
            System.out.println("Error adding customer: The email address format is not legal");
            return false;
        }
        return query.addCustomer(customerName, email, passwd) != 0;
    }

    // 客户登录
    public boolean login(String email, String passwd) {
        String sql = "SELECT customer_id FROM Customers WHERE email = ? AND passwd = ?";
        String sql1 = "SELECT customer_name FROM Customers WHERE customer_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             PreparedStatement pstmt1 = conn.prepareStatement(sql1);) {
            pstmt.setString(1, email);
            pstmt.setString(2, passwd); // 在实际应用中，密码应该被加密并匹配存储的哈希值
            ResultSet rs = pstmt.executeQuery();
            // 如果查询有结果，则表示登录成功
            if(rs.next()) {
                System.out.println("Customer login successfully.");
                customerID = rs.getInt("customer_id");
                pstmt.setString(1, Integer.toString(customerID));
                ResultSet rs1 = pstmt1.executeQuery();
                customerName = rs1.getString("customer_name");

                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    // 客户登出
    public void logout() {
        customerID = 0;
        customerName = null;
        accountList = null;
        System.out.println("Customer logged out.");
    }

    // 客户注销账户
    public boolean unregister() {
        return query.deleteCustomer(customerID);
    }
}
