<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<a href="mealCreate.jsp">Add meal</a>
<hr>
<br>
<br>

<table border="3" cellspacing="5" cellpadding="5">
    <tr align="center">
        <td>Date</td>
        <td>Description</td>
        <td>Calories</td>
    </tr>
    <c:forEach items="${meals}" var="meal">
        <tr align="left" style="${meal.excess? "color: red":"color: green"}" }>
            <td>
                <jsp:useBean id="dateTimeFormatter" scope="request" type="java.time.format.DateTimeFormatter"/>
                <c:out value="${meal.dateTime.format(dateTimeFormatter)}"/></td>
            <td><c:out value="${meal.description}"/></td>
            <td><c:out value="${meal.calories}"/></td>

            <td>
                <a href="meals?action=edit&id=<c:out value="${meal.id}"/>">Update</a>
            </td>
            <td>
                <form action="meals?action=delete&id=<c:out value="${meal.id}"/>" method="post">
                    <input type="submit" value="Delete"/>
                </form>
            </td>

        </tr>
    </c:forEach>

</table>
</body>
</html>
