package org.example;


import java.util.Scanner;

public class Main {
    public static int accountID = 0;
    public static String gap = "================================\n";
    public static String loginMenu =
                    gap +
                    "    您还未登录, 请登录.\n" +
                    "    1. 登录\n" +
                    "    2. 注册\n" +
                    "    3. 退出\n" +
                    gap;

    public static String accountMenu =
                    gap +
                    "    账户菜单\n" +
                    "    1. 存款\n" +
                    "    2. 取款\n" +
                    "    3. 查询余额\n" +
                    "    4. 转账\n" +
                    "    5. 退卡\n" +
                    gap;
    public static void main(String[] args) {
        // 创建sql查询实例
        SQLQuery sql = new SQLQuery();
        // 用户登录实例
        CustomerLogin login = new CustomerLogin(sql);
        Scanner input = new Scanner(System.in);
        String in;
        while (true) {
            if (login.getCustomerID() == 0) {
                System.out.print(loginMenu);
                in = input.next();
                if (in.equals("1")) {
                    System.out.print("邮箱: ");
                    String email = input.nextLine();
                    System.out.print("密码: ");
                    String passwd = input.nextLine();
                    if (!login.login(email, passwd)) {
                        continue;
                    }
                } else if (in.equals("2")) {
                    System.out.print("姓名: ");
                    String name = input.nextLine();
                    System.out.print("邮箱: ");
                    String email = input.nextLine();
                    System.out.print("密码: ");
                    String passwd = input.nextLine();
                    if (login.register(name, email, passwd)) {
                        continue;
                    }
                } else if (in.equals("3")) {
                    System.out.println("再见.");
                    System.exit(0);
                } else {
                    System.out.println("输入不合法, 重新输入.");
                }
            } else {
                System.out.print(
                        gap +
                        "    已登录." +
                        "    姓名: " + login.getCustomerName() + "\n" +
                        "    邮箱: " + login.getCustomerEmail() + "\n" +
                        "    账户列表: " + login.getAccountList().toString() + "\n" +
                        "    1. 登入账户\n" +
                        "    2. 创建账户\n" +
                        "    3. 注销账户\n" +
                        "    4. 登出\n" +
                        "    5. 注销\n" +
                        gap
                );
                System.out.print("请输入: ");
                in = input.next();
                switch (in) {
                    case "1":
                        break;

                    case "2":

                        break;

                    case "3":
                        break;

                    case "4":
                        login.logout();
                        break;

                    case "5":
                        login.unregister();
                        break;

                    default:

                }
            }

        }

    }
}