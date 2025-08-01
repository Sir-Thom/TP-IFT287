<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Supprimer une chambre - Auberg-Inn</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
          crossorigin="anonymous">

    <style>
        body {
            background: url('<%= request.getContextPath() %>/assets/supprimerChambre.jpg') no-repeat center center fixed;
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
<div class="container">

    <!-- Messages -->
    <div class="container d-flex justify-content-center align-items-center flex-column" style="min-height: 100vh;">
        <div class="col-md-8 form-overlay">
            <c:if test="${not empty message}">
                <div class="alert alert-success alert-dismissible fade show">
                    <strong>Succès!</strong> ${message}
                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                </div>
            </c:if>


            <div class="card">
                <div class="card-header bg-danger text-white">
                    <h3 class="mb-0"> Supprimer une chambre</h3>
                </div>
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/chambres" method="POST">
                        <input type="hidden" name="action" value="supprimer">

                        <div class="form-group">
                            <label for="chambre">Sélectionnez la chambre à supprimer *</label>
                            <select class="form-control" id="chambre" name="nom" required>
                                <option value="">-- Sélectionnez une chambre --</option>
                                <c:forEach items="${chambres}" var="chambre">
                                    <option value="${chambre.nomChambre}"
                                            <c:if test="${param.nom eq chambre.nomChambre}">selected</c:if>>
                                            ${chambre.nomChambre} - ${chambre.typeLit} (<fmt:formatNumber value="${chambre.prixBase}" pattern="#,##0.00" />$)
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="alert alert-warning">
                            <strong>Attention!</strong> L'équipe de démolition sera contactée une fois l'action effectuée.
                        </div>

                        <div class="row">
                            <div class="col-md-6">
                                <button type="submit" class="btn btn-danger btn-block">
                                    <i class="fas fa-trash-alt"></i> 🔥SUPPRIMER🔥
                                </button>
                            </div>
                            <div class="col-md-6">
                                <a href="${pageContext.request.contextPath}/menu.jsp" class="btn btn-secondary btn-block">
                                    <i class="fas fa-times"></i> Retourner
                                </a>
                            </div>
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
<script src="https://kit.fontawesome.com/a076d05399.js" crossorigin="anonymous"></script>
</body>
</html>