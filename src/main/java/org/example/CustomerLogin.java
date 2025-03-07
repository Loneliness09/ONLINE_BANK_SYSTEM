package org.example;

import java.sql.Connection;
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
        customerID = query.login(email, passwd);
        if (customerID == 0) {
            return false;
        }
        customerName = query.getCustomerName(customerID);
        accountList = query.getCustomerAccounts(customerID);
        println();
        return true;
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

    public static void main(String[] args) {
        SQLQuery sql = new SQLQuery();
        CustomerLogin login = new CustomerLogin(sql);
        login.login("abcaaddyytiar@qq.com", "123456");
        login.logout();
        sql.closeConnection();
    }
}
