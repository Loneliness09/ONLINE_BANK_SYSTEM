<%--
  Created by IntelliJ IDEA.
  User: Jarvis Sun
  Date: 2025/3/9
  Time: 21:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>
<head>
  <title>银行线上交易系统 - 用户主页面</title>
</head>
<body>
<h1>欢迎来到银行线上交易系统</h1>
<div>
  <h2>用户信息</h2>
  <p>姓名：${userInfo.name}</p>
  <p>邮箱：${userInfo.email}</p>
  <p>用户ID：${userInfo.userId}</p>
  <h3>账户列表</h3>
  <ul>
    <c:forEach var="account" items="${userInfo.accountList}">
      <li>${account.accountNumber} -${account.accountType}</li>
    </c:forEach>
  </ul>
</div>
<div>
  <h2>功能列表</h2>
  <ul>
    <li><a href="loginAccount.jsp">登入账户</a></li>
    <li><a href="createAccount.jsp">创建账户</a></li>
    <li><a href="deleteAccount.jsp">注销账户</a></li>
    <li><a href="changeUser.jsp">更换用户</a></li>
    <li><a href="logout.jsp">注销用户</a></li>
  </ul>
</div>
</body>
</html>
