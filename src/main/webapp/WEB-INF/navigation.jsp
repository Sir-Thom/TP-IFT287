<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

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
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
  <a class="navbar-brand" href="menu.jsp">
    <strong>Auberg-Inn</strong>
  </a>

  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
    <span class="navbar-toggler-icon"></span>
  </button>

  <div class="collapse navbar-collapse" id="navbarNav">
    <ul class="navbar-nav mr-auto">
      <!-- Gestion des Chambres -->
      <!-- ===================== -->
      <li class="nav-item dropdown">
        <a class="nav-link dropdown-toggle" href="#" id="chambresDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
          Chambres
        </a>
        <div class="dropdown-menu" aria-labelledby="chambresDropdown">
          <a class="dropdown-item" href="chambres/ajouterChambre.jsp"> Ajouter une chambre</a>
          <a class="dropdown-item" href="supprimerChambre.jsp"> Supprimer une chambre</a>
          <div class="dropdown-divider"></div>
          <a class="dropdown-item" href="afficherChambre.jsp">Voir une chambre</a>
          <a class="dropdown-item" href="chambres/chambresLibres.jsp">Voir les chambres de libres</a>
        </div>
      </li>

      <!-- Gestion des Clients -->
      <!-- ===================== -->
      <li class="nav-item dropdown">
        <a class="nav-link dropdown-toggle" href="#" id="clientsDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
           Clients
        </a>
        <div class="dropdown-menu" aria-labelledby="clientsDropdown">
          <a class="dropdown-item" href="ajouterClient.jsp">Ajouter un client</a>
          <a class="dropdown-item" href="supprimerClient.jsp">Supprimer un client</a>
          <div class="dropdown-divider"></div>
          <a class="dropdown-item" href="afficherClient.jsp">Afficher un client</a>
        </div>
      </li>

      <!-- Gestion des Commodités -->
      <!-- ===================== -->
      <li class="nav-item dropdown">
        <a class="nav-link dropdown-toggle" href="#" id="commoditesDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
           Commodités
        </a>
        <div class="dropdown-menu" aria-labelledby="commoditesDropdown">
          <a class="dropdown-item" href="ajouterCommodite.jsp">Ajouter une commodité</a>
          <a class="dropdown-item" href="inclureCommodite.jsp">Inclure une commdité à chambre</a>
          <a class="dropdown-item" href="enleverCommodite.jsp">Enlever une commodité d'une chambre</a>
        </div>
      </li>

      <!-- Gestion des Réservations -->
      <!-- ===================== -->
      <li class="nav-item dropdown">
        <a class="nav-link dropdown-toggle" href="#" id="reservationsDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
          Réservations
        </a>
        <div class="dropdown-menu" aria-labelledby="reservationsDropdown">
          <a class="dropdown-item" href="ajouterReservation.jsp"> Réserver</a>
        </div>
      </li>
    </ul>

  </div>
</nav>

<!-- Affichage du chemin de navigation pour certaines pages -->
<% if (request.getRequestURI().contains(".jsp") && !request.getRequestURI().endsWith("index.jsp")) { %>
<div class="container-fluid bg-light py-2">
  <div class="container">
    <small class="text-muted">
      <i class="fas fa-map-marker-alt"></i>
      Vous êtes ici: <strong><%= request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/") + 1) %></strong>
    </small>
  </div>
</div>
<% } %>