package com.alight.servlet;

import com.alight.CustomerLogin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UnRegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CustomerLogin login = (CustomerLogin) request.getSession().getAttribute("User");
        if (login.unregister()) {
            request.getSession().setAttribute("infoMessage", "注销成功!");
            response.sendRedirect("login.jsp");
        } else {
            request.getSession().setAttribute("alertMessage", "注销失败!" + login.getMessage());
            request.getRequestDispatcher("home.jsp").forward(request, response);
        }
    }
}
