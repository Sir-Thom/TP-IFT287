<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Auberg-Inn - Configuration Base de Données</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
          integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
</head>
<body class="bg-light">

<%
    // Vérifier si la BD est déjà configurée
    if (com.servlet.TP.InnHelper.infoBDValide(application)) {
        // Redirection vers le menu principal si BD configurée
        response.sendRedirect("menu.jsp");
        return;
    }
%>

<div class="container">
    <div class="row justify-content-center mt-5">
        <div class="col-md-8">
            <div class="card shadow">
                <div class="card-header bg-primary text-white text-center">
                    <h2 class="mb-0"> Auberg-Inn</h2>
                    <p class="mb-0">Configuration de la Base de Données</p>
                </div>
                <div class="card-body">
                    <div class="alert alert-info">
                        <strong>Configuration initiale requise</strong><br>
                        Veuillez configurer la connexion à la base de données MongoDB pour utiliser l'application.
                    </div>

                    <form action="Connexion" method="POST">
                        <input type="hidden" name="connexion" value="configurer">

                        <div class="form-group">
                            <label for="serveur">Type de serveur *</label>
                            <select class="form-control" id="serveur" name="serveur" required>
                                <option value="">Choisir un serveur</option>
                                <option value="local" <%= "local".equals(request.getAttribute("serveur")) ? "selected" : "" %>>
                                    Local (localhost:27017)
                                </option>
                                <option value="dinf" <%= "dinf".equals(request.getAttribute("serveur")) ? "selected" : "" %>>
                                     DINF (bd-info2.dinf.usherbrooke.ca)
                                </option>
                                <option value="atlas" <%= "atlas".equals(request.getAttribute("serveur")) ? "selected" : "" %>>
                                     MongoDB Atlas
                                </option>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="bd">Nom de la base de données *</label>
                            <input type="text" class="form-control" id="bd" name="bd"
                                   value="<%= request.getAttribute("bd") != null ? request.getAttribute("bd") : "auberginn" %>"
                                   placeholder="auberginn" required>
                        </div>

                        <div class="form-group">
                            <label for="user">Nom d'utilisateur *</label>
                            <input type="text" class="form-control" id="user" name="user"
                                   value="<%= request.getAttribute("user") != null ? request.getAttribute("user") : "" %>"
                                   placeholder="Nom d'utilisateur MongoDB" required>
                        </div>

                        <div class="form-group">
                            <label for="pass">Mot de passe *</label>
                            <input type="password" class="form-control" id="pass" name="pass"
                                   placeholder="Mot de passe MongoDB" required>
                            <small class="form-text text-muted">
                                Le mot de passe ne sera pas affiché pour des raisons de sécurité
                            </small>
                        </div>

                        <hr>

                        <div class="form-group text-center">
                            <button type="submit" class="btn btn-primary btn-lg px-5">
                                <i class="fas fa-database"></i> Configurer et Se Connecter
                            </button>
                        </div>
                    </form>

                    <!-- Aide contextuelle -->
                    <div class="mt-4">
                        <h5>💡 Aide à la configuration</h5>
                        <div class="row">
                            <div class="col-md-4">
                                <strong> Local :</strong><br>
                                <small>
                                    Pour MongoDB installé localement<br>
                                    User/Pass : vos identifiants locaux
                                </small>
                            </div>
                            <div class="col-md-4">
                                <strong> DINF :</strong><br>
                                <small>
                                    Serveur universitaire<br>
                                    User/Pass : vos identifiants DINF
                                </small>
                            </div>
                            <div class="col-md-4">
                                <strong> Atlas :</strong><br>
                                <small>
                                    MongoDB Cloud<br>
                                    User/Pass : identifiants Atlas
                                </small>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Messages d'erreur -->
            <div class="mt-3">
                <jsp:include page="/WEB-INF/messageErreur.jsp" />
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