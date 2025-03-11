<%@ page import="org.example.CustomerLogin" %><%--
 Created by IntelliJ IDEA.
 User: Jarvis Sun
 Date: 2025/3/8
 Time: 13:02
 To change this template use File Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Login and Registration</title>
  <!-- 引入Bootstrap CSS -->
  <link href="css/bootstrap.css" rel="stylesheet">
  <!-- 自定义CSS -->
  <style>
    body {
      background-color: #0d1117;
      color: #c9d1d9;
    }
    .container {
      max-width: 400px;
      margin: 50px auto;
      padding: 20px;
      border-radius: 10px;
      background-color: #161b22;
      box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
    }
    h2 {
      color: #ffffff;
      text-align: center;
    }
    form {
      margin-top: 20px;
    }
    .form-group label {
      font-weight: bold;
    }
    .form-control {
      background-color: #0d1117;
      color: #c9d1d9;
      border-color: #30363d;
    }
    .btn-primary {
      background-color: #238636;
      border-color: #238636;
    }
    .btn-primary:hover {
      background-color: #2ea043;
      border-color: #2ea043;
    }
    a {
      color: #58a6ff;
      text-decoration: none;
    }
    a:hover {
      text-decoration: underline;
    }
    .footer-links {
      display: flex;
      justify-content: space-between;
      margin-top: 30px;
      font-size: 12px;
    }
    .footer-links a {
      color: #58a6ff;
      text-decoration: none;
    }
    .footer-links a:hover {
      text-decoration: underline;
    }
  </style>
</head>
<body>
<%
  // 检查会话中是否已经有UserSessionObject
  CustomerLogin user = (CustomerLogin) session.getAttribute("User");

  // 如果没有，则创建一个新的User并存储在会话中
  if (user == null) {
    response.sendRedirect("login.jsp");
  } else {
    System.out.println("已找到用户对象.");
  }
  // 现在user是与当前用户会话关联的独特对象
%>
<div class="container">
  <h2>Sign Up</h2>
  <form action="registerServlet" method="post">
    <div class="form-group">
      <label for="name">Username</label>
      <input type="name" class="form-control" id="name" name="name" required>
    </div>
    <div class="form-group">
      <label for="email">Email address</label>
      <input type="email" class="form-control" id="email" name="email" required>
    </div>
    <div class="form-group">
      <label for="password">Password</label>
      <input type="password" class="form-control" id="password" name="password" required>
    </div>
    <button type="submit" class="btn btn-primary btn-block">Sign up</button>
  </form>
  <p class="text-center mt-3">
    Sign In <a href="login.jsp">Sign Up</a>
  </p>
  <div class="footer-links">
    <a href="#">Terms</a>
    <a href="#">Privacy</a>
    <a href="#">Docs</a>
    <a href="#">Contact Support</a>
    <a href="#">Manage cookies</a>
    <a href="#">Do not share my personal information</a>
  </div>
</div>
</body>
</html>
