<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
<h5 style="color: red">${message}</h5>
<form action="${pageContext.request.contextPath}/login" method="post">
    Login <input type="text" name="login" value="${log}" required>
    Password <input type="password" name="password" required>
    <button type="submit">login</button>
</form>
<h3><a href="${pageContext.request.contextPath}/drivers/add">registration</a></h3>
</body>
</html>
