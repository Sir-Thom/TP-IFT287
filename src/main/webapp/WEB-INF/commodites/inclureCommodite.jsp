<%--
  Created by IntelliJ IDEA.
  User: zowes
  Date: 2025-07-29
  Time: 9:37 a.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <title>Auberg-Inn - Inclure Commodité</title>
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
          <li class="breadcrumb-item"><a href="../../menu.jsp">Menu</a></li>
          <li class="breadcrumb-item active">Inclure Commodité</li>
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
        <div class="card-header bg-success text-white">
          <h3 class="mb-0">➕ Inclure une commodité à une chambre</h3>
        </div>
        <div class="card-body">
          <div class="alert alert-info">
            <strong><i class="fas fa-info-circle"></i> Information</strong><br>
            Associez une commodité existante à une chambre spécifique.
            La commodité sera disponible pour cette chambre et ajoutera son surplus au prix de base.
          </div>

          <form action="../../CommoditeServlet" method="POST">
            <input type="hidden" name="action" value="inclure">

            <div class="form-group">
              <label for="nomChambre">Nom de la chambre *</label>
              <input type="text" class="form-control" id="nomChambre" name="nomChambre"
                     value="<%= request.getAttribute("nomChambre") != null ? request.getAttribute("nomChambre") : "" %>"
                     placeholder="Ex: Chambre 101, Suite Deluxe" required>
              <small class="form-text text-muted">
                Le nom exact de la chambre (doit exister dans le système)
              </small>
            </div>

            <div class="form-group">
              <label for="idCommodite">ID de la commodité *</label>
              <input type="number" class="form-control" id="idCommodite" name="idCommodite"
                     value="<%= request.getAttribute("idCommodite") != null ? request.getAttribute("idCommodite") : "" %>"
                     min="1" placeholder="Ex: 1, 2, 3..." required>
              <small class="form-text text-muted">
                L'identifiant numérique de la commodité (doit exister dans le système)
              </small>
            </div>

            <!-- Aide pour trouver les IDs -->
            <div class="form-group">
              <div class="card border-light">
                <div class="card-header bg-light">
                  <h6 class="mb-0"><i class="fas fa-question-circle"></i> Comment trouver les IDs ?</h6>
                </div>
                <div class="card-body">
                  <div class="row">
                    <div class="col-md-6">
                      <strong>📋 Commodités courantes :</strong>
                      <ul class="list-unstyled mt-2">
                        <li><code>ID 1</code> - WiFi haut débit</li>
                        <li><code>ID 2</code> - Climatisation</li>
                        <li><code>ID 3</code> - Mini-bar</li>
                        <li><code>ID 4</code> - Room Service 24h</li>
                        <li><code>ID 5</code> - Balcon privé</li>
                      </ul>
                    </div>
                    <div class="col-md-6">
                      <strong>🛏️ Chambres courantes :</strong>
                      <ul class="list-unstyled mt-2">
                        <li>Chambre 101, 102, 103...</li>
                        <li>Suite Deluxe</li>
                        <li>Chambre Standard</li>
                        <li>Chambre Familiale</li>
                      </ul>
                      <button type="button" class="btn btn-sm btn-outline-info" disabled>
                        <i class="fas fa-list"></i> Voir toutes les chambres (à venir)
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- Raccourcis rapides -->
            <div class="form-group">
              <label>⚡ Associations rapides :</label>
              <div class="row">
                <div class="col-md-6">
                  <button type="button" class="btn btn-outline-success btn-sm btn-block mb-1"
                          onclick="remplirFormulaire('Chambre 101', '1')">
                    📶 WiFi → Chambre 101
                  </button>
                  <button type="button" class="btn btn-outline-success btn-sm btn-block mb-1"
                          onclick="remplirFormulaire('Suite Deluxe', '2')">
                    ❄️ Climatisation → Suite Deluxe
                  </button>
                </div>
                <div class="col-md-6">
                  <button type="button" class="btn btn-outline-success btn-sm btn-block mb-1"
                          onclick="remplirFormulaire('Suite Deluxe', '3')">
                    🍷 Mini-bar → Suite Deluxe
                  </button>
                  <button type="button" class="btn btn-outline-success btn-sm btn-block mb-1"
                          onclick="remplirFormulaire('Chambre 102', '4')">
                    🛎️ Room Service → Chambre 102
                  </button>
                </div>
              </div>
              <small class="form-text text-muted">Cliquez pour un remplissage automatique</small>
            </div>

            <hr>

            <div class="form-group">
              <div class="row">
                <div class="col-md-6">
                  <button type="submit" class="btn btn-success btn-block">
                    <i class="fas fa-link"></i> Inclure la commodité
                  </button>
                </div>
                <div class="col-md-6">
                  <a href="../../menu.jsp" class="btn btn-secondary btn-block">
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
                <i class="fas fa-plus-circle"></i>
                <a href="../../CommoditeServlet?action=afficherFormAjouter">Créer une nouvelle commodité</a>
              </small>
            </div>
            <div class="col-md-6 text-right">
              <small>
                <i class="fas fa-unlink"></i>
                <a href="../../CommoditeServlet?action=afficherFormEnlever">Enlever une commodité</a>
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

    chambreInput.style.backgroundColor = '#d4edda';
    commoditeInput.style.backgroundColor = '#d4edda';

    setTimeout(() => {
      chambreInput.style.backgroundColor = '';
      commoditeInput.style.backgroundColor = '';
    }, 1000);
  }
</script>
</body>
</html>
