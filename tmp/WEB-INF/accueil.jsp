<%@ page import="tp.objets.Client" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.servlet.TP.InnHelper" %>
<!DOCTYPE html>
<html>
<head>
    <title>Auberg-Inn</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="Page d'accueil du système de gestion des réservations.">

    <link rel="stylesheet"
          href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
          integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO"
          crossorigin="anonymous">
</head>
<body>
<div class="container">
    <jsp:include page="/WEB-INF/navigation.jsp" />
    <h1 class="text-center">Système de gestion de l'auberge</h1>

    <h3 class="text-center">Clients</h3>
    <div class="col-8 offset-2">
        <table class="table">
            <thead class="thead-dark">
            <tr>
                <th>Nom</th>
                <th>Prénom</th>
                <th>Âge</th>
            </tr>
            </thead>
            <tbody>
            <%
                List<Client> clients = null;
                String errorMessage = null;

                try {
                    // Vérification étape par étape afin d'identifier le problème quoi
                    if (InnHelper.getInnInterro(session) != null) {
                        if (InnHelper.getInnInterro(session).getGestionClient() != null) {
                            clients = InnHelper.getInnInterro(session).getGestionClient().getListClients();
                        } else {
                            errorMessage = "GestionClient n'est pas initialisé";
                        }
                    } else {
                        errorMessage = "InnInterro n'est pas initialisé dans la session";
                    }
                } catch (Exception e) {
                    errorMessage = "Erreur lors de la récupération des clients: " + e.getMessage();
                    e.printStackTrace(); // Pour le debug
                }

                // Si clients est null, initialiser une liste vide
                if (clients == null) {
                    clients = new ArrayList<>();
                }

                if (clients.isEmpty()) {
            %>
            <tr>
                <td colspan="3" class="text-center text-muted">
                    <% if (errorMessage != null) { %>
                    <div class="alert alert-warning">
                        <%= errorMessage %>
                    </div>
                    <% } else { %>
                    Aucun client enregistré pour le moment.
                    <% } %>
                </td>
            </tr>
            <%
            } else {
                for (Client c : clients) {
            %>
            <tr>
                <td><%= c.getNom() != null ? c.getNom() : "N/A" %></td>
                <td><%= c.getPrenom() != null ? c.getPrenom() : "N/A" %></td>
                <td><%= c.getAge() %></td>
            </tr>
            <%
                    }
                }
            %>
            </tbody>
        </table>
    </div>

    <br>
    <jsp:include page="/WEB-INF/messageErreur.jsp" />
    <br>
</div>

<!-- JS -->
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
        integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
        crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"
        integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49"
        crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"
        integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy"
        crossorigin="anonymous"></script>
</body>
</html>