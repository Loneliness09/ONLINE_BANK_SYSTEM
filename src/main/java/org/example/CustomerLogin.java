package org.example;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomerLogin {
    private final SQLQuery query;

    private int customerID = 0;
    private String customerName;
    private String customerEmail;
    private List<Integer> accountList;
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

    public void setAccountList() {
        accountList = query.getCustomerAccounts(customerID);
    }

    public int getAccountID() {
        return accountID;
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

    // 客户登出
    public void logout() {
        customerID = 0;
        customerName = null;
        accountList = null;
        System.out.println("用户登出.");
    }

    // 客户注销账户
    public boolean unregister() {
        if (customerID == 0) return false;
        boolean success = query.deleteCustomer(customerID);
        if (success) {
            logout();
        }
        return success;
    }

    public boolean createAccount(String passwd) {
        if (customerID == 0) return false;
        if (isNotSixDigitNumber(passwd)) {
            System.out.println("创建账户失败: 密码必须为6位");
            return false;
        }
        accountID = query.createAccount(customerID, passwd);
        return accountID != 0;
    }
    public boolean accountLogin(int accountID, String passwd) {
        if (customerID == 0) return false;
        if (isNotSixDigitNumber(passwd)) {
            System.out.println("登录账户失败: 密码必须为6位");
            return false;
        }
        this.accountID = query.accountLogin(accountID, passwd);
        return this.accountID != 0;
    }
    public void accountLogout() {
        accountID = 0;
        System.out.println("账户登出.");
    }
    public boolean deleteAccount(int accountId) {
        if (customerID == 0 || accountId == 0) return false;
        return query.deleteAccount(accountId);
    }

    public boolean deposit(BigDecimal amount) {
        if (customerID == 0 || accountID == 0) return false;
        return query.deposit(accountID, amount);
    }

    public boolean withdraw(BigDecimal amount) {
        if (customerID == 0 || accountID == 0) return false;
        return query.withdraw(accountID, amount);
    }

    public BigDecimal getBalance() {
        if (customerID == 0 || accountID == 0) {
            System.out.println("未登录.");
            return BigDecimal.valueOf(0);
        }
        return query.getAccountBalance(accountID);
    }

    public boolean transfer(int targetAccountId, BigDecimal amount) {
        if (customerID == 0 || accountID == 0) {
            System.out.println("未登录.");
            return false;
        }
        if (query.transfer(accountID, targetAccountId, amount)) {
            System.out.println("转账成功, 对方开户名: " + query.recordCustomerName(targetAccountId));
            return true;
        } else return false;
    }

    public boolean getTransactionRecords() {
        if (customerID == 0 || accountID == 0) {
            System.out.println("未登录.");
            return false;
        }
        for (TransactionRecord record:query.getTransactionRecords(accountID)) {
            record.printInfo();
        }
        return true;
    }

    public static boolean isNotSixDigitNumber(String str) {
        String regex = "^\\d{6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return !matcher.matches();
    }

    public static void main(String[] args) {
        SQLQuery sql = new SQLQuery();
        CustomerLogin login = new CustomerLogin(sql);
        login.login("abcaaddyytiar@qq.com", "123456");
        login.logout();
        System.out.println(isNotSixDigitNumber("111111"));
        sql.closeConnection();
    }
}
