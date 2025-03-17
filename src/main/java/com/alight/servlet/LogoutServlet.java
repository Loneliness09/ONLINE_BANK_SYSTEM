package com.alight.servlet;

import com.alight.CustomerLogin;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ((CustomerLogin) request.getSession().getAttribute("User")).logout();
        request.getSession().invalidate();
        request.getSession().setAttribute("infoMessage", "登出成功!");
        response.sendRedirect("login.jsp");
    }
}
