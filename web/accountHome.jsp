<%@ page import="org.example.CustomerLogin" %>
<%@ page import="java.util.List" %>
<%@ page import="org.example.TransactionRecord" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>账户主界面</title>
    <link href="css/bootstrap.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="css/home.css">
    <style>
        body {
            background-color: #0d1117;
            color: #c9d1d9;
        }
        .container {
            max-width: 600px;
            margin: 50px auto;
            padding: 20px;
            border-radius: 10px;
            background-color: #161b22;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
        }
        h2, h3 {
            color: #58a6ff;
            text-align: center;
        }
        .button {
            background-color: #238636;
            border-color: #238636;
            color: white;
            margin: 10px 0;
        }
        .button:hover {
            background-color: #2ea043;
            border-color: #2ea043;
        }
        .modal-content {
            background-color: #161b22;
            color: #c9d1d9;
        }
        .close {
            color: #c9d1d9;
        }
    </style>
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

<div class="container">
    <h2>欢迎，<span id="username"><%=user.getCustomerName()%></span></h2>
    <p>账户ID: <span id="account-id"><%=user.getAccountID()%></span></p>

    <div class="text-center">
        <button class="button btn btn-primary btn-block" onclick="showModal('deposit')">存款</button>
        <button class="button btn btn-primary btn-block" onclick="showModal('withdraw')">取款</button>
        <button class="button btn btn-primary btn-block" onclick="showModal('balance')">查询余额</button>
        <button class="button btn btn-primary btn-block" onclick="showModal('transfer')">转账</button>
        <button class="button btn btn-primary btn-block" onclick="showModal('transactions')">查询流水</button>
        <button class="button btn btn-primary btn-block" onclick="showModal('changeAccount')">更换账户</button>
    </div>
</div>

<!-- 存款模态框 -->
<div id="depositModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeModal('deposit')">&times;</span>
        <h2>存款</h2>
        <form action="depositServlet" method="post">
            <div class="modal-div">
                <label for="depositAmount">金额:</label>
                <input type="number" id="depositAmount" name="amount" required>
            </div>

            <button type="submit" class="button btn btn-primary btn-block">提交</button>
        </form>
    </div>
</div>

<!-- 取款模态框 -->
<div id="withdrawModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeModal('withdraw')">&times;</span>
        <h2>取款</h2>
        <form action="withdrawServlet" method="post">
            <div class="modal-div">
            <label for="withdrawAmount">金额:</label>
            <input type="number" id="withdrawAmount" name="amount" required>
            </div>
            <button type="submit" class="button btn btn-primary btn-block">提交</button>
        </form>
    </div>
</div>

<!-- 查询余额模态框 -->
<div id="balanceModal" class="modal">
    <div class="modal-content">
        <div class="close" onclick="closeModal('balance')">&times;</div>
        <h2>查询余额</h2>
        <p>您的余额为: <span id="balanceAmount"><%=user.getBalance()%></span></p>
    </div>
</div>

<!-- 转账模态框 -->
<div id="transferModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeModal('transfer')">&times;</span>
        <h2>转账</h2>
        <form action="transferServlet" method="post">
            <div class="modal-div">
            <label for="transferTo">转入账户:</label>
            <input type="text" id="transferTo" name="toAccount" required>
            </div>
            <br>

            <div class="modal-div">
            <label for="transferAmount">金额:</label>
            <input type="text" id="transferAmount" name="amount" required>
            </div>
            <br>
            <button type="submit" class="button btn btn-primary btn-block">提交</button>
        </form>
    </div>
</div>

<!-- 查询流水模态框 -->
<div id="transactionsModal" class="modal">
    <div class="modal-content" style="width: 820px; max-height: 50%;">
        <span class="close" onclick="closeModal('transactions')">&times;</span>
        <h2>查询流水</h2>
        <div style="width: 98%; overflow-y: auto;max-height: 80%;margin-bottom: 10px">
        <ul>
            <%
                List<TransactionRecord> records = user.getTransactionRecords();
                if (records != null) {
                    for (TransactionRecord record: records) {
            %>
            <li><%=record.getString()%></li>
            <hr style="margin-right: 40px">
            <%
                    }
                }
            %>
        </ul>
    </div>
    </div>
</div>

<!-- 更换账户模态框 -->
<div id="changeAccountModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeModal('changeAccount')">&times;</span>
        <h2>更换账户</h2>
        <form action="logoutAccountServlet" method="post">
            <p>您确定要登出账户吗?</p>
            <button type="submit" class="button btn btn-primary btn-block">确定</button>
        </form>
    </div>
</div>

<script>
    function showModal(modalId) {
        var modal = document.getElementById(modalId + 'Modal');
        modal.style.display = "block";
    }

    function closeModal(modalId) {
        var modal = document.getElementById(modalId + 'Modal');
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