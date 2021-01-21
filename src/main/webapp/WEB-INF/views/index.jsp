<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>Manufacturer</h1>
<h4><a href="${pageContext.request.contextPath}/manufacturer/create">Add manufacturer</a></h4>
<h4><a href="${pageContext.request.contextPath}/manufacturer/all">Get all manufacturers</a></h4>
<h1>Driver</h1>
<h4><a href="${pageContext.request.contextPath}/driver/create">Add driver</a></h4>
<h4><a href="${pageContext.request.contextPath}/driver/all">Get all drivers</a></h4>
<h1>Car</h1>
<h4><a href="${pageContext.request.contextPath}/car/create">Add car</a></h4>
<h4><a href="${pageContext.request.contextPath}/car/all">Get all cars</a></h4>
<h4><a href="${pageContext.request.contextPath}/car/drivers/add">Add driver to car</a></h4>
</body>
</html>
