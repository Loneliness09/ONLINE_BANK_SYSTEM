<%--
  Created by IntelliJ IDEA.
  User: Jarvis Sun
  Date: 2025/3/8
  Time: 13:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Login and Registration</title>
    <link rel="stylesheet" type="text/css" href="home.css">
    <%@ include file="checkWindow.jsp" %>
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
    <label for="email">Email: </label>
    <input type="email" id="email" name="email" required><br>
    <label for="passwd">Password: </label>
    <input type="password" id="passwd" name="password" required><br>
    <button type="submit" class="button">登录</button>
</form>

<h2>Register</h2>
<form action="registerServlet" method="post">
    <label for="tname">Name: </label>
    <input type="text" id="tname" name="name" required><br>
    <label for="email1">Email: </label>
    <input type="email" id="email1" name="email" required><br>
    <label for="passwd1">Password: </label>
    <input type="password" id="passwd1" name="password" required><br>
    <button type="submit" class="button">注册</button>
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
