<%--
  Created by IntelliJ IDEA.
  User: Jarvis Sun
  Date: 2025/3/8
  Time: 13:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Login and Registration</title>
</head>
<body>
<h2>Login</h2>

<%@ page import="org.example.SQLQuery" %>
<%@ page import="org.example.CustomerLogin" %>

<%!
    private SQLQuery sql;
    public void jspInit() {
        sql = new SQLQuery();
    }
%>

<%
    // 检查会话中是否已经有UserSessionObject
    CustomerLogin user = (CustomerLogin) session.getAttribute("User");

    // 如果没有，则创建一个新的User并存储在会话中
    if (user == null) {
        System.out.println("没有找到用户对象, 需要创建.");
        user = new CustomerLogin(sql);
        session.setAttribute("User", user);
    } else {
        System.out.println("已找到用户对象.");
    }
    // 现在user是与当前用户会话关联的独特对象
%>
<form action="loginServlet" method="post">
    Email: <input type="email" name="email" required><br>
    Password: <input type="password" name="password" required><br>
    <input type="submit" value="Login">
</form>

<h2>Register</h2>
<form action="registerServlet" method="post">
    Name: <input type="name" name="name" required><br>
    Email: <input type="email" name="email" required><br>
    Password: <input type="password" name="password" required><br>
    <input type="submit" value="Register">
</form>

<%
    // Check if the user is logged in
    if (((CustomerLogin) session.getAttribute("User")).getCustomerID() != 0) {
%>

<h2> Welcome, <%=((CustomerLogin) session.getAttribute("User")).getCustomerName()%> !</h2>
<form action="logoutServlet" method="post">
    <input type="submit" value="Logout">
</form>

<%
    }
%>
</body>
</html>
