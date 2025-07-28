<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Auberg-Inn - Réserver</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
          crossorigin="anonymous">
</head>
<body>
<div class="container">
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <a class="navbar-brand" href="../../../menu.jsp">🏨 Auberg-Inn</a>
    </nav>

    <!-- Breadcrumb -->
    <div class="row mt-4">
        <div class="col">
            <nav aria-label="breadcrumb">
                <ol class="breadcrumb">
                    <li class="breadcrumb-item"><a href="../../../menu.jsp">Menu</a></li>
                    <li class="breadcrumb-item active">Réserver</li>
                </ol>
            </nav>
        </div>
    </div>

    <!-- Messages -->
    <div class="row">
        <div class="col-md-8 offset-md-2">
            <% if (request.getAttribute("message") != null) { %>
            <div class="alert alert-success alert-dismissible fade show">
                <strong>Succès!</strong> <%= request.getAttribute("message") %>
                <button type="button" class="close" data-dismiss="alert">&times;</button>
            </div>
            <% } %>

            <% if (request.getAttribute("erreur") != null) { %>
            <div class="alert alert-danger alert-dismissible fade show">
                <strong>Erreur!</strong> <%= request.getAttribute("erreur") %>
                <button type="button" class="close" data-dismiss="alert">&times;</button>
            </div>
            <% } %>

            <div class="card">
                <div class="card-header bg-primary text-white">
                    <h3 class="mb-0">📅 Réserver une chambre</h3>
                </div>
                <div class="card-body">
                    <form action="../../../ReservationServlet" method="POST">
                        <input type="hidden" name="action" value="reserver">

                        <div class="form-group">
                            <label for="prenom">Prénom du client *</label>
                            <input type="text" class="form-control" id="prenom" name="prenom"
                                   value="<%= request.getAttribute("prenom") != null ? request.getAttribute("prenom") : "" %>"
                                   required>
                        </div>

                        <div class="form-group">
                            <label for="nom">Nom du client *</label>
                            <input type="text" class="form-control" id="nom" name="nom"
                                   value="<%= request.getAttribute("nom") != null ? request.getAttribute("nom") : "" %>"
                                   required>
                        </div>

                        <div class="form-group">
                            <label for="chambre">Nom de la chambre *</label>
                            <input type="text" class="form-control" id="chambre" name="chambre"
                                   value="<%= request.getAttribute("chambre") != null ? request.getAttribute("chambre") : "" %>"
                                   required>
                        </div>

                        <div class="form-group">
                            <label for="dateDebut">Date de début *</label>
                            <input type="date" class="form-control" id="dateDebut" name="dateDebut"
                                   value="<%= request.getAttribute("dateDebut") != null ? request.getAttribute("dateDebut") : "" %>"
                                   required>
                        </div>

                        <div class="form-group">
                            <label for="dateFin">Date de fin *</label>
                            <input type="date" class="form-control" id="dateFin" name="dateFin"
                                   value="<%= request.getAttribute("dateFin") != null ? request.getAttribute("dateFin") : "" %>"
                                   required>
                        </div>

                        <div class="row">
                            <div class="col-md-6">
                                <button type="submit" class="btn btn-primary btn-block">Réserver</button>
                            </div>
                            <div class="col-md-6">
                                <a href="../../../menu.jsp" class="btn btn-secondary btn-block">Retour au menu</a>
                            </div>
                        </div>

                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Scripts -->
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
        crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"
        crossorigin="anonymous"></script>
</body>
</html>
