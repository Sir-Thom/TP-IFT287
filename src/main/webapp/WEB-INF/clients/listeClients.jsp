<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Liste des Clients</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
    <meta charset="UTF-8">

    <style>
        body {
            background: url('<%= request.getContextPath() %>/assets/happyfamilylol.png') no-repeat center center fixed;
            background-size: cover;
            min-height: 100vh;
        }

        .form-overlay {
            background-color: rgba(255, 255, 255, 0.92);
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.4);
            margin-top: 40px;
        }

        .message-auto-hide {
            transition: opacity 0.5s ease-out;
        }
    </style>
</head>
<body>

<!-- <div class="container"> -->
<div class="container d-flex justify-content-center align-items-center flex-column" style="min-height: 100vh;">
    <div class="col-md-8 form-overlay">


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
                <c:forEach items="${clients}" var="client" varStatus="loop">
                    <tr>
                        <td>${loop.index + 1}</td>
                        <td>
                                <%-- Création d'une URL dynamique pour chaque client --%>
                            <c:url var="detailsUrl" value="client">
                                <c:param name="action" value="afficherClient"/>
                                <c:param name="id" value="${client.prenom}|${client.nom}"/>
                            </c:url>
                                <%-- Le nom du client est maintenant un lien --%>
                            <a href="${detailsUrl}">${client.nom}</a>
                        </td>
                        <td>${client.prenom}</td>
                        <td>${client.age}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
    <div class="col-md-6">
        <a href="${pageContext.request.contextPath}/menu.jsp" class="btn btn-secondary btn-block">
            <i class="fas fa-times"></i> Retourner
        </a>
    </div>
    <jsp:include page="/WEB-INF/messageErreur.jsp" />

    </div>

</div>
</body>
</html>