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
<form action="loginServlet" method="post">
    Email: <input type="email" name="email" required><br>
    Password: <input type="password" name="password" required><br>
    <input type="submit" value="Login">
</form>

<h2>Register</h2>
<form action="registerServlet" method="post">
    Email: <input type="email" name="email" required><br>
    Password: <input type="password" name="password" required><br>
    <input type="submit" value="Register">
</form>

<%
    // Check if the user is logged in
    if (session.getAttribute("user") != null) {
        out.println("<h2> Welcome, " + session.getAttribute("user") + "!</h2>");
        out.println("<form action=\"logoutServlet\" method=\"post\">");
        out.println("<input type=\"submit\" value=\"Logout\">");
        out.println("</form>");
    }
%>
</body>
</html>
