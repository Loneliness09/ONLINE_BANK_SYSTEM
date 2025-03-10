package org.example;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteAccountServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int accountId = Integer.parseInt(request.getParameter("accountId"));
        CustomerLogin login = (CustomerLogin) request.getSession().getAttribute("User");
        if (login.deleteAccount(accountId)) {
            System.out.println("accountID: " + accountId + " deleted.");
            request.getSession().setAttribute("alertMessage", "账户注销成功!");
            response.sendRedirect("home.jsp");
        } else {
            request.getSession().setAttribute("alertMessage", "账户注销失败, 还有存款或交易记录!");
            request.getRequestDispatcher("home.jsp").forward(request, response);
        }
    }
}
