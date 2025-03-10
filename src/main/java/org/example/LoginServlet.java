package org.example;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        System.out.println("email: " + email + " password: " + password);
        CustomerLogin login = (CustomerLogin) request.getSession().getAttribute("User");
        if (login.login(email, password)) {
            request.getSession().setAttribute("alertMessage", "登录成功!");
//            request.getSession().setAttribute("User", login);
            response.sendRedirect("home.jsp");
        } else {
            request.setAttribute("error", "Invalid email or password!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
