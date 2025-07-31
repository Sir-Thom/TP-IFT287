<%--
  Created by IntelliJ IDEA.
  User: zowes
  Date: 2025-07-29
  Time: 10:06 a.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="tp.objets.Chambre" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <title>Auberg-Inn - Modifier Chambre</title>
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
          <li class="breadcrumb-item"><a href="<%= request.getContextPath() %>/menu.jsp" >Menu</a></li>
          <li class="breadcrumb-item active">Modifier Chambre</li>
        </ol>
      </nav>
    </div>
  </div>



  <div class="row">
    <div class="col-md-10 offset-md-1">
      <!-- Messages de succès/erreur -->
      <% if (request.getAttribute("message") != null) { %>
      <div class="alert alert-success alert-dismissible fade show">
        <strong>Succès!</strong> <%= request.getAttribute("message") %>
        <button type="button" class="close" data-dismiss="alert">&times;</button>
      </div>
      <% } %>

      <% if (request.getAttribute("erreur") != null) { %>
      <div class="alert alert-danger alert-dismissible fade show">
        <strong>Erreur!</strong> <%= request.getAttribute("erreur") %>
        <button type="button" class="close" data-dismiss="alert">&times;</button>
      </div>
      <% } %>

      <%
        Chambre chambre = (Chambre) request.getAttribute("chambre");
        String nomRecherche = (String) request.getParameter("nom");
        if (nomRecherche == null) nomRecherche = "";
      %>

      <!-- Étape 1: Rechercher la chambre à modifier (si pas encore trouvée) -->
      <% if (chambre == null) { %>
      <div class="card">
        <div class="card-header bg-warning text-dark">
          <h3 class="mb-0"> Étape 1 : Rechercher la chambre à modifier</h3>
        </div>
        <div class="card-body">
          <div class="alert alert-info">
            <i class="fas fa-info-circle"></i>
            Commencez par rechercher la chambre que vous souhaitez modifier.
          </div>

          <form action="../../ChambreServlet" method="POST">
            <input type="hidden" name="action" value="rechercherPourModifier">

            <div class="form-group">
              <label for="nomActuel">Nom actuel de la chambre *</label>
              <input type="text" class="form-control" id="nomActuel" name="nomActuel"
                     value="<%= nomRecherche %>"
                     placeholder="Ex: Chambre 101, Suite Deluxe" required>
              <small class="form-text text-muted">Saisissez le nom exact de la chambre à modifier</small>
            </div>

            <div class="form-group">
              <button type="submit" class="btn btn-warning">
                <i class="fas fa-search"></i> Rechercher cette chambre
              </button>
            </div>
          </form>

          <!-- Suggestions -->
          <div class="form-group">
            <label>Suggestions de recherche :</label>
            <div class="row">
              <div class="col-md-3">
                <button type="button" class="btn btn-outline-warning btn-sm btn-block mb-1"
                        onclick="rechercherChambre('Chambre 101')">
                   Chambre 101
                </button>
              </div>
              <div class="col-md-3">
                <button type="button" class="btn btn-outline-warning btn-sm btn-block mb-1"
                        onclick="rechercherChambre('Suite Deluxe')">
                   Suite Deluxe
                </button>
              </div>
              <div class="col-md-3">
                <button type="button" class="btn btn-outline-warning btn-sm btn-block mb-1"
                        onclick="rechercherChambre('Chambre Familiale')">
                  Chambre Familiale
                </button>
              </div>
              <div class="col-md-3">
                <button type="button" class="btn btn-outline-warning btn-sm btn-block mb-1"
                        onclick="rechercherChambre('Chambre Standard')">
                   Chambre Standard
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <% } else { %>
      <!-- Étape 2: Formulaire de modification -->
      <div class="card">
        <div class="card-header bg-success text-white">
          <h3 class="mb-0">Étape 2 : Modifier la chambre "<%= chambre.getNomChambre() %>"</h3>
        </div>
        <div class="card-body">
          <div class="row">
            <!-- Informations actuelles -->
            <div class="col-md-4">
              <div class="card border-info">
                <div class="card-header bg-info text-white">
                  <h6 class="mb-0"> Informations actuelles</h6>
                </div>
                <div class="card-body">
                  <ul class="list-unstyled">
                    <li><strong>ID :</strong> <%= chambre.getIdChambre() %></li>
                    <li><strong>Nom :</strong> <%= chambre.getNomChambre() %></li>
                    <li><strong>Type de lit :</strong> <%= chambre.getTypeLit() %></li>
                    <li><strong>Prix :</strong> $<%= String.format("%.2f", chambre.getPrixBase()) %></li>
                  </ul>
                </div>
              </div>
            </div>

            <!-- Formulaire de modification -->
            <div class="col-md-8">
              <form action="../../ChambreServlet" method="POST" onsubmit="return confirmerModification()">
                <input type="hidden" name="action" value="modifier">
                <input type="hidden" name="nomActuel" value="<%= chambre.getNomChambre() %>">

                <div class="form-group">
                  <label for="nouveauNom">Nouveau nom de la chambre *</label>
                  <input type="text" class="form-control" id="nouveauNom" name="nouveauNom"
                         value="<%= request.getAttribute("nouveauNom") != null ? request.getAttribute("nouveauNom") : chambre.getNomChambre() %>"
                         placeholder="Ex: Chambre 102, Suite Premium" required>
                  <small class="form-text text-muted">Vous pouvez garder le même nom ou en choisir un nouveau</small>
                </div>

                <div class="form-group">
                  <label for="typeLit">Type de lit *</label>
                  <select class="form-control" id="typeLit" name="typeLit" required>
                    <option value="">Choisir un type de lit</option>
                    <option value="Simple" <%= "Simple".equals(chambre.getTypeLit()) ? "selected" : "" %>>Lit Simple</option>
                    <option value="Double" <%= "Double".equals(chambre.getTypeLit()) ? "selected" : "" %>>Lit Double</option>
                    <option value="Queen" <%= "Queen".equals(chambre.getTypeLit()) ? "selected" : "" %>>Lit Queen</option>
                    <option value="King" <%= "King".equals(chambre.getTypeLit()) ? "selected" : "" %>>Lit King</option>
                    <option value="Superposé" <%= "Superposé".equals(chambre.getTypeLit()) ? "selected" : "" %>>Lits Superposés</option>
                  </select>
                </div>

                <div class="form-group">
                  <label for="prixBase">Prix de base par nuit (CAD) *</label>
                  <div class="input-group">
                    <div class="input-group-prepend">
                      <span class="input-group-text">$</span>
                    </div>
                    <input type="number" class="form-control" id="prixBase" name="prixBase"
                           value="<%= request.getAttribute("prixBase") != null ? request.getAttribute("prixBase") : String.format("%.2f", chambre.getPrixBase()) %>"
                           step="0.01" min="0" required>
                  </div>
                  <small class="form-text text-muted">Prix sans les commodités supplémentaires</small>
                </div>

                <!-- Actions de modification rapide -->
                <div class="form-group">
                  <label>⚡ Modifications rapides :</label>
                  <div class="row">
                    <div class="col-md-6">
                      <button type="button" class="btn btn-outline-primary btn-sm btn-block mb-1"
                              onclick="upgrader('Queen', 120.00)">
                        Upgrader vers Queen ($120)
                      </button>
                      <button type="button" class="btn btn-outline-primary btn-sm btn-block mb-1"
                              onclick="upgrader('King', 150.00)">
                       Upgrader vers King ($150)
                      </button>
                    </div>
                    <div class="col-md-6">
                      <button type="button" class="btn btn-outline-success btn-sm btn-block mb-1"
                              onclick="ajusterPrix(10)">
                         +$10 au prix actuel
                      </button>
                      <button type="button" class="btn btn-outline-warning btn-sm btn-block mb-1"
                              onclick="ajusterPrix(-10)">
                        -$10 au prix actuel
                      </button>
                    </div>
                  </div>
                </div>

                <hr>

                <div class="form-group">
                  <div class="row">
                    <div class="col-md-6">
                      <button type="submit" class="btn btn-success btn-block">
                        <i class="fas fa-save"></i> Sauvegarder les modifications
                      </button>
                    </div>
                    <div class="col-md-6">
                      <a href="<%= request.getContextPath() %>/menu.jsp"  class="btn btn-secondary btn-block">
                        <i class="fas fa-times"></i> Annuler et retourner au menu
                      </a>
                    </div>
                  </div>
                </div>
              </form>
            </div>
          </div>
        </div>
        <div class="card-footer text-muted">
          <small>
            <i class="fas fa-shield-alt"></i> Modification sécurisée •
            <i class="fas fa-history"></i> Changements appliqués immédiatement
          </small>
        </div>
      </div>
      <% } %>


      <!-- Bouton de retour -->
      <div class="mb-3">
        <a href="<%= request.getContextPath() %>/menu.jsp" class="btn btn-secondary btn-block">
          <i class="fas fa-arrow-left"></i> Retour au menu principal
        </a>
      </div>
    </div>
  </div>

    </div>
  </div>
</div>

<!-- Scripts -->
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>

<script>
  function rechercherChambre(nomChambre) {
    document.getElementById('nomActuel').value = nomChambre;

    // Animation de feedback
    const input = document.getElementById('nomActuel');
    input.style.backgroundColor = '#fff3cd';
    setTimeout(() => {
      input.style.backgroundColor = '';
    }, 1000);
  }

  function upgrader(typeLit, nouveauPrix) {
    document.getElementById('typeLit').value = typeLit;
    document.getElementById('prixBase').value = nouveauPrix.toFixed(2);

    // Animation de feedback
    const selectLit = document.getElementById('typeLit');
    const inputPrix = document.getElementById('prixBase');

    selectLit.style.backgroundColor = '#d4edda';
    inputPrix.style.backgroundColor = '#d4edda';

    setTimeout(() => {
      selectLit.style.backgroundColor = '';
      inputPrix.style.backgroundColor = '';
    }, 1500);
  }

  function ajusterPrix(ajustement) {
    const inputPrix = document.getElementById('prixBase');
    const prixActuel = parseFloat(inputPrix.value) || 0;
    const nouveauPrix = Math.max(0, prixActuel + ajustement);

    inputPrix.value = nouveauPrix.toFixed(2);

    // Animation de feedback
    inputPrix.style.backgroundColor = ajustement > 0 ? '#d4edda' : '#fff3cd';
    setTimeout(() => {
      inputPrix.style.backgroundColor = '';
    }, 1000);
  }

  function confirmerModification() {
    const nomActuel = '<%= chambre != null ? chambre.getNomChambre() : "" %>';
    const nouveauNom = document.getElementById('nouveauNom').value;
    const typeLit = document.getElementById('typeLit').value;
    const prixBase = document.getElementById('prixBase').value;

    return confirm(
            'Confirmer les modifications ?\n\n' +
            'Chambre : "' + nomActuel + '" → "' + nouveauNom + '"\n' +
            'Type de lit : ' + typeLit + '\n' +
            'Prix : $' + prixBase + ' CAD/nuit\n\n' +
            'Ces changements seront appliqués immédiatement.'
    );
  }
</script>
</body>
</html>