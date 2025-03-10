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
