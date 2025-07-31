<%--
  Created by IntelliJ IDEA.
  User: zowes
  Date: 2025-07-29
  Time: 9:37 a.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Auberg-Inn - Ajouter Commodité</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
          integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
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
                    <li class="breadcrumb-item"><a href="../../menu.jsp">Menu</a></li>
                    <li class="breadcrumb-item active">Ajouter Commodité</li>
                </ol>
            </nav>
        </div>
    </div>

    <div class="row">
        <div class="col-md-8 offset-md-2">
            <!-- Messages de succès -->
            <% if (request.getAttribute("message") != null) { %>
            <div class="alert alert-success alert-dismissible fade show">
                <strong>Succès!</strong> <%= request.getAttribute("message") %>
                <button type="button" class="close" data-dismiss="alert">&times;</button>
            </div>
            <% } %>

            <!-- Messages d'erreur -->
            <% if (request.getAttribute("erreur") != null) { %>
            <div class="alert alert-danger alert-dismissible fade show">
                <strong>Erreur!</strong> <%= request.getAttribute("erreur") %>
                <button type="button" class="close" data-dismiss="alert">&times;</button>
            </div>
            <% } %>

            <div class="card">
                <div class="card-header bg-warning text-dark">
                    <h3 class="mb-0">🛎️ Ajouter une nouvelle commodité</h3>
                </div>
                <div class="card-body">
                    <div class="alert alert-info">
                        <strong><i class="fas fa-info-circle"></i> Information</strong><br>
                        Les commodités sont des services ou équipements supplémentaires
                        que vous pouvez ajouter à vos chambres (ex: WiFi, Climatisation, Room Service).
                    </div>

                    <form action="../../CommoditeServlet" method="POST">
                        <input type="hidden" name="action" value="ajouter">

                        <div class="form-group">
                            <label for="description">Description de la commodité *</label>
                            <input type="text" class="form-control" id="description" name="description"
                                   value="<%= request.getAttribute("description") != null ? request.getAttribute("description") : "" %>"
                                   placeholder="Ex: WiFi haut débit, Climatisation, Room Service" required>
                            <small class="form-text text-muted">Décrivez le service ou équipement proposé</small>
                        </div>

                        <div class="form-group">
                            <label for="surplusPrix">Surplus de prix par nuit (CAD) *</label>
                            <div class="input-group">
                                <div class="input-group-prepend">
                                    <span class="input-group-text">+ $</span>
                                </div>
                                <input type="number" class="form-control" id="surplusPrix" name="surplusPrix"
                                       value="<%= request.getAttribute("surplusPrix") != null ? request.getAttribute("surplusPrix") : "" %>"
                                       step="0.01" min="0" placeholder="0.00" required>
                            </div>
                            <small class="form-text text-muted">
                                Prix supplémentaire ajouté au prix de base de la chambre.
                                Mettez 0.00 si la commodité est gratuite.
                            </small>
                        </div>

                        <!-- Exemples de commodités courantes -->
                        <div class="form-group">
                            <label>💡 Suggestions de commodités populaires :</label>
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="list-group list-group-flush">
                                        <button type="button" class="list-group-item list-group-item-action"
                                                onclick="remplirCommodite('WiFi haut débit', '0.00')">
                                            📶 WiFi haut débit - Gratuit
                                        </button>
                                        <button type="button" class="list-group-item list-group-item-action"
                                                onclick="remplirCommodite('Climatisation', '15.00')">
                                            ❄️ Climatisation - $15/nuit
                                        </button>
                                        <button type="button" class="list-group-item list-group-item-action"
                                                onclick="remplirCommodite('Mini-bar', '25.00')">
                                            🍷 Mini-bar - $25/nuit
                                        </button>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="list-group list-group-flush">
                                        <button type="button" class="list-group-item list-group-item-action"
                                                onclick="remplirCommodite('Room Service 24h', '10.00')">
                                            🛎️ Room Service 24h - $10/nuit
                                        </button>
                                        <button type="button" class="list-group-item list-group-item-action"
                                                onclick="remplirCommodite('Balcon privé', '20.00')">
                                            🌅 Balcon privé - $20/nuit
                                        </button>
                                        <button type="button" class="list-group-item list-group-item-action"
                                                onclick="remplirCommodite('Parking', '12.00')">
                                            🚗 Parking - $12/nuit
                                        </button>
                                    </div>
                                </div>
                            </div>
                            <small class="form-text text-muted">Cliquez sur une suggestion pour la sélectionner</small>
                        </div>

                        <hr>

                        <div class="form-group">
                            <div class="row">
                                <div class="col-md-6">
                                    <button type="submit" class="btn btn-warning btn-block">
                                        <i class="fas fa-plus"></i> Ajouter la commodité
                                    </button>
                                </div>
                                <div class="col-md-6">
                                    <a href="../../menu.jsp" class="btn btn-secondary btn-block">
                                        <i class="fas fa-arrow-left"></i> Retour au menu
                                    </a>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="card-footer text-muted">
                    <small>
                        <i class="fas fa-lightbulb"></i>
                        <em>Après avoir créé vos commodités, vous pourrez les associer à vos chambres.</em>
                    </small>
                </div>
            </div>
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
    function remplirCommodite(description, prix) {
        document.getElementById('description').value = description;
        document.getElementById('surplusPrix').value = prix;

        // Animation de feedback
        const descInput = document.getElementById('description');
        descInput.style.backgroundColor = '#d4edda';
        setTimeout(() => {
            descInput.style.backgroundColor = '';
        }, 1000);
    }
</script>
</body>
</html>