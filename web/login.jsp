<%@ page import="com.alight.CustomerLogin" %>
<%@ page import="com.alight.SQLQuery" %>
<%--
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
  <link href="css/login.css" rel="stylesheet">
</head>
<%!
  private SQLQuery sql;
  public void jspInit() {
    sql = new SQLQuery();
  }
  public void jspDestroy() {
    sql.closeConnection();
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
<body>
<div class="container">
  <h2>Sign In</h2>
  <form action="loginServlet" method="post">
    <div class="form-group">
      <label for="email"> 邮 箱</label>
      <input type="email" class="form-control" id="email" name="email" required>
    </div>
    <div class="form-group">
      <label for="password"> 密 码</label>
      <input type="password" class="form-control" id="password" name="password" required>
    </div>
    <button type="submit" class="btn btn-primary btn-block">登 录</button>
  </form>
  <p class="text-center mt-3">
    新用户? <a href="register.jsp">注册</a>
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
