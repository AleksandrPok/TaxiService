<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registration</title>
</head>
<body>
<h1>Write your information</h1>
<form method="post" action="${pageContext.request.contextPath}/drivers/add">
    Enter name <input type="text" name="driver_name" required>
    Enter license <input type="text" name="licenseNumber" required>
    Enter login <input type="text" name="login" value="${log}" required>
    Enter password <input type="password" name="pwd" required>
    Repeat password <input type="password" name="pwdRpt" required>
    <button type="submit">Add</button>
</form>
</body>
<footer>
    <h2><a href="${pageContext.request.contextPath}/">back to main page</a></h2>
</footer>
</html>
