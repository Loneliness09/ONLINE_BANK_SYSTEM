<%@ page import="org.example.CustomerLogin" %>
<%@ page import="java.util.List" %>
<%@ page import="org.example.TransactionRecord" %>
<%--
  Created by IntelliJ IDEA.
  User: Jarvis Sun
  Date: 2025/3/10
  Time: 8:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta charset="UTF-8">
    <title>账户主界面</title>
    <link rel="stylesheet" type="text/css" href="home.css">
    <%@ include file="checkWindow.jsp" %>

</head>
<body>
<%
    CustomerLogin user = (CustomerLogin) session.getAttribute("User");
    if (user == null || user.getAccountID() == 0) {
        System.out.println("未登录账户.");
        response.sendRedirect("home.jsp");
        return;
    }
%>
<h2>欢迎，<span id="username"><%=user.getCustomerName()%></span></h2>
<p>账户ID: <span id="account-id"><%=user.getAccountID()%></span></p>

<button class="button" onclick="showModal('deposit')">存款</button>
<button class="button" onclick="showModal('withdraw')">取款</button>
<button class="button" onclick="showModal('balance')">查询余额</button>
<button class="button" onclick="showModal('transfer')">转账</button>
<button class="button" onclick="showModal('transactions')">查询流水</button>
<button class="button" onclick="showModal('changeAccount')">更换账户</button>

<!-- 存款模态框 -->
<div id="depositModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeModal('depositModal')">&times;</span>
        <h2>存款</h2>
        <form action="depositServlet" method="post">
            <label for="depositAmount">金额:</label>
            <input type="number" id="depositAmount" name="amount" required>
            <button type="submit" class="button">提交</button>
        </form>
    </div>
</div>

<!-- 取款模态框 -->
<div id="withdrawModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeModal('withdrawModal')">&times;</span>
        <h2>取款</h2>
        <form action="withdrawServlet" method="post">
            <label for="withdrawAmount">金额:</label>
            <input type="number" id="withdrawAmount" name="amount" required>
            <button type="submit" class="button">提交</button>
        </form>
    </div>
</div>

<!-- 查询余额模态框 -->
<div id="balanceModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeModal('balanceModal')">&times;</span>
        <h2>查询余额</h2>
        <p>您的余额为: <span id="balanceAmount"><%=user.getBalance()%></span></p>
    </div>
</div>

<!-- 转账模态框 -->
<div id="transferModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeModal('transferModal')">&times;</span>
        <h2>转账</h2>
        <form action="transferServlet" method="post">
            <label for="transferTo">转入账户:</label>
            <input type="text" id="transferTo" name="toAccount" required>
            <label for="transferAmount">金额:</label>
            <input type="text" id="transferAmount" name="amount" required>
            <button type="submit" class="button">提交</button>
        </form>
    </div>
</div>

<!-- 查询流水模态框 -->
<div id="transactionsModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeModal('transactionsModal')">&times;</span>
        <h2>查询流水</h2>
        <ul>
        <%
            List<TransactionRecord> records = user.getTransactionRecords();
            if (records != null) {
                for (TransactionRecord record: records) {
        %>
        <li><%=record.getString()%></li>
        <%
                }
            }
        %>
        </ul>
<%--        <form action="/transactionRecordsServlet" method="post">--%>
<%--            <button type="submit" class="button">刷新</button>--%>
<%--        </form>--%>
    </div>
</div>

<!-- 更换账户模态框 -->
<div id="changeAccountModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeModal('changeAccountModal')">&times;</span>
        <h2>更换账户</h2>
        <form action="logoutAccountServlet" method="post">
            <p>您确定要登出账户吗?</p>
            <button type="submit" class="button">确定</button>
        </form>
    </div>
</div>

<script>
    function showModal(modalId) {
        var modal = document.getElementById(modalId + 'Modal');
        modal.style.display = "block";
    }

    function closeModal(modalId) {
        var modal = document.getElementById(modalId);
        modal.style.display = "none";
    }

    window.onclick = function(event) {
        var modals = document.getElementsByClassName('modal');
        for (var i = 0; i < modals.length; i++) {
            if (event.target === modals[i]) {
                modals[i].style.display = "none";
            }
        }
    }


</script>

</body>
</html>

