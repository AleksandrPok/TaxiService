<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add driver to car</title>
</head>
<body>
<h1>Enter all necessary information</h1>
<form method="post" action="${pageContext.request.contextPath}/cars/drivers/add">
    Enter car id <input type="number" name="carId" required>
    Enter driver id <input type="number" name="driverId" required>
    <button type="submit">Add</button>
</form>
</body>
<footer>
    <h2><a href="${pageContext.request.contextPath}/">back to main page</a></h2>
</footer>
</html>
