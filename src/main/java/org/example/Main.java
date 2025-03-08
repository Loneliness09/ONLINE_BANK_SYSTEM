package org.example;

import java.math.BigDecimal;
import java.util.Scanner;

public class Main {
    public static String gap = "===============================================================================================\n";
    public static String startMenu =
                    gap +
                    "  您还未登录, 请登录.\n" +
                    "    1. 用户登录\n" +
                    "    2. 用户注册\n" +
                    "    3. 退出程序\n" +
                    gap;

    public static void main(String[] args) {
        // 创建sql查询实例
        SQLQuery sql = new SQLQuery();
        // 用户登录实例
        CustomerLogin login = new CustomerLogin(sql);
        Scanner input = new Scanner(System.in);
        String in;
        String passwd;
        int accountID = 0;
        BigDecimal amount;
        while (true) {
            if (login.getCustomerID() == 0) {
                System.out.print(startMenu);
                System.out.print("请输入: ");
                in = input.nextLine();
                System.out.print(gap);
                switch (in) {
                    case "1": {
                        System.out.print("邮箱: ");
                        String email = input.nextLine();
                        System.out.print("密码: ");
                        passwd = input.nextLine();
                        System.out.print(gap);
                        if (!login.login(email, passwd)) {
                            System.out.println("登录失败.");
                            continue;
                        }
                        break;
                    }
                    case "2": {
                        System.out.print("姓名: ");
                        String name = input.nextLine();
                        System.out.print("邮箱: ");
                        String email = input.nextLine();
                        System.out.print("密码: ");
                        passwd = input.nextLine();
                        if (login.register(name, email, passwd)) {
                            continue;
                        }
                        break;
                    }
                    case "3":
                        System.out.println("再见.");
                        System.exit(0);
                    default:
                        System.out.println("输入不合法, 重新输入.");
                        break;
                }
            } else if (login.getAccountID() == 0) {
                login.setAccountList();
                System.out.print(
                        gap +
                        "  姓名: " + login.getCustomerName() +
                        "    邮箱: " + login.getCustomerEmail() +"\n" +
                        "  账户列表: " + login.getAccountList().toString() + "\n" +
                        "    1. 登入账户\n" +
                        "    2. 创建账户\n" +
                        "    3. 注销账户\n" +
                        "    4. 更换用户\n" +
                        "    5. 注销用户\n" +
                        "    0. 退出程序\n" +
                        gap
                );
                System.out.print("请输入: ");
                in = input.nextLine();
                System.out.print(gap);
                switch (in) {
                    case "1":
                        System.out.print("请输入账户ID: ");
                        accountID = Integer.parseInt(input.nextLine());
                        if (!login.getAccountList().contains(accountID)) {
                            System.out.println("这不是你的账户.");
                            break;
                        }
                        System.out.print("请输入密码: ");
                        passwd = input.nextLine();
                        if (login.accountLogin(accountID, passwd)) {
                            System.out.println("账户ID " + accountID + " 登入成功.");
                        } else {
                            System.out.println("账户ID " + accountID + " 登入失败.");
                        }
                        break;

                    case "2":
                        System.out.print("请输入密码: ");
                        passwd = input.nextLine();
                        if (login.createAccount(passwd)) {
                            System.out.println("账户ID " + login.getAccountID() + " 创建成功.");
                        } else {
                            System.out.println("账户创建失败.");
                        }
                        break;

                    case "3":
                        System.out.print("请输入账户ID: ");
                        accountID = Integer.parseInt(input.nextLine());
                        if (!login.getAccountList().contains(accountID)) {
                            System.out.println("这不是你的账户.");
                            break;
                        }
                        if (login.deleteAccount(accountID)) {
                            login.accountLogout();
                        }
                        break;

                    case "4":
                        login.logout();
                        break;

                    case "5":
                        if (login.unregister()) {
                            System.out.println("注销成功.");
                        } else {
                            System.out.println("注销失败.");
                        }
                        break;

                    case "0":
                        System.exit(0);
                        break;

                    default:
                        System.out.println("输入不合法, 重新输入.");

                }

            } else {
                System.out.print(
                        gap +
                        "  姓名: " + login.getCustomerName() +
                        "    账户ID: " + login.getAccountID() + "\n" +
                        "    1. 存款\n" +
                        "    2. 取款\n" +
                        "    3. 查询余额\n" +
                        "    4. 转账\n" +
                        "    5. 查询流水\n" +
                        "    6. 更换账户\n" +
                        "    0. 退出程序\n" +
                        gap
                );
                System.out.print("请输入: ");
                in = input.nextLine();
                System.out.print(gap);
                switch (in) {
                    case "1":
                        System.out.print("请输入金额: ");
                        amount = input.nextBigDecimal();
                        input.nextLine();
                        if (login.deposit(amount)) {
                            System.out.println("账户ID " + login.getAccountID() + " 存款成功.");
                        } else {
                            System.out.println("账户ID " + login.getAccountID() + " 存款失败.");
                        }
                        break;

                    case "2":
                        System.out.print("请输入金额: ");
                        amount = input.nextBigDecimal();
                        input.nextLine();
                        if (login.withdraw(amount)) {
                            System.out.println("账户ID " + accountID + " 取款成功.");
                        } else {
                            System.out.println("账户ID " + accountID + " 取款失败.");
                        }
                        break;

                    case "3":
                        System.out.println("您卡号 " + login.getAccountID() + " 的账户余额为 "+ login.getBalance());
                        break;

                    case "4":
                        System.out.print("请输入对方账户ID: ");
                        accountID = Integer.parseInt(input.nextLine());
                        System.out.print("请输入金额: ");
                        amount = input.nextBigDecimal();
                        input.nextLine();
                        if (login.transfer(accountID, amount)) {
                            System.out.println("向账户ID " + accountID + " 转账成功.");
                        } else {
                            System.out.println("向账户ID " + accountID + " 转账失败.");
                        }
                        break;

                    case "5":
                        if (login.getTransactionRecords()) {
                            System.out.print(gap);
                            System.out.println("账户ID " + accountID + " 查询成功.");
                        } else {
                            System.out.println("账户ID " + accountID + " 查询失败.");
                        }
                        break;

                    case "6":
                        login.accountLogout();
                        break;

                    case "0":
                        System.exit(0);
                        break;

                    default:
                        System.out.println("输入不合法, 重新输入.");

                }
            }

        }

    }
}