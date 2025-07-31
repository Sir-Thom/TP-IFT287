<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

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
          <a class="dropdown-item" href="ChambreServlet?action=afficherFormAjouter" >Ajouter une chambre</a>
          <a class="dropdown-item" href="ChambreServlet?action=afficherFormSupprimer">Supprimer une chambre</a>
          <a class="dropdown-item" href="ChambreServlet?action=afficherFormModifier">Modifier une chambre</a>
          <div class="dropdown-divider"></div>
          <a class="dropdown-item" href="ChambreServlet?action=afficherFormRecherche">Voir une chambre - A venir...</a>
          <a class="dropdown-item" href="ChambreServlet?action=afficherFormChambresLibres" >Voir les chambres de libres</a>
        </div>

      </li>

      <!-- Gestion des Clients -->
      <!-- ===================== -->
      <li class="nav-item dropdown">
        <a class="nav-link dropdown-toggle" href="#" id="clientsDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
           Clients
        </a>
        <div class="dropdown-menu" aria-labelledby="clientsDropdown">
          <a class="dropdown-item" href="ClientServlet?action=afficherFormAjouter">Ajouter un client</a>
          <a class="dropdown-item" href="ClientServlet?action=afficherFormAjouter" >Supprimer un client</a>
          <div class="dropdown-divider"></div>
          <a class="dropdown-item" href="<%= request.getContextPath() %>/afficherClient.jsp">Afficher un client</a>
        </div>
      </li>

      <!-- Gestion des Commodités -->
      <!-- ===================== -->
      <li class="nav-item dropdown">
        <a class="nav-link dropdown-toggle" href="#" id="commoditesDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
           Commodités
        </a>
        <div class="dropdown-menu" aria-labelledby="commoditesDropdown">
          <a class="dropdown-item" href="<%= request.getContextPath() %>/commodites/ajouterCommodite.jsp">Ajouter une commodité</a>
          <a class="dropdown-item" href="<%= request.getContextPath() %>/commodites/inclureCommodite.jsp">Inclure une commdité à chambre</a>
          <a class="dropdown-item" href="<%= request.getContextPath() %>/commodites/enleverCommodite.jsp">Enlever une commodité d'une chambre</a>
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