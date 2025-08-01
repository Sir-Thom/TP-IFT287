<%@ page import="tp.objets.Chambre" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <title>Auberge-Inn - Afficher Chambre</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

  <!-- Bootstrap -->
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
          <li class="breadcrumb-item"><a href="<%= request.getContextPath() %>/menu.jsp">Menu</a></li>
          <li class="breadcrumb-item active">Afficher Chambre</li>
        </ol>
      </nav>
    </div>
  </div>

  <div class="row">
    <div class="col-md-10 offset-md-1">
      <!-- Messages de succès/erreur -->
      <% if (request.getAttribute("message") != null) { %>
      <div class="alert alert-success alert-dismissible fade show">
        <strong><i class="fas fa-check-circle"></i> Succès!</strong> <%= request.getAttribute("message") %>
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <% } %>

      <% if (request.getAttribute("erreur") != null) { %>
      <div class="alert alert-danger alert-dismissible fade show">
        <strong><i class="fas fa-exclamation-triangle"></i> Erreur!</strong> <%= request.getAttribute("erreur") %>
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <% } %>

      <!-- Formulaire de recherche -->
      <div class="card">
        <div class="card-header bg-info text-white">
          <h3 class="mb-0"><i class="fas fa-search"></i> Rechercher une chambre</h3>
        </div>
        <div class="card-body">
          <form id="formRecherche" action="<%= request.getContextPath() %>/chambres" method="GET">
            <input type="hidden" name="action" value="afficher">

            <div class="form-group">
              <label for="nom">Nom de la chambre *</label>
              <input type="text" class="form-control" id="nom" name="nom"
                     value="<%= request.getAttribute("nomRecherche") != null ? request.getAttribute("nomRecherche") : "" %>"
                     placeholder="Ex: Chambre 101, Suite Deluxe" required>
              <small class="form-text text-muted">Saisissez le nom exact de la chambre à afficher</small>
            </div>

            <div class="form-group">
              <div class="row">
                <div class="col-md-6">
                  <button type="submit" class="btn btn-info btn-block" id="btnRechercher">
                    <i class="fas fa-search"></i> Rechercher la chambre
                  </button>
                </div>
                <div class="col-md-6">
                  <a href="<%= request.getContextPath() %>/menu.jsp" class="btn btn-secondary btn-block">
                    <i class="fas fa-arrow-left"></i> Retour au menu
                  </a>
                </div>
              </div>
            </div>
          </form>

        </div>
      </div>
    </div>

    <!-- Résultats de la recherche : -->
      <%
        Chambre chambre = (Chambre) request.getAttribute("chambre");
        if (chambre != null) {
      %>
    <div class="card mt-4">
      <div class="card-header bg-success text-white">
        <h4 class="mb-0"><i class="fas fa-check-circle"></i> Chambre trouvée !</h4>
      </div>
      <div class="card-body">
        <div class="row">
          <div class="col-md-6">
            <h5 class="text-success">
              <i class="fas fa-bed"></i> <%= chambre.getNomChambre() %>
            </h5>

            <div class="table-responsive">
              <table class="table table-borderless">
                <tbody>
                <tr>
                  <td><strong><i class="fas fa-hashtag"></i> ID Chambre :</strong></td>
                  <td><span class="badge badge-primary"><%= chambre.getIdChambre() %></span></td>
                </tr>
                <tr>
                  <td><strong><i class="fas fa-tag"></i> Nom :</strong></td>
                  <td><%= chambre.getNomChambre() %></td>
                </tr>
                <tr>
                  <td><strong><i class="fas fa-bed"></i> Type de lit :</strong></td>
                  <td>
                      <span class="badge badge-info">
                        <%= chambre.getTypeLit() %>
                      </span>
                  </td>
                </tr>
                <tr>
                  <td><strong><i class="fas fa-dollar-sign"></i> Prix de base :</strong></td>
                  <td>
                      <span class="badge badge-success">
                        <%= String.format("%.2f", chambre.getPrixBase()) %> CAD/nuit
                      </span>
                  </td>
                </tr>
                </tbody>
              </table>
            </div>
          </div>
          <div class="col-md-6">
            <h6><i class="fas fa-concierge-bell"></i> Commodités associées</h6>
            <%
              if (chambre.getCommodites() != null && !chambre.getCommodites().isEmpty()) {
            %>
            <ul class="list-group list-group-flush">
              <% for (Integer idCommodite : chambre.getCommodites()) { %>
              <li class="list-group-item d-flex justify-content-between align-items-center">
                Commodité ID <%= idCommodite %>
                <span class="badge badge-primary badge-pill"><%= idCommodite %></span>
              </li>
              <% } %>
            </ul>
            <% } else { %>
            <div class="alert alert-light">
              <i class="fas fa-info-circle"></i> Aucune commodité associée à cette chambre
            </div>
            <% } %>

            <!-- Actions sur cette chambre -->
            <div class="mt-3">
              <h6><i class="fas fa-cogs"></i> Actions disponibles</h6>
              <div class="btn-group-vertical w-100">
                <a href="<%= request.getContextPath() %>/chambres?action=afficherFormRecherche=<%= chambre.getNomChambre() %>"
                   class="btn btn-warning btn-sm">
                  <i class="fas fa-edit"></i> Modifier cette chambre
                </a>
                <a href="<%= request.getContextPath() %>/CommoditeServlet?action=afficherFormRecherche=<%= chambre.getNomChambre() %>"
                   class="btn btn-outline-success btn-sm">
                  <i class="fas fa-plus"></i> Ajouter une commodité
                </a>
                <button class="btn btn-outline-danger btn-sm"
                        onclick="confirmerSuppression('<%= chambre.getNomChambre() %>')">
                  <i class="fas fa-trash"></i> Supprimer cette chambre
                </button>
                <a href="<%= request.getContextPath() %>/ReservationServlet?action=afficherFormReserver&chambre=<%= chambre.getNomChambre() %>"
                   class="btn btn-outline-primary btn-sm">
                  <i class="fas fa-calendar-plus"></i> Réserver cette chambre
                </a>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="card-footer text-muted">
        <div class="row">
          <div class="col-md-6">
            <small>
              <i class="fas fa-clock"></i> Informations à jour •
              <i class="fas fa-shield-alt"></i> Données sécurisées
            </small>
          </div>
          <div class="col-md-6 text-right">
            <small>
              <button class="btn btn-sm btn-outline-secondary" onclick="rechercherAutreChambre()">
                <i class="fas fa-search-plus"></i> Rechercher une autre chambre
              </button>
            </small>
          </div>
        </div>
      </div>
    </div>
      <% } %>


    <!-- Scripts -->
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>

    <script>
      function rechercherChambre(nomChambre) {
        document.getElementById('nom').value = nomChambre;

        // Animation de feedback
        const input = document.getElementById('nom');
        input.style.backgroundColor = '#d4edda';
        setTimeout(() => {
          input.style.backgroundColor = '';
        }, 1000);

        // Auto-submit après un délai pour permettre de voir l'animation
        setTimeout(() => {
          document.getElementById('formRecherche').submit();
        }, 500);
      }

      function confirmerSuppression(nomChambre) {
        if (confirm('Êtes-vous sûr de vouloir supprimer la chambre "' + nomChambre + '" ?\n\nCette action est irréversible.')) {
          window.location.href = '<%= request.getContextPath() %>/chambres?action=supprimer&nom=' + encodeURIComponent(nomChambre);
        }
      }

      function rechercherAutreChambre() {
        document.getElementById('nom').value = '';
        document.getElementById('nom').focus();

        // Scroll vers le formulaire de recherche
        document.querySelector('.card').scrollIntoView({ behavior: 'smooth' });
      }

      // Animation du bouton de recherche
      document.getElementById('formRecherche').addEventListener('submit', function() {
        const btnRechercher = document.getElementById('btnRechercher');
        btnRechercher.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Recherche en cours...';
        btnRechercher.disabled = true;
      });

      // Auto-focus sur le champ de recherche si pas de résultat
      <% if (request.getAttribute("chambre") == null) { %>
      document.addEventListener('DOMContentLoaded', function() {
        document.getElementById('nom').focus();
      });
      <% } %>

      // Validation en temps réel
      document.getElementById('nom').addEventListener('input', function() {
        const nom = this.value.trim();
        if (nom.length > 0 && nom.length < 2) {
          this.setCustomValidity('Le nom doit contenir au moins 2 caractères');
        } else {
          this.setCustomValidity('');
        }
      });
    </script>
</body>
</html>