<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add manufacturer</title>
</head>
<body>
<h1>Enter data about manufacturer</h1>
<form method="post" action="${pageContext.request.contextPath}/manufacturers/add">
    Please provide manufacturer name<input type="text" name="manufacturer_name" required>
    Please provide manufacturer country<input type="text" name="manufacturer_country" required>
    <button type="submit">Add</button>
</form>
</body>
<footer>
    <h2><a href="${pageContext.request.contextPath}/">back to main page</a></h2>
</footer>
</html>
