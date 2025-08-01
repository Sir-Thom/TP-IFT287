<%--
  Created by IntelliJ IDEA.
  User: zowes
  Date: 2025-07-29
  Time: 9:38 a.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <title>Auberg-Inn - Enlever Commodité</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

  <!-- Bootstrap CSS -->
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
          <li class="breadcrumb-item active">Enlever Commodité</li>
        </ol>
      </nav>
    </div>
  </div>

  <div class="row">
    <div class="col-md-8 offset-md-2">
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
        <div class="card-header bg-danger text-white">
          <h3 class="mb-0">➖ Enlever une commodité d'une chambre</h3>
        </div>
        <div class="card-body">
          <div class="alert alert-warning">
            <strong><i class="fas fa-exclamation-triangle"></i> Attention</strong><br>
            Cette action va retirer la commodité de la chambre spécifiée.
            Le surplus de prix ne sera plus appliqué pour cette chambre.
          </div>

          <form action="<%= request.getContextPath() %>/CommoditeServlet" method="POST" onsubmit="return confirmerSuppression()">
            <input type="hidden" name="action" value="enlever">

            <div class="form-group">
              <label for="nomChambre">Nom de la chambre *</label>
              <input type="text" class="form-control" id="nomChambre" name="nomChambre"
                     value="<%= request.getAttribute("nomChambre") != null ? request.getAttribute("nomChambre") : "" %>"
                     placeholder="Ex: Chambre 101, Suite Deluxe" required>
              <small class="form-text text-muted">
                Le nom exact de la chambre dont vous voulez retirer la commodité
              </small>
            </div>

            <div class="form-group">
              <label for="idCommodite">ID de la commodité à enlever *</label>
              <input type="number" class="form-control" id="idCommodite" name="idCommodite"
                     value="<%= request.getAttribute("idCommodite") != null ? request.getAttribute("idCommodite") : "" %>"
                     min="1" placeholder="Ex: 1, 2, 3..." required>
              <small class="form-text text-muted">
                L'identifiant de la commodité actuellement associée à cette chambre
              </small>
            </div>

            <!-- Aide pour identifier les commodités -->
            <div class="form-group">
              <div class="card border-warning">
                <div class="card-header bg-warning text-dark">
                  <h6 class="mb-0"><i class="fas fa-search"></i> Identifier la commodité à enlever</h6>
                </div>
                <div class="card-body">
                  <div class="row">
                    <div class="col-md-6">
                      <strong>📋 Commodités par ID :</strong>
                      <ul class="list-unstyled mt-2">
                        <li><span class="badge badge-primary">1</span> WiFi haut débit</li>
                        <li><span class="badge badge-primary">2</span> Climatisation</li>
                        <li><span class="badge badge-primary">3</span> Mini-bar</li>
                        <li><span class="badge badge-primary">4</span> Room Service 24h</li>
                        <li><span class="badge badge-primary">5</span> Balcon privé</li>
                        <li><span class="badge badge-primary">6</span> Parking</li>
                      </ul>
                    </div>
                    <div class="col-md-6">
                      <strong>⚠️ Vérification recommandée :</strong>
                      <p class="text-muted mt-2">
                        Assurez-vous que la chambre possède bien cette commodité
                        avant de tenter de l'enlever.
                      </p>
                      <a href="<%= request.getContextPath() %>/ChambreServlet?action=afficherFormRecherche" class="btn btn-sm btn-outline-info">
                        <i class="fas fa-eye"></i> Voir détails d'une chambre
                      </a>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- Actions rapides -->
            <div class="form-group">
              <label>⚡ Retraits rapides :</label>
              <div class="row">
                <div class="col-md-6">
                  <button type="button" class="btn btn-outline-danger btn-sm btn-block mb-1"
                          onclick="remplirFormulaire('Suite Deluxe', '3')">
                    🍷 Enlever Mini-bar de Suite Deluxe
                  </button>
                  <button type="button" class="btn btn-outline-danger btn-sm btn-block mb-1"
                          onclick="remplirFormulaire('Chambre 101', '1')">
                    📶 Enlever WiFi de Chambre 101
                  </button>
                </div>
                <div class="col-md-6">
                  <button type="button" class="btn btn-outline-danger btn-sm btn-block mb-1"
                          onclick="remplirFormulaire('Suite Deluxe', '2')">
                    ❄️ Enlever Climatisation de Suite Deluxe
                  </button>
                  <button type="button" class="btn btn-outline-danger btn-sm btn-block mb-1"
                          onclick="remplirFormulaire('Chambre 102', '4')">
                    🛎️ Enlever Room Service de Chambre 102
                  </button>
                </div>
              </div>
              <small class="form-text text-muted">Cliquez pour un remplissage automatique</small>
            </div>

            <!-- Zone de confirmation -->
            <div class="alert alert-light border">
              <div class="form-check">
                <input class="form-check-input" type="checkbox" value="" id="confirmationEnlever" required>
                <label class="form-check-label" for="confirmationEnlever">
                  <strong>Je confirme vouloir enlever cette commodité de cette chambre</strong>
                </label>
              </div>
            </div>

            <hr>

            <div class="form-group">
              <div class="row">
                <div class="col-md-6">
                  <button type="submit" class="btn btn-danger btn-block">
                    <i class="fas fa-unlink"></i> Enlever la commodité
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
        <div class="card-footer text-muted">
          <div class="row">
            <div class="col-md-6">
              <small>
                <i class="fas fa-link"></i>
                <a href="<%= request.getContextPath() %>/CommoditeServlet?action=afficherFormInclure">Inclure une commodité</a>
              </small>
            </div>
            <div class="col-md-6 text-right">
              <small>
                <i class="fas fa-plus-circle"></i>
                <a href="<%= request.getContextPath() %>/CommoditeServlet?action=afficherFormAjouter">Créer une nouvelle commodité</a>
              </small>
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
  function remplirFormulaire(nomChambre, idCommodite) {
    document.getElementById('nomChambre').value = nomChambre;
    document.getElementById('idCommodite').value = idCommodite;

    // Animation de feedback
    const chambreInput = document.getElementById('nomChambre');
    const commoditeInput = document.getElementById('idCommodite');

    chambreInput.style.backgroundColor = '#f8d7da';
    commoditeInput.style.backgroundColor = '#f8d7da';

    setTimeout(() => {
      chambreInput.style.backgroundColor = '';
      commoditeInput.style.backgroundColor = '';
    }, 1000);
  }

  function confirmerSuppression() {
    const nomChambre = document.getElementById('nomChambre').value;
    const idCommodite = document.getElementById('idCommodite').value;

    return confirm(`Êtes-vous sûr de vouloir enlever la commodité ID ${idCommodite} de la chambre "${nomChambre}" ?`);
  }
</script>
</body>
</html>