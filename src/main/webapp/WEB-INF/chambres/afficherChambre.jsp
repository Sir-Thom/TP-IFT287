<%@ page import="tp.objets.Chambre" %>
<%@ page import="tp.objets.Commodite" %>
<%@ page import="java.util.List" %>
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

  <style>
    .commodite-item {
      transition: all 0.3s ease;
      border-radius: 8px;
    }
    .commodite-item:hover {
      background-color: #f8f9fa;
      transform: translateX(5px);
    }
    .prix-commodite {
      font-size: 0.9rem;
      font-weight: 600;
    }
  </style>
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

    <!-- Résultats de la recherche -->
    <%
      Chambre chambre = (Chambre) request.getAttribute("chambre");
      @SuppressWarnings("unchecked")
      List<Commodite> commoditesChambre = (List<Commodite>) request.getAttribute("commoditesChambre");

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
              if (commoditesChambre != null && !commoditesChambre.isEmpty()) {
                double totalCommodites = 0.0;
                for (Commodite commodite : commoditesChambre) {
                  totalCommodites += commodite.getSurplusPrix();
                }
            %>
            <div class="mb-3">
              <small class="text-muted">
                <i class="fas fa-info-circle"></i>
                <%= commoditesChambre.size() %> commodité(s) • Total:
                <strong class="text-success">+<%= String.format("%.2f", totalCommodites) %> CAD/nuit</strong>
              </small>
            </div>

            <ul class="list-group list-group-flush">
              <% for (Commodite commodite : commoditesChambre) { %>
              <li class="list-group-item commodite-item d-flex justify-content-between align-items-center p-3">
                <div class="flex-grow-1">
                  <div class="d-flex align-items-center mb-1">
                    <span class="badge badge-primary badge-pill mr-2"><%= commodite.getIdCommodite() %></span>
                    <strong><%= commodite.getDescription() %></strong>
                  </div>
                  <% if (commodite.getSurplusPrix() > 0) { %>
                  <small class="text-success prix-commodite">
                    <i class="fas fa-plus-circle"></i> +<%= String.format("%.2f", commodite.getSurplusPrix()) %> CAD/nuit
                  </small>
                  <% } else { %>
                  <small class="text-muted">
                    <i class="fas fa-gift"></i> Gratuit
                  </small>
                  <% } %>
                </div>
                <div class="text-right">
                  <button class="btn btn-sm btn-outline-danger"
                          onclick="enleverCommodite(<%= commodite.getIdCommodite() %>, '<%= commodite.getDescription().replace("'", "\\'") %>')">
                    <i class="fas fa-times"></i>
                  </button>
                </div>
              </li>
              <% } %>
            </ul>

            <!-- Prix total de la chambre -->
            <div class="card bg-light mt-3">
              <div class="card-body text-center">
                <h6 class="card-title text-primary">Prix total par nuit</h6>
                <h4 class="text-success">
                  <%= String.format("%.2f", chambre.getPrixBase() + totalCommodites) %> CAD
                </h4>
                <small class="text-muted">
                  Prix de base: <%= String.format("%.2f", chambre.getPrixBase()) %> CAD +
                  Commodités: <%= String.format("%.2f", totalCommodites) %> CAD
                </small>
              </div>
            </div>

            <% } else { %>
            <div class="alert alert-light">
              <i class="fas fa-info-circle"></i> Aucune commodité associée à cette chambre
            </div>
            <% } %>

            <!-- Actions sur cette chambre -->
            <div class="mt-3">
              <h6><i class="fas fa-cogs"></i> Actions disponibles</h6>
              <div class="btn-group-vertical w-100">
                <a href="<%= request.getContextPath() %>/CommoditeServlet?action=afficherFormInclure"
                   class="btn btn-outline-success btn-sm">
                  <i class="fas fa-plus"></i> Ajouter une commodité
                </a>
                <a href="<%= request.getContextPath() %>/chambres?action=afficherFormSupprimer"
                   class="btn btn-outline-danger btn-sm">
                  <i class="fas fa-trash"></i> Supprimer cette chambre
                </a>
                <a href="<%= request.getContextPath() %>/ReservationServlet?action=afficherFormAjouter"
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
  </div>
</div>

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
    setTimeout(() => {
      document.getElementById('formRecherche').submit();
    }, 500);
  }

  function enleverCommodite(idCommodite, description) {
    if (confirm('Êtes-vous sûr de vouloir enlever la commodité "' + description + '" de cette chambre ?')) {
      // Rediriger vers le formulaire d'enlèvement avec les paramètres pré-remplis
      const chambreNom = '<%= chambre != null ? chambre.getNomChambre().replace("'", "\\'") : "" %>';
      const url = '<%= request.getContextPath() %>/CommoditeServlet?action=afficherFormEnlever&nomChambre=' +
              encodeURIComponent(chambreNom) + '&idCommodite=' + idCommodite;
      window.location.href = url;
    }
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

  // Auto-hide des alertes après 5 secondes
  setTimeout(function() {
    $('.alert').fadeOut('slow');
  }, 5000);
</script>
</body>
</html>