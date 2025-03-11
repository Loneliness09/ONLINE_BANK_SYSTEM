package com.servlet;

import org.example.CustomerLogin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutAccountServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        CustomerLogin login = (CustomerLogin) request.getSession().getAttribute("User");
        int id = login.getAccountID();
        if (login.logoutAccount()) {
            System.out.println("accountID: " + id + " logouted.");
            request.getSession().setAttribute("alertMessage", "账户登出成功!");
//            request.getSession().setAttribute("User", login);
            response.sendRedirect("home.jsp");
        } else {
            request.getSession().setAttribute("alertMessage", "Invalid error!");
            request.getRequestDispatcher("home.jsp").forward(request, response);
        }
    }
}
