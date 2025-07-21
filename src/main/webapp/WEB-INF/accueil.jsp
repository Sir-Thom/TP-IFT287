
<!DOCTYPE html>
<html>
<head>
    <title>TP Auberginn</title>
    <meta name="description"
          content="Page d'accueil du système de gestion des reservations.">

    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
</head>
<body>
<div class="container">
    <h1 class="text-center">Systeme de gestion de reservation</h1>
    <div class="col-md-4 offset-md-4">
        <form action="Login" method="POST">
            <div class="form-group">
                <label for="userIdBD">Nom d'utilisateur de la base de donnee</label>
                <input class="form-control" type="TEXT" name="userIdBD" value="<%= (request.getAttribute("userIdBD") != null ? (String)request.getAttribute("userIdBD") : "") %>" placeholder="ift287_XX">
            </div>
            <div class="form-group">
                <label for="motDePasseBD">Mot de passe</label>
                <input class="form-control" type="PASSWORD" name="motDePasseBD" value="<%= (request.getAttribute("motDePasseBD") != null ? (String)request.getAttribute("motDePasseBD") : "") %>">
            </div>
            <div class="form-group">
                <label for="serveur">Serveur</label>
                <select class="custom-select" name="serveur">
                    <option value="local" <%= (request.getAttribute("serveur") != null ? (((String)request.getAttribute("serveur")).equals("local") ? "selected" : "") : "") %>>local
                    <option value="dinf" <%= (request.getAttribute("serveur") != null ? (((String)request.getAttribute("serveur")).equals("dinf") ? "selected" : "") : "selected") %>>dinf
                </select>
            </div>
            <div class="form-group">
                <label for="bd">Nom de la base de donnee</label>
                <input class="form-control" type="TEXT" name="bd" value="<%= (request.getAttribute("bd") != null ? (String)request.getAttribute("bd") : "") %>" placeholder="ift287_XXdb">
            </div>
            <input class="btn btn-primary" type="SUBMIT" name="connecter" value="Se connecter">
        </form>
    </div>
</div>
<br>
<%-- inclusion d'une autre page pour l'affichage des messages d'erreur--%>
<jsp:include page="/WEB-INF/messageErreur.jsp" />
<br>

<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
</body>
</html>
