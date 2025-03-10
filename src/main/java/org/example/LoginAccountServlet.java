package org.example;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginAccountServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int accountId = Integer.parseInt(request.getParameter("accountId"));
        String password = request.getParameter("password");
        System.out.println("accountID: " + accountId + " password: " + password);
        CustomerLogin login = (CustomerLogin) request.getSession().getAttribute("User");
        if (login.loginAccount(accountId, password)) {
            request.getSession().setAttribute("alertMessage", "账户登录成功!");
//            request.getSession().setAttribute("User", login);
            response.sendRedirect("accountHome.jsp");
        } else {
            request.getSession().setAttribute("alertMessage", "Invalid id or password!");
            request.getRequestDispatcher("home.jsp").forward(request, response);
        }
    }
}
