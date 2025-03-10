<%--
  Created by IntelliJ IDEA.
  User: Jarvis Sun
  Date: 2025/3/9
  Time: 21:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.example.CustomerLogin" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
  <title>银行线上交易系统 - 用户主页面</title>
  <link rel="stylesheet" type="text/css" href="home.css">
  <%@ include file="checkWindow.jsp" %>
</head>
<body>
<%
  CustomerLogin user = (CustomerLogin) session.getAttribute("User");
  if (user == null || user.getCustomerID() == 0) {
    response.sendRedirect("login.jsp");
    return;
  }
  user.setAccountList();
  List<Integer> accounts = user.getAccountList();
  System.out.println("refresh.");
  System.out.println(accounts);
%>
<h1>欢迎来到银行线上交易系统</h1>
<div>
  <h2>用户信息</h2>
  <p>姓名：<%=user.getCustomerName()%></p>
  <p>邮箱：<%=user.getCustomerEmail()%></p>
  <p>用户ID：<%=user.getCustomerID()%></p>
  <h3>账户列表</h3>
  <ul>
    <%
      if (accounts.size() != 0) {
        for (Integer account : accounts) {
    %>

    <li><%=account%></li>

    <%
      }
      } else {
    %>

    <li>None</li>

    <%
      }
    %>
  </ul>
</div>
<div>
  <h2>功能列表</h2>
  <button class="button" onclick="document.getElementById('loginModal').style.display='block'">登入账户</button>
  <button class="button" onclick="document.getElementById('createModal').style.display='block'">创建账户</button>
  <button class="button" onclick="document.getElementById('deleteModal').style.display='block'">注销账户</button>
  <button class="button" onclick="document.getElementById('changeModal').style.display='block'">更换用户</button>
  <button class="button" onclick="document.getElementById('logoutModal').style.display='block'">注销用户</button>
</div>

<!-- 登入账户模态框 -->
<div id="loginModal" class="modal">
  <div class="modal-content">
    <span class="close" onclick="document.getElementById('loginModal').style.display='none'">&times;</span>
    <h2>登入账户</h2>
    <form action="loginAccountServlet" method="post">
      <label for="accountId">账户ID: </label>
      <input type="text" id="accountId" name="accountId"><br>
      <label for="passwd">密码: </label>
      <input type="text" id="passwd" name="password"><br>
      <button type="submit" class="button">提交</button>
    </form>
  </div>
</div>

<!-- 创建账户模态框 -->
<div id="createModal" class="modal">
  <div class="modal-content">
    <span class="close" onclick="document.getElementById('createModal').style.display='none'">&times;</span>
    <h2>创建账户</h2>
    <form action="createAccountServlet" method="post">
      <label for="passwd1">密码: </label>
      <input type="text" id="passwd1" name="password"><br>
      <button type="submit" class="button">创建</button>
    </form>
  </div>
</div>

<!-- 注销账户模态框 -->
<div id="deleteModal" class="modal">
  <div class="modal-content">
    <span class="close" onclick="document.getElementById('deleteModal').style.display='none'">&times;</span>
    <h2>注销账户</h2>
    <form action="deleteAccountServlet" method="post">
      <label for="accountId1">账户ID: </label>
      <input type="text" id="accountId1" name="accountId"><br><br>
      <button type="submit" class="button">注销</button>
    </form>
  </div>
</div>

<!-- 更换用户模态框 -->
<div id="changeModal" class="modal">
  <div class="modal-content">
    <span class="close" onclick="document.getElementById('changeModal').style.display='none'">&times;</span>
    <h2>即将登出</h2>
    <form action="logoutServlet" method="post">
      <button type="submit" class="button">确定</button>
    </form>
  </div>
</div>

<!-- 注销用户模态框 -->
<div id="logoutModal" class="modal">
  <div class="modal-content">
    <span class="close" onclick="document.getElementById('logoutModal').style.display='none'">&times;</span>
    <h2>注销用户</h2>
    <p>您确定要注销用户吗?</p>
    <form action="unRegisterServlet" method="post">
      <button type="submit" class="button">注销</button>
    </form>
  </div>
</div>

<script>
  // 获取所有模态框
  var modals = document.getElementsByClassName('modal');
  // 遍历并关闭模态框
  window.onclick = function(event) {
    for (var i = 0; i < modals.length; i++) {
      if (event.target === modals[i]) {
        modals[i].style.display = "none";
      }
    }
  }
</script>
</body>
</html>
