<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add driver</title>
</head>
<body>
<h1>Write information about your driver</h1>
<form method="post" action="${pageContext.request.contextPath}/driver/create">
    Enter driver name <input type="text" name="driver_name" required>
    Enter driver license <input type="text" name="licenseNumber" required>
    <button type="submit">Add</button>
</form>
</body>
<footer>
    <h2><a href="${pageContext.request.contextPath}/">back to main page</a></h2>
</footer>
</html>
