<%@ page import="cn.haizhi.util.Const" %>
<html>
<body>
<h2>Hello World!</h2>
<%=request.getSession().getAttribute(Const.CURRENT_USER)%>
</body>
</html>
