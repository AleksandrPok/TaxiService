<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>Manufacturer</h1>
<h4><a href="${pageContext.request.contextPath}/manufacturers/add">Add manufacturer</a></h4>
<h4><a href="${pageContext.request.contextPath}/manufacturers">Get all manufacturers</a></h4>
<h1>Driver</h1>
<h4><a href="${pageContext.request.contextPath}/drivers/add">Add driver</a></h4>
<h4><a href="${pageContext.request.contextPath}/drivers">Get all drivers</a></h4>
<h1>Car</h1>
<h4><a href="${pageContext.request.contextPath}/cars/add">Add car</a></h4>
<h4><a href="${pageContext.request.contextPath}/cars">Get all cars</a></h4>
<h4><a href="${pageContext.request.contextPath}/cars/drivers/add">Add driver to car</a></h4>
</body>
</html>
