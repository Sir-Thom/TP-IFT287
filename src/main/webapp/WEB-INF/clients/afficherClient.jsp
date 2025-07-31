<%-- /WEB-INF/clients/afficherClient.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Détails de ${client.prenom} ${client.nom}</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
    <meta charset="UTF-8">
</head>
<body>
<div class="container">
    <jsp:include page="/WEB-INF/navigation.jsp" />

    <%-- Informations du client --%>
    <div class="card my-4">
        <div class="card-header">
            <h3>Détails du Client</h3>
        </div>
        <div class="card-body">
            <h5 class="card-title">${client.prenom} ${client.nom}</h5>
            <p class="card-text"><strong>Âge :</strong> ${client.age} ans</p>
        </div>
    </div>

    <%-- Liste des réservations --%>
    <h4>Historique des réservations</h4>
    <c:choose>
        <c:when test="${empty reservations}">
            <div class="alert alert-info">Ce client n'a aucune réservation.</div>
        </c:when>
        <c:otherwise>
            <table class="table table-hover">
                <thead class="thead-light">
                <tr>
                    <th>ID Réservation</th>
                    <th>ID Chambre</th>
                    <th>Date de début</th>
                    <th>Date de fin</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${reservations}" var="res">
                    <tr>
                        <td>${res.idReservation}</td>
                        <td>${res.idChambre}</td>
                        <td>${res.dateDebut}</td>
                        <td>${res.dateFin}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>

    <a href="client?action=lister" class="btn btn-secondary mt-3">Retour à la liste</a>
</div>
</body>
</html>