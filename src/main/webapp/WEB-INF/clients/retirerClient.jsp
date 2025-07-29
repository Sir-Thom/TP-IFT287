<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Retirer un client - Auberg-Inn</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
          crossorigin="anonymous">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <style>
        .card-header {
            font-weight: 600;
        }
        .alert-warning {
            border-left: 4px solid #ffc107;
        }
        .btn-danger {
            transition: all 0.3s ease;
        }
        .btn-danger:hover {
            transform: scale(1.05);
        }
    </style>
</head>
<body>
<div class="container">
    <!-- Navigation -->
    <jsp:include page="/WEB-INF/navigation.jsp" />

    <!-- Breadcrumb -->
    <div class="row mt-4">
        <div class="col">
            <nav aria-label="breadcrumb">
                <ol class="breadcrumb">
                    <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/menu.jsp">Menu</a></li>
                    <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/client?action=lister">Clients</a></li>
                    <li class="breadcrumb-item active">Retirer un client</li>
                </ol>
            </nav>
        </div>
    </div>

    <!-- Messages -->
    <div class="row">
        <div class="col-md-8 offset-md-2">
            <c:if test="${not empty message}">
                <div class="alert alert-success alert-dismissible fade show">
                    <strong>Succès!</strong> ${message}
                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                </div>
            </c:if>

            <div class="card">
                <div class="card-header bg-danger text-white">
                    <h3 class="mb-0"><i class="fas fa-user-minus"></i> Retirer un client</h3>
                </div>
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/client" method="POST">
                        <input type="hidden" name="action" value="retirer">

                        <div class="form-group">
                            <label for="client">Sélectionnez le client à retirer *</label>
                            <select class="form-control" id="client" name="clientIdentifier" required>
                                <c:forEach items="${clients}" var="client">
                                    <c:set var="fullIdentifier" value="${client.prenom}|${client.nom}" />
                                    <option value="${fullIdentifier}"
                                            <c:if test="${param.clientIdentifier eq fullIdentifier}">selected</c:if>>
                                            ${client.nom}, ${client.prenom} (${client.age} ans)
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="alert alert-warning">
                            <h5><i class="fas fa-exclamation-triangle"></i> Attention!</h5>
                            <ul class="mb-0">
                                <li>Cette action est irréversible</li>
                                <li>Toutes les réservations associées à ce client seront également supprimées</li>
                                <li>Les données du client seront définitivement perdues</li>
                            </ul>
                        </div>

                        <div class="text-center mt-4">
                            <button type="submit" class="btn btn-danger btn-lg mr-3">
                                <i class="fas fa-trash-alt"></i> Confirmer la suppression
                            </button>
                            <a href="${pageContext.request.contextPath}/menu.jsp" class="btn btn-secondary btn-lg">
                                <i class="fas fa-times"></i> Annuler
                            </a>
                        </div>
                    </form>
                </div>
            </div>
        </div>

    </div>
    <jsp:include page="/WEB-INF/messageErreur.jsp" />

</div>

<!-- Scripts -->
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
        crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"
        crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"
        crossorigin="anonymous"></script>
</body>
</html>