package com.alight.servlet;

import com.alight.CustomerLogin;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class RegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        System.out.println("name : " + name + " email : " + email + " password : " + password);
        CustomerLogin login = (CustomerLogin) request.getSession().getAttribute("User");
        if (login.register(name, email, password)) {
            request.getSession().setAttribute("infoMessage", "注册成功!");
            response.sendRedirect("login.jsp");
        } else {
            request.getSession().setAttribute("alertMessage", "注册失败!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
