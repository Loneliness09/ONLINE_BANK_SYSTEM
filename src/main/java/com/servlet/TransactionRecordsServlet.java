package com.servlet;

import org.example.CustomerLogin;
import org.example.TransactionRecord;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class TransactionRecordsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CustomerLogin login = (CustomerLogin) request.getSession().getAttribute("User");
        List<TransactionRecord> records = login.getTransactionRecords();
        login.printTransactionRecords();
        if (records == null) {
            request.getSession().setAttribute("alertMessage", "无交易记录!");
            response.sendRedirect("accountHome.jsp");
        } else {

            request.getSession().setAttribute("transactionRecords", records);
            request.getSession().setAttribute("openTransactionRecords", "yes");
            response.sendRedirect("accountHome.jsp");
        }
    }
}
