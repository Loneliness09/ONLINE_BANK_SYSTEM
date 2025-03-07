package org.example;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomerLogin {
    private final SQLQuery query;

    private int customerID = 0;
    private String customerName;
    private String customerEmail;
    private List<Integer> accountList;

    private Account account;
    private int accountID = 0;
    static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    public CustomerLogin(SQLQuery query) {
        this.query = query;
    }

    public int getCustomerID() {
        return customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public List<Integer> getAccountList() {
        return accountList;
    }

    // 打印信息
    public void println() {
        System.out.println("    用户ID: " + customerID);
        System.out.println("    用户姓名: " + customerName);
        System.out.print("    账户列表: ");
        System.out.println(accountList);

    }
    // 客户注册
    public boolean register(String customerName, String email, String passwd) {
        Matcher matcher = pattern.matcher(email);

        if (!matcher.matches()) {
            System.out.println("注册失败: 邮箱格式不正确.");
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
        customerEmail = query.getCustomerEmail(customerID);
        accountList = query.getCustomerAccounts(customerID);
        println();
        return true;
    }

    public boolean accountLogin(int accountID, String passwd) {
        this.accountID = query.accountLogin(accountID, passwd);
        if (accountID == 0) {
            return false;
        }
        customerName = query.getCustomerName(customerID);
        customerEmail = query.getCustomerEmail(customerID);
        accountList = query.getCustomerAccounts(customerID);
        println();
        return true;
    }

    // 客户登出
    public void logout() {
        customerID = 0;
        customerName = null;
        accountList = null;
        System.out.println("用户登出.");
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
