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
<h2>Create or Edit meal</h2>

<form method="POST" action='meals' name="frmAddMeal">
    <table>
        <tr>
            <td>Description :</td>
            <td align="right">
                <input style="text-align: right"
                        type="text" name="description"
                        value="<c:out value="${meal.description}"/>"/>
            </td>
        </tr>
        <tr>
            <td>Calories :</td>
            <td align="right"><input style="text-align: right"
                    type="text" name="calories"
                    value="<c:out value="${meal.calories}"/>"/></td>
        </tr>
    </table>
    <br>
    <input type="submit" value="Save"/>
    <button onclick="window.history.back()" type="button">Cancel</button>
</form>


</body>
</html>
