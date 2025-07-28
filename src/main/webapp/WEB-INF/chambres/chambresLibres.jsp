<%--
  Created by IntelliJ IDEA.
  User: zowes
  Date: 2025-07-28
  Time: 8:32 a.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="tp.objets.Chambre" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="com.servlet.TP.InnHelper" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <title>Auberg-Inn - Chambres Libres</title>
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
          <li class="breadcrumb-item"><a href="../index.jsp">Accueil</a></li>
          <li class="breadcrumb-item active">Chambres Libres</li>
        </ol>
      </nav>
    </div>
  </div>

  <!-- Form de recherche -->
  <div class="row">
    <div class="col-md-8 offset-md-2">
      <div class="card">
        <div class="card-header bg-primary text-white">
          <h3 class="mb-0"> Rechercher les chambres disponibles</h3>
        </div>
        <div class="card-body">
          <form action="ChambreServlet" method="POST">
            <input type="hidden" name="action" value="chambresLibres">

            <div class="row">
              <div class="col-md-6">
                <div class="form-group">
                  <label for="dateDebut">Date d'arrivée *</label>
                  <input type="date" class="form-control" id="dateDebut" name="dateDebut"
                         value="<%= request.getAttribute("dateDebut") != null ? request.getAttribute("dateDebut") : LocalDate.now().toString() %>"
                         min="<%= LocalDate.now().toString() %>" required>
                </div>
              </div>
              <div class="col-md-6">
                <div class="form-group">
                  <label for="dateFin">Date de départ *</label>
                  <input type="date" class="form-control" id="dateFin" name="dateFin"
                         value="<%= request.getAttribute("dateFin") != null ? request.getAttribute("dateFin") : LocalDate.now().plusDays(1).toString() %>"
                         min="<%= LocalDate.now().plusDays(1).toString() %>" required>
                </div>
              </div>
            </div>

            <div class="form-group text-center">
              <button type="submit" class="btn btn-primary">
                <i class="fas fa-search"></i> Rechercher les disponibilités
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>

  <!-- Résultats -->
  <%
    List<Chambre> chambresLibres = null;
    String dateDebut = (String) request.getAttribute("dateDebut");
    String dateFin = (String) request.getAttribute("dateFin");
    String errorMessage = null;

    if (dateDebut != null && dateFin != null) {
      try {
        if (InnHelper.getInnInterro(session) != null &&
                InnHelper.getInnInterro(session).getGestionChambre() != null) {
          chambresLibres = InnHelper.getInnInterro(session).getGestionChambre().afficherChambresLibres(dateDebut, dateFin);
        } else {
          errorMessage = "Service de gestion des chambres non disponible";
        }
      } catch (Exception e) {
        errorMessage = "Erreur lors de la recherche: " + e.getMessage();
        e.printStackTrace();
      }
    }
  %>

  <% if (dateDebut != null && dateFin != null) { %>
  <div class="row mt-4">
    <div class="col">
      <div class="card">
        <div class="card-header bg-info text-white">
          <h4 class="mb-0">
            Résultats pour du <%= dateDebut %> au <%= dateFin %>
          </h4>
        </div>
        <div class="card-body">
          <% if (errorMessage != null) { %>
          <div class="alert alert-danger">
            <strong>Erreur:</strong> <%= errorMessage %>
          </div>
          <% } else if (chambresLibres != null && !chambresLibres.isEmpty()) { %>
          <div class="alert alert-success">
            <strong>Excellent!</strong> <%= chambresLibres.size() %> chambre(s) disponible(s) pour ces dates.
          </div>

          <div class="row">
            <% for (Chambre chambre : chambresLibres) { %>
            <div class="col-md-4 mb-3">
              <div class="card border-success">
                <div class="card-body">
                  <h5 class="card-title text-success">
                     <%= chambre.getNomChambre() %>
                  </h5>
                  <p class="card-text">
                    <strong>Type de lit:</strong> <%= chambre.getTypeLit() %><br>
                    <strong>Prix par nuit:</strong> <span class="text-success font-weight-bold">$<%= String.format("%.2f", chambre.getPrixBase()) %> CAD</span>
                  </p>
                  <div class="btn-group btn-group-sm w-100">
                    <a href="reserverChambre.jsp?chambre=<%= chambre.getNomChambre() %>&dateDebut=<%= dateDebut %>&dateFin=<%= dateFin %>"
                       class="btn btn-success">
                      <i class="fas fa-calendar-plus"></i> Réserver
                    </a>
                    <a href="detailsChambre.jsp?nom=<%= chambre.getNomChambre() %>"
                       class="btn btn-outline-info">
                      <i class="fas fa-info-circle"></i> Détails
                    </a>
                  </div>
                </div>
              </div>
            </div>
            <% } %>
          </div>
          <% } else if (chambresLibres != null) { %>
          <div class="alert alert-warning">
            <strong>Aucune chambre disponible</strong> pour la période sélectionnée.<br>
            Essayez des dates différentes ou consultez toutes nos chambres.
          </div>
          <div class="text-center">
            <a href="listeChambres.jsp" class="btn btn-outline-primary">
              <i class="fas fa-list"></i> Voir toutes les chambres
            </a>
          </div>
          <% } %>
        </div>
      </div>
    </div>
  </div>
  <% } %>

  <!-- Messages d'erreur/succès -->
  <div class="row mt-3">
    <div class="col">
      <jsp:include page="/WEB-INF/messageErreur.jsp" />
    </div>
  </div>

  <!-- Bouton retour -->
  <div class="row mt-4">
    <div class="col text-center">
      <a href="../index.jsp" class="btn btn-secondary">
        <i class="fas fa-arrow-left"></i> Retour au menu principal
      </a>
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
  // Validation des dates
  document.getElementById('dateDebut').addEventListener('change', function() {
    const dateDebut = new Date(this.value);
    const dateFin = document.getElementById('dateFin');
    const dateFinValue = new Date(dateFin.value);

    if (dateFinValue <= dateDebut) {
      dateFin.value = new Date(dateDebut.getTime() + 24*60*60*1000).toISOString().split('T')[0];
    }

    dateFin.min = new Date(dateDebut.getTime() + 24*60*60*1000).toISOString().split('T')[0];
  });

  document.getElementById('dateFin').addEventListener('change', function() {
    const dateFin = new Date(this.value);
    const dateDebut = document.getElementById('dateDebut');
    const dateDebutValue = new Date(dateDebut.value);

    if (dateFin <= dateDebutValue) {
      dateDebut.value = new Date(dateFin.getTime() - 24*60*60*1000).toISOString().split('T')[0];
    }
  });
</script>
</body>
</html>