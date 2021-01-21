<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add car</title>
</head>
<body>
<h1>Enter car information</h1>
<form method="post" action="${pageContext.request.contextPath}/car/create">
    Enter model <input type="text" name="model" required>
    Enter manufacturer ID <input type="number" name="manufacturerID" required>
    <button type="submit">Add</button>
</form>
</body>
<footer>
    <h2><a href="${pageContext.request.contextPath}/">back to main page</a></h2>
</footer>
</html>
