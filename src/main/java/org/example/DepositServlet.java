package org.example;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

public class DepositServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(request.getParameter("amount")));

        CustomerLogin login = (CustomerLogin) request.getSession().getAttribute("User");
        System.out.println("deposit amount " + amount + " to account " + login.getAccountID());
        if (login.deposit(amount)) {
            request.getSession().setAttribute("alertMessage", "存款 " + amount + "元 成功!");
            response.sendRedirect("accountHome.jsp");
        } else {
            request.setAttribute("error", "Invalid amount!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
