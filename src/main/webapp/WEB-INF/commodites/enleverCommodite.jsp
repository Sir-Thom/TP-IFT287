<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="tp.objets.Commodite" %>
<%@ page import="java.util.List" %>
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
          <!-- CORRIGÉ: Lien avec scriptlet -->
          <li class="breadcrumb-item"><a href="<%= request.getContextPath() %>/menu.jsp">Menu</a></li>
          <li class="breadcrumb-item active">Enlever Commodité</li>
        </ol>
      </nav>
    </div>
  </div>

  <div class="row">
    <div class="col-md-10 offset-md-1">
      <!-- Messages de succès -->
      <% if (request.getAttribute("message") != null) { %>
      <div class="alert alert-success alert-dismissible fade show">
        <strong><i class="fas fa-check-circle"></i> Succès!</strong> <%= request.getAttribute("message") %>
        <button type="button" class="close" data-dismiss="alert">&times;</button>
      </div>
      <% } %>

      <!-- Messages d'erreur -->
      <% if (request.getAttribute("erreur") != null) { %>
      <div class="alert alert-danger alert-dismissible fade show">
        <strong><i class="fas fa-exclamation-triangle"></i> Erreur!</strong> <%= request.getAttribute("erreur") %>
        <button type="button" class="close" data-dismiss="alert">&times;</button>
      </div>
      <% } %>

      <!-- Message d'information -->
      <% if (request.getAttribute("messageInfo") != null) { %>
      <div class="alert alert-info alert-dismissible fade show">
        <strong><i class="fas fa-info-circle"></i> Information!</strong> <%= request.getAttribute("messageInfo") %>
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

          <!-- NOUVELLE SECTION: Commodités disponibles -->
          <%
            @SuppressWarnings("unchecked")
            List<Commodite> commoditesDisponibles = (List<Commodite>) request.getAttribute("commoditesDisponibles");
          %>

          <% if (commoditesDisponibles != null && !commoditesDisponibles.isEmpty()) { %>
          <div class="card border-warning mb-4">
            <div class="card-header bg-warning text-dark">
              <h6 class="mb-0"><i class="fas fa-list"></i> Commodités disponibles pour suppression (<%= commoditesDisponibles.size() %>)</h6>
            </div>
            <div class="card-body">
              <div class="row">
                <% for (Commodite commodite : commoditesDisponibles) { %>
                <div class="col-md-6 mb-2">
                  <div class="card border-light">
                    <div class="card-body py-2 px-3">
                      <div class="d-flex justify-content-between align-items-center">
                        <div>
                          <strong>ID <%= commodite.getIdCommodite() %></strong> - <%= commodite.getDescription() %>
                          <br>
                          <small class="text-danger">-$<%= String.format("%.2f", commodite.getSurplusPrix()) %>/nuit</small>
                        </div>
                        <button class="btn btn-outline-danger btn-sm"
                                onclick="selectionnerCommodite(<%= commodite.getIdCommodite() %>, '<%= commodite.getDescription() %>')">
                          <i class="fas fa-minus"></i> Sélectionner
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
                <% } %>
              </div>
            </div>
          </div>
          <% } else { %>
          <div class="alert alert-warning">
            <h6><i class="fas fa-exclamation-triangle"></i> Aucune commodité disponible</h6>
            <p class="mb-2">Il n'y a actuellement aucune commodité dans le système à enlever.</p>
            <a href="<%= request.getContextPath() %>/CommoditeServlet?action=afficherFormAjouter"
               class="btn btn-warning btn-sm">
              <i class="fas fa-plus"></i> Créer une commodité d'abord
            </a>
          </div>
          <% } %>

          <!-- CORRIGÉ: Action du formulaire avec scriptlet -->
          <form action="<%= request.getContextPath() %>/CommoditeServlet" method="POST" onsubmit="return confirmerSuppression()">
            <input type="hidden" name="action" value="enlever">

            <div class="form-group">
              <label for="chambreNom">Nom de la chambre *</label>
              <input type="text" class="form-control" id="chambreNom" name="chambreNom"
                     value="<%= request.getAttribute("chambreNom") != null ? request.getAttribute("chambreNom") : "" %>"
                     placeholder="Ex: Chambre 101, Suite Deluxe" required>
              <small class="form-text text-muted">
                Le nom exact de la chambre dont vous voulez retirer la commodité
              </small>
            </div>

            <div class="form-group">
              <label for="idCommodite">ID de la commodité à enlever *</label>
              <div class="input-group">
                <input type="number" class="form-control" id="idCommodite" name="idCommodite"
                       value="<%= request.getAttribute("idCommodite") != null ? request.getAttribute("idCommodite") : "" %>"
                       min="1" placeholder="Sélectionnez ci-dessus ou saisissez l'ID" required>
                <div class="input-group-append">
                  <button type="button" class="btn btn-outline-secondary" onclick="viderCommodite()">
                    <i class="fas fa-times"></i> Vider
                  </button>
                </div>
              </div>
              <small class="form-text text-muted">
                L'identifiant de la commodité actuellement associée à cette chambre
              </small>
            </div>

            <!-- Sélection via dropdown alternative -->
            <% if (commoditesDisponibles != null && !commoditesDisponibles.isEmpty()) { %>
            <div class="form-group">
              <label for="commoditeSelect">Ou sélectionnez dans la liste :</label>
              <select class="form-control" id="commoditeSelect" onchange="selectionnerDepuisDropdown()">
                <option value="">-- Choisir une commodité à enlever --</option>
                <% for (Commodite commodite : commoditesDisponibles) { %>
                <option value="<%= commodite.getIdCommodite() %>"
                        data-description="<%= commodite.getDescription() %>"
                        data-prix="<%= commodite.getSurplusPrix() %>">
                  ID <%= commodite.getIdCommodite() %> - <%= commodite.getDescription() %> (-$<%= String.format("%.2f", commodite.getSurplusPrix()) %>)
                </option>
                <% } %>
              </select>
            </div>
            <% } %>

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
              <small class="form-text text-muted">Exemples rapides pour tester</small>
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
                  <!-- CORRIGÉ: Lien de retour avec scriptlet -->
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
                <!-- CORRIGÉ: Lien avec scriptlet -->
                <a href="<%= request.getContextPath() %>/CommoditeServlet?action=afficherFormInclure">Inclure une commodité</a>
              </small>
            </div>
            <div class="col-md-6 text-right">
              <small>
                <i class="fas fa-plus-circle"></i>
                <!-- CORRIGÉ: Lien avec scriptlet -->
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
  // NOUVELLE FONCTION: Sélectionner une commodité depuis les cartes
  function selectionnerCommodite(idCommodite, description) {
    document.getElementById('idCommodite').value = idCommodite;

    // Animation de feedback
    const commoditeInput = document.getElementById('idCommodite');
    commoditeInput.style.backgroundColor = '#f8d7da';
    commoditeInput.style.borderColor = '#dc3545';

    setTimeout(() => {
      commoditeInput.style.backgroundColor = '';
      commoditeInput.style.borderColor = '';
    }, 1500);

    // Notification visuelle
    showNotification('Commodité sélectionnée pour suppression : ID ' + idCommodite + ' - ' + description, 'warning');
  }

  // NOUVELLE FONCTION: Sélectionner depuis la dropdown
  function selectionnerDepuisDropdown() {
    const select = document.getElementById('commoditeSelect');
    const selectedValue = select.value;
    const selectedOption = select.options[select.selectedIndex];

    if (selectedValue) {
      document.getElementById('idCommodite').value = selectedValue;

      const description = selectedOption.getAttribute('data-description');
      const prix = selectedOption.getAttribute('data-prix');

      showNotification('Sélectionné pour suppression : ' + description + ' (-$' + prix + '/nuit)', 'warning');

      // Animation
      const commoditeInput = document.getElementById('idCommodite');
      commoditeInput.style.backgroundColor = '#f8d7da';
      commoditeInput.style.borderColor = '#dc3545';

      setTimeout(() => {
        commoditeInput.style.backgroundColor = '';
        commoditeInput.style.borderColor = '';
      }, 1500);
    }
  }

  // NOUVELLE FONCTION: Vider la sélection
  function viderCommodite() {
    document.getElementById('idCommodite').value = '';
    document.getElementById('commoditeSelect').value = '';
    document.getElementById('confirmationEnlever').checked = false;
    showNotification('Sélection effacée', 'info');
  }

  function remplirFormulaire(chambreNom, idCommodite) {
    document.getElementById('chambreNom').value = chambreNom;
    document.getElementById('idCommodite').value = idCommodite;

    // Animation de feedback
    const chambreInput = document.getElementById('chambreNom');
    const commoditeInput = document.getElementById('idCommodite');

    chambreInput.style.backgroundColor = '#f8d7da';
    commoditeInput.style.backgroundColor = '#f8d7da';

    setTimeout(() => {
      chambreInput.style.backgroundColor = '';
      commoditeInput.style.backgroundColor = '';
    }, 1000);

    showNotification('Exemple rempli : ' + chambreNom + ' - Commodité ID ' + idCommodite, 'warning');
  }

  function confirmerSuppression() {
    const chambreNom = document.getElementById('chambreNom').value;
    const idCommodite = document.getElementById('idCommodite').value;

    if (!document.getElementById('confirmationEnlever').checked) {
      showNotification('Vous devez cocher la case de confirmation !', 'danger');
      return false;
    }

    return confirm(`Êtes-vous sûr de vouloir enlever la commodité ID ${idCommodite} de la chambre "${chambreNom}" ?\n\nCette action est irréversible.`);
  }

  // NOUVELLE FONCTION: Notifications toast
  function showNotification(message, type) {
    // Créer la notification
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type} alert-dismissible fade show position-fixed`;
    alertDiv.style.cssText = 'top: 20px; right: 20px; z-index: 9999; max-width: 350px;';
    alertDiv.innerHTML = `
      <strong><i class="fas fa-info-circle"></i></strong> ${message}
      <button type="button" class="close" data-dismiss="alert">&times;</button>
    `;

    // Ajouter au body
    document.body.appendChild(alertDiv);

    // Auto-suppression après 3 secondes
    setTimeout(() => {
      if (alertDiv.parentNode) {
        alertDiv.parentNode.removeChild(alertDiv);
      }
    }, 3000);
  }

  // Auto-hide des messages de succès
  $(document).ready(function() {
    $('.alert-success').delay(5000).fadeOut('slow');
  });
</script>
</body>
</html>