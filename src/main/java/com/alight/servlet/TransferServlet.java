package com.alight.servlet;

import com.alight.CustomerLogin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

public class TransferServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int targetAccountId = Integer.parseInt(request.getParameter("toAccount"));
        BigDecimal amount;
        try {
            amount = BigDecimal.valueOf(Double.parseDouble(request.getParameter("amount")));
            System.out.println("输入合法.");
        } catch(Exception e){
            System.out.println("输入不合法: " + request.getParameter("amount"));
            request.getSession().setAttribute("alertMessage", "转账失败, 输入不合法!");
            response.sendRedirect("accountHome.jsp");
            return;
        }

        CustomerLogin login = (CustomerLogin) request.getSession().getAttribute("User");
        if (targetAccountId == login.getAccountID()) {
            request.getSession().setAttribute("alertMessage", "转账失败, 不可给本账户转账!");
            response.sendRedirect("accountHome.jsp");
        } else {
            System.out.println("Account " + login.getAccountID() + " transfer amount " + amount + " to account " + targetAccountId);
            if (login.transfer(targetAccountId, amount)) {
                request.getSession().setAttribute("infoMessage", "转账给 " + targetAccountId + " 金额 " + amount + "元 成功! 对方户名 " + login.getTargetName(targetAccountId));
                response.sendRedirect("accountHome.jsp");
            } else {
                if (login.getTargetName(targetAccountId) == null) {
                    request.getSession().setAttribute("alertMessage", "转账失败, 没有这个账户ID!");
                } else {
                    request.getSession().setAttribute("alertMessage", "转账失败, 余额不足!");
                }
                response.sendRedirect("accountHome.jsp");
            }
        }

    }
}
