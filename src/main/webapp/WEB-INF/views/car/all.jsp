<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>All cars</title>
</head>
<body>
<h1>All cars</h1>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Model</th>
        <th>Manufacturer</th>
        <th>Drivers</th>
    </tr>
    <c:forEach var="car" items="${cars}">
        <tr>
            <td>
                <c:out value="${car.id}"/>
            </td>
            <td>
                <c:out value="${car.model}"/>
            </td>
            <td>
                <c:out value="${car.manufacturer.name}"/>
            </td>
            <td>
                <c:forEach var="driver" items="${car.drivers}">
                    <table border="1">
                        <td>
                            <c:out value="${driver.name}"/>
                        </td>
                        <td>
                            <c:out value="${driver.licenceNumber}"/>
                        </td>
                    </table>
                </c:forEach>
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/car/delete?car_id=${car.id}">delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
<footer>
    <h2><a href="${pageContext.request.contextPath}/">back to main page</a></h2>
</footer>
</html>
