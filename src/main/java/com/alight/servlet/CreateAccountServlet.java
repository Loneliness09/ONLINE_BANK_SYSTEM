package com.alight.servlet;

import com.alight.CustomerLogin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CreateAccountServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String password = request.getParameter("password");

        CustomerLogin login = (CustomerLogin) request.getSession().getAttribute("User");
        if (login.createAccount(password)) {
            System.out.println("accountID: " + login.getAccountID() + " password: " + password);
            request.getSession().setAttribute("alertMessage", "账户创建成功! 密码: " + password);
//            request.getSession().setAttribute("User", login);
            response.sendRedirect("accountHome.jsp");
        } else {
            request.getSession().setAttribute("alertMessage", "账户创建失败, 密码必须为6位!");
            request.getRequestDispatcher("home.jsp").forward(request, response);
        }
    }
}
