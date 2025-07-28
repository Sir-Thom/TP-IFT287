<%--
  Created by IntelliJ IDEA.
  User: zoe
  Date: 2025-07-28
  Time: 5:05 a.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Auberg-Inn - Ajouter Client</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
          integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
</head>
<body>
<div class="container">
    <!-- Navigation -->
    <jsp:include page="/WEB-INF/navigation.jsp" />

    <!-- Header -->
    <div class="row mt-4">
        <div class="col">
            <nav aria-label="breadcrumb">
                <ol class="breadcrumb">
                    <li class="breadcrumb-item"><a href="index.jsp">Accueil</a></li>
                    <li class="breadcrumb-item active">Ajouter Client</li>
                </ol>
            </nav>
        </div>
    </div>

    <div class="row">
        <div class="col-md-6 offset-md-3">
            <div class="card">
                <div class="card-header bg-success text-white">
                    <h3 class="mb-0">👤 Enregistrer un nouveau client</h3>
                </div>
                <div class="card-body">
                    <form action="ClientServlet" method="POST">
                        <input type="hidden" name="action" value="ajouter">

                        <div class="form-group">
                            <label for="nom">Nom de famille *</label>
                            <input type="text" class="form-control" id="nom" name="nom"
                                   value="<%= request.getAttribute("nom") != null ? request.getAttribute("nom") : "" %>"
                                   placeholder="Dupont" required>
                        </div>

                        <div class="form-group">
                            <label for="prenom">Prénom *</label>
                            <input type="text" class="form-control" id="prenom" name="prenom"
                                   value="<%= request.getAttribute("prenom") != null ? request.getAttribute("prenom") : "" %>"
                                   placeholder="Jean" required>
                        </div>

                        <div class="form-group">
                            <label for="age">Âge *</label>
                            <input type="number" class="form-control" id="age" name="age"
                                   value="<%= request.getAttribute("age") != null ? request.getAttribute("age") : "" %>"
                                   min="1" max="120" placeholder="25" required>
                            <small class="form-text text-muted">L'âge doit être entre 1 et 120 ans</small>
                        </div>



                        <hr>

                        <div class="form-group">
                            <div class="row">
                                <div class="col-md-6">
                                    <button type="submit" class="btn btn-success btn-block">
                                        <i class="fas fa-user-plus"></i> Enregistrer le client
                                    </button>
                                </div>
                                <div class="col-md-6">
                                    <a href="index.jsp" class="btn btn-secondary btn-block">
                                        <i class="fas fa-arrow-left"></i> Retour au menu
                                    </a>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="card-footer text-muted">
                    <small><em>Les champs marqués d'un * sont obligatoires</em></small>
                </div>
            </div>
        </div>
    </div>

    <!-- Messages d'erreur/succès -->
    <div class="row mt-3">
        <div class="col">
            <jsp:include page="/WEB-INF/messageErreur.jsp" />
        </div>
    </div>

</div>

<!-- Scripts -->
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
        integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"
        integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"
        integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>

<script>
    // Validation côté client pour l'âge
    document.getElementById('age').addEventListener('input', function() {
        const age = parseInt(this.value);
        if (age < 1 || age > 120) {
            this.setCustomValidity('L\'âge doit être entre 1 et 120 ans');
        } else {
            this.setCustomValidity('');
        }
    });
</script>
</body>
</html>
