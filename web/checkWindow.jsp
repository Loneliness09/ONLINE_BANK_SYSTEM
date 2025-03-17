<%--
  Created by IntelliJ IDEA.
  User: Jarvis Sun
  Date: 2025/3/9
  Time: 22:31
  To change this template use File | Settings | File Templates.
--%>
<%
    String errorInfo = (String)session.getAttribute("alertMessage"); // 获取错误属性
    if (errorInfo != null) {
%>
<script type="text/javascript">
    alert("<%=errorInfo%>"); // 弹出错误信息
</script>
<%
        session.removeAttribute("alertMessage");
    }
%>

<%
    String info = (String)session.getAttribute("infoMessage"); // 获取错误属性
    if (info != null) {
        System.out.println(info);
%>
<script type="text/javascript">
    var notification = document.createElement('div');
    notification.className = 'notification';
    notification.textContent = '<%=info%>';
    document.body.appendChild(notification);
    setTimeout(function () {
        notification.style.opacity = 0;
        setTimeout(function () {
            document.body.removeChild(notification);
        }, 1000);
    }, 1000);
</script>
<%
        session.removeAttribute("infoMessage");
    }
%>
