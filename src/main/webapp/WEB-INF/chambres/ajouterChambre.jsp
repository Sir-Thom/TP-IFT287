<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
    <title>Auberg-Inn - Ajouter Chambre</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
          integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">

    <style>
        body {
            background: url('<%= request.getContextPath() %>/assets/addRoom.jpg') no-repeat center center fixed;
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
    <!-- Navigation
    plus clean SANS la bare navigation.
     -->


    <div class="container d-flex justify-content-center align-items-center flex-column" style="min-height: 100vh;">
        <div class="col-md-8 form-overlay">
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
                <div class="card-header bg-success text-white">
                    <h3 class="mb-0">Ajouter une nouvelle chambre</h3>
                </div>
                <div class="card-body">
                    <form action="chambres" method="POST">


                    <input type="hidden" name="action" value="ajouter">

                        <div class="form-group">
                            <label for="nom">Nom de la chambre *</label>
                            <input type="text" class="form-control" id="nom" name="nom"
                                   value="<%= request.getAttribute("nom") != null ? request.getAttribute("nom") : "" %>"
                                   placeholder="Ex: Chambre 101" required>
                            <small class="form-text text-muted">Nom unique pour identifier la chambre</small>
                        </div>

                        <div class="form-group">
                            <label for="typeLit">Type de lit *</label>
                            <select class="form-control" id="typeLit" name="typeLit" required>
                                <option value="">Choisir un type de lit</option>
                                <option value="Simple" <%= "Simple".equals(request.getAttribute("typeLit")) ? "selected" : "" %>>Lit Simple</option>
                                <option value="Double" <%= "Double".equals(request.getAttribute("typeLit")) ? "selected" : "" %>>Lit Double</option>
                                <option value="Queen" <%= "Queen".equals(request.getAttribute("typeLit")) ? "selected" : "" %>>Lit Queen</option>
                                <option value="King" <%= "King".equals(request.getAttribute("typeLit")) ? "selected" : "" %>>Lit King</option>
                                <option value="Superposé" <%= "Superposé".equals(request.getAttribute("typeLit")) ? "selected" : "" %>>Lits Superposés</option>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="prixBase">Prix de base par nuit (CAD) *</label>
                            <div class="input-group">
                                <div class="input-group-prepend">
                                    <span class="input-group-text">$</span>
                                </div>
                                <input type="number" class="form-control" id="prixBase" name="prixBase"
                                       value="<%= request.getAttribute("prixBase") != null ? request.getAttribute("prixBase") : "" %>"
                                       step="0.01" min="0" placeholder="75.00" required>
                            </div>
                            <small class="form-text text-muted">Prix sans les commodités supplémentaires</small>
                        </div>

                        <div class="form-group">
                            <div class="row">
                                <div class="col-md-6">
                                    <button type="submit" class="btn btn-success btn-block">
                                        <i class="fas fa-plus"></i> Ajouter la chambre
                                    </button>
                                </div>
                                <div class="col-md-6">
                                    <a href="${pageContext.request.contextPath}/menu.jsp" class="btn btn-secondary btn-block">
                                        <i class="fas fa-arrow-left"></i> Retourner
                                    </a>
                                </div>
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
        integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"
        integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"
        integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
</body>
</html>