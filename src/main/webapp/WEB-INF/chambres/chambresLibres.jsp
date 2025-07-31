<%@ page import="tp.objets.Chambre" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.time.LocalDate" %>
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
  <!-- Font Awesome pour les icônes -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">

  <style>
    .chambre-card {
      transition: transform 0.2s, box-shadow 0.2s;
      cursor: pointer;
    }
    .chambre-card:hover {
      transform: translateY(-3px);
      box-shadow: 0 4px 15px rgba(0,0,0,0.1);
    }
    .prix-total {
      font-size: 1.2em;
      font-weight: bold;
    }
    .prix-detail {
      font-size: 0.9em;
      color: #6c757d;
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
          <li class="breadcrumb-item active">Chambres Libres</li>
        </ol>
      </nav>
    </div>
  </div>

  <!-- Messages de succès/erreur -->
  <% if (request.getAttribute("message") != null) { %>
  <div class="alert alert-success alert-dismissible fade show">
    <strong><i class="fas fa-check-circle"></i> Succès!</strong> <%= request.getAttribute("message") %>
    <button type="button" class="close" data-dismiss="alert">&times;</button>
  </div>
  <% } %>

  <% if (request.getAttribute("erreur") != null) { %>
  <div class="alert alert-danger alert-dismissible fade show">
    <strong><i class="fas fa-exclamation-triangle"></i> Erreur!</strong> <%= request.getAttribute("erreur") %>
    <button type="button" class="close" data-dismiss="alert">&times;</button>
  </div>
  <% } %>

  <!-- Formulaire de recherche -->
  <div class="row">
    <div class="col-md-8 offset-md-2">
      <div class="card">
        <div class="card-header bg-primary text-white">
          <h3 class="mb-0"><i class="fas fa-search"></i> Rechercher les chambres disponibles</h3>
        </div>
        <div class="card-body">
          <div class="alert alert-info">
            <i class="fas fa-info-circle"></i>
            <strong>Information :</strong> Sélectionnez vos dates d'arrivée et de départ pour voir les chambres disponibles avec leurs prix complets (base + commodités).
          </div>

          <form id="formRecherche" action="<%= request.getContextPath() %>/ChambreServlet" method="POST">
            <input type="hidden" name="action" value="chambresLibres">

            <div class="row">
              <div class="col-md-6">
                <div class="form-group">
                  <label for="dateDebut"><i class="fas fa-calendar-plus"></i> Date d'arrivée *</label>
                  <input type="date" class="form-control" id="dateDebut" name="dateDebut"
                         value="<%= request.getAttribute("dateDebut") != null ? request.getAttribute("dateDebut") : LocalDate.now().toString() %>"
                         min="<%= LocalDate.now().toString() %>" required>
                </div>
              </div>
              <div class="col-md-6">
                <div class="form-group">
                  <label for="dateFin"><i class="fas fa-calendar-minus"></i> Date de départ *</label>
                  <input type="date" class="form-control" id="dateFin" name="dateFin"
                         value="<%= request.getAttribute("dateFin") != null ? request.getAttribute("dateFin") : LocalDate.now().plusDays(1).toString() %>"
                         min="<%= LocalDate.now().plusDays(1).toString() %>" required>
                </div>
              </div>
            </div>

            <!-- Suggestions de dates rapides -->
            <div class="form-group">
              <label>⚡ Suggestions rapides :</label>
              <div class="row">
                <div class="col-md-4">
                  <button type="button" class="btn btn-outline-primary btn-sm btn-block mb-1"
                          onclick="definirDates(0, 1)">
                    🌙 Ce soir (1 nuit)
                  </button>
                </div>
                <div class="col-md-4">
                  <button type="button" class="btn btn-outline-primary btn-sm btn-block mb-1"
                          onclick="definirDates(0, 2)">
                    🏖️ Week-end (2 nuits)
                  </button>
                </div>
                <div class="col-md-4">
                  <button type="button" class="btn btn-outline-primary btn-sm btn-block mb-1"
                          onclick="definirDates(0, 7)">
                    📅 Semaine (7 nuits)
                  </button>
                </div>
              </div>
            </div>

            <div class="form-group text-center">
              <button type="submit" class="btn btn-primary btn-lg" id="btnRechercher">
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
    @SuppressWarnings("unchecked")
    List<Chambre> chambresLibres = (List<Chambre>) request.getAttribute("chambresLibres");
    @SuppressWarnings("unchecked")
    Map<Integer, Double> prixTotaux = (Map<Integer, Double>) request.getAttribute("prixTotaux");
    String dateDebut = (String) request.getAttribute("dateDebut");
    String dateFin = (String) request.getAttribute("dateFin");
    Long nombreNuits = (Long) request.getAttribute("nombreNuits");
  %>

  <% if (dateDebut != null && dateFin != null) { %>
  <div class="row mt-4">
    <div class="col">
      <div class="card">
        <div class="card-header bg-info text-white">
          <div class="row">
            <div class="col-md-8">
              <h4 class="mb-0">
                <i class="fas fa-calendar-check"></i> Résultats pour du <%= dateDebut %> au <%= dateFin %>
              </h4>
              <% if (nombreNuits != null) { %>
              <small><%= nombreNuits %> nuit<%= nombreNuits > 1 ? "s" : "" %></small>
              <% } %>
            </div>
            <div class="col-md-4 text-right">
              <% if (chambresLibres != null) { %>
              <span class="badge badge-light badge-lg">
                <i class="fas fa-bed"></i> <%= chambresLibres.size() %> chambre<%= chambresLibres.size() > 1 ? "s" : "" %> disponible<%= chambresLibres.size() > 1 ? "s" : "" %>
              </span>
              <% } %>
            </div>
          </div>
        </div>
        <div class="card-body">
          <% if (chambresLibres != null && !chambresLibres.isEmpty()) { %>
          <div class="alert alert-success">
            <strong><i class="fas fa-thumbs-up"></i> Excellent!</strong>
            <%= chambresLibres.size() %> chambre<%= chambresLibres.size() > 1 ? "s sont" : " est" %> disponible<%= chambresLibres.size() > 1 ? "s" : "" %> pour ces dates.
          </div>

          <div class="row">
            <% for (Chambre chambre : chambresLibres) {
              Double prixTotal = prixTotaux != null ? prixTotaux.get(chambre.getIdChambre()) : chambre.getPrixBase();
              if (prixTotal == null) prixTotal = chambre.getPrixBase();

              // Calculer le prix pour le séjour complet
              double prixSejour = nombreNuits != null ? prixTotal * nombreNuits : prixTotal;
            %>
            <div class="col-md-6 col-lg-4 mb-3">
              <div class="card border-success chambre-card h-100">
                <div class="card-header bg-light">
                  <h5 class="card-title text-success mb-0">
                    <i class="fas fa-bed"></i> <%= chambre.getNomChambre() %>
                  </h5>
                </div>
                <div class="card-body">
                  <div class="row">
                    <div class="col-12">
                      <p class="card-text">
                        <strong><i class="fas fa-bed"></i> Type de lit:</strong>
                        <span class="badge badge-info"><%= chambre.getTypeLit() %></span>
                      </p>

                      <!-- Détails du prix -->
                      <div class="border p-2 mb-2 bg-light">
                        <div class="prix-detail">
                          <i class="fas fa-dollar-sign"></i> Prix de base: $<%= String.format("%.2f", chambre.getPrixBase()) %>/nuit
                        </div>
                        <% if (prixTotal > chambre.getPrixBase()) { %>
                        <div class="prix-detail">
                          <i class="fas fa-plus"></i> Commodités: +$<%= String.format("%.2f", prixTotal - chambre.getPrixBase()) %>/nuit
                        </div>
                        <hr class="my-1">
                        <% } %>
                        <div class="prix-total text-success">
                          <i class="fas fa-tag"></i> Total/nuit: $<%= String.format("%.2f", prixTotal) %> CAD
                        </div>
                        <% if (nombreNuits != null && nombreNuits > 1) { %>
                        <div class="prix-total text-primary">
                          <i class="fas fa-calendar"></i> Total séjour: $<%= String.format("%.2f", prixSejour) %> CAD
                          <small>(<%= nombreNuits %> nuits)</small>
                        </div>
                        <% } %>
                      </div>

                      <!-- Commodités -->
                      <% if (chambre.getCommodites() != null && !chambre.getCommodites().isEmpty()) { %>
                      <div class="mb-2">
                        <small class="text-muted">
                          <i class="fas fa-concierge-bell"></i> Commodités:
                          <% for (Integer idCommodite : chambre.getCommodites()) { %>
                          <span class="badge badge-secondary badge-sm"><%= idCommodite %></span>
                          <% } %>
                        </small>
                      </div>
                      <% } %>
                    </div>
                  </div>
                </div>
                <div class="card-footer">
                  <div class="btn-group w-100">
                    <a href="<%= request.getContextPath() %>/ReservationServlet?action=afficherFormReserver&chambre=<%= chambre.getNomChambre() %>&dateDebut=<%= dateDebut %>&dateFin=<%= dateFin %>"
                       class="btn btn-success btn-sm">
                      <i class="fas fa-calendar-plus"></i> Réserver
                    </a>
                    <a href="<%= request.getContextPath() %>/ChambreServlet?action=afficher&nom=<%= chambre.getNomChambre() %>"
                       class="btn btn-outline-info btn-sm">
                      <i class="fas fa-info-circle"></i> Détails
                    </a>
                  </div>
                </div>
              </div>
            </div>
            <% } %>
          </div>

          <!-- Résumé financier -->
          <% if (chambresLibres.size() > 0) { %>
          <div class="card bg-light mt-3">
            <div class="card-body">
              <h6 class="card-title"><i class="fas fa-chart-bar"></i> Résumé des prix</h6>
              <div class="row">
                <%
                  double prixMin = Double.MAX_VALUE;
                  double prixMax = Double.MIN_VALUE;
                  double prixMoyen = 0;

                  for (Chambre chambre : chambresLibres) {
                    Double prix = prixTotaux != null ? prixTotaux.get(chambre.getIdChambre()) : chambre.getPrixBase();
                    if (prix == null) prix = chambre.getPrixBase();

                    if (prix < prixMin) prixMin = prix;
                    if (prix > prixMax) prixMax = prix;
                    prixMoyen += prix;
                  }
                  prixMoyen = prixMoyen / chambresLibres.size();
                %>
                <div class="col-md-4">
                  <strong>Prix minimum:</strong> $<%= String.format("%.2f", prixMin) %>/nuit
                </div>
                <div class="col-md-4">
                  <strong>Prix maximum:</strong> $<%= String.format("%.2f", prixMax) %>/nuit
                </div>
                <div class="col-md-4">
                  <strong>Prix moyen:</strong> $<%= String.format("%.2f", prixMoyen) %>/nuit
                </div>
              </div>
            </div>
          </div>
          <% } %>

          <% } else if (chambresLibres != null) { %>
          <div class="alert alert-warning text-center">
            <h5><i class="fas fa-calendar-times"></i> Aucune chambre disponible</h5>
            <p>Toutes nos chambres sont réservées pour la période sélectionnée.</p>
            <p class="mb-0">Essayez des dates différentes ou
              <a href="<%= request.getContextPath() %>/ClientServlet?action=afficherFormAjouter" class="alert-link">
                enregistrez-vous sur notre liste d'attente
              </a>
            </p>
          </div>
          <% } %>
        </div>
      </div>
    </div>
  </div>
  <% } %>

  <!-- Actions supplémentaires -->
  <div class="row mt-4">
    <div class="col">
      <div class="card">
        <div class="card-body">
          <h6 class="card-title"><i class="fas fa-tools"></i> Actions rapides</h6>
          <div class="row">
            <div class="col-md-3">
              <a href="<%= request.getContextPath() %>/ChambreServlet?action=afficherFormAjouter" class="btn btn-success btn-sm btn-block">
                <i class="fas fa-plus"></i> Ajouter une chambre
              </a>
            </div>
            <div class="col-md-3">
              <a href="<%= request.getContextPath() %>/CommoditeServlet?action=afficherFormAjouter" class="btn btn-warning btn-sm btn-block">
                <i class="fas fa-concierge-bell"></i> Ajouter commodité
              </a>
            </div>
            <div class="col-md-3">
              <a href="<%= request.getContextPath() %>/ClientServlet?action=afficherFormAjouter" class="btn btn-primary btn-sm btn-block">
                <i class="fas fa-user-plus"></i> Ajouter client
              </a>
            </div>
            <div class="col-md-3">
              <a href="<%= request.getContextPath() %>/menu.jsp" class="btn btn-secondary btn-sm btn-block">
                <i class="fas fa-arrow-left"></i> Retour au menu
              </a>
            </div>
          </div>
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
  // Fonction pour définir des dates rapides
  function definirDates(joursDebut, joursTotal) {
    const aujourd = new Date();
    const dateDebut = new Date(aujourd);
    dateDebut.setDate(aujourd.getDate() + joursDebut);

    const dateFin = new Date(dateDebut);
    dateFin.setDate(dateDebut.getDate() + joursTotal);

    document.getElementById('dateDebut').value = dateDebut.toISOString().split('T')[0];
    document.getElementById('dateFin').value = dateFin.toISOString().split('T')[0];

    // Animation de feedback
    const inputs = [document.getElementById('dateDebut'), document.getElementById('dateFin')];
    inputs.forEach(input => {
      input.style.backgroundColor = '#d4edda';
      setTimeout(() => {
        input.style.backgroundColor = '';
      }, 1000);
    });
  }

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

  // Animation du bouton de recherche
  document.getElementById('formRecherche').addEventListener('submit', function() {
    const btnRechercher = document.getElementById('btnRechercher');
    btnRechercher.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Recherche en cours...';
    btnRechercher.disabled = true;
  });

  // Animation des cartes de chambres
  document.querySelectorAll('.chambre-card').forEach(card => {
    card.addEventListener('click', function() {
      const reserverBtn = this.querySelector('.btn-success');
      if (reserverBtn) {
        reserverBtn.style.backgroundColor = '#28a745';
        setTimeout(() => {
          reserverBtn.style.backgroundColor = '';
        }, 200);
      }
    });
  });
</script>
</body>
</html>