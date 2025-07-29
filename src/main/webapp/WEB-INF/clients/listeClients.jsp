<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Liste des Clients</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
    <meta charset="UTF-8">
</head>
<body>
<div class="container">
    <jsp:include page="/WEB-INF/navigation.jsp" />

    <h1 class="text-center my-4">Liste des Clients</h1>



    <c:choose>
        <c:when test="${empty clients}">
            <div class="alert alert-warning text-center">
                Aucun client trouvé dans la base de données
            </div>
        </c:when>
        <c:otherwise>
            <table class="table table-striped table-bordered">
                <thead class="thead-dark">
                <tr>
                    <th>ID</th>
                    <th>Nom</th>
                    <th>Prénom</th>
                    <th>Âge</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${clients}" var="client" varStatus="loop">
                    <tr>
                        <td>${loop.index + 1}</td>
                        <td>${client.nom}</td>
                        <td>${client.prenom}</td>
                        <td>${client.age}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>