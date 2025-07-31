<%--
  Created by IntelliJ IDEA.
  User: zowes
  Date: 2025-07-29
  Time: 10:06 a.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <title>Auberge-Inn - Supprimer Chambre</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

  <!-- Bootstrap CSS -->
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">

  <style>
    body {
      background: url('<%= request.getContextPath() %>/assets/supprimerChambre.jpg') no-repeat center center fixed;
      background-size: cover;
      min-height: 100vh;
    }

    .form-overlay {
      background-color: rgba(255, 255, 255, 0.92);
      padding: 30px;
      border-radius: 15px;
      box-shadow: 0 4px 15px rgba(0, 0, 0, 0.4);
      margin-top: 40px;
    }

    .message-auto-hide {
      transition: opacity 0.5s ease-out;
    }
  </style>
</head>
<body>

<!-- Navigation -->
<jsp:include page="/WEB-INF/navigation.jsp" />

<!-- Contenu principal -->
<div class="container d-flex justify-content-center align-items-center flex-column" style="min-height: 100vh;">
  <div class="col-md-8 form-overlay">

    <!-- Message succès -->
    <% if (request.getAttribute("message") != null) { %>
    <div class="alert alert-success alert-dismissible fade show message-auto-hide" id="messageSuccess">
      <strong><i class="fas fa-check-circle"></i> Succès!</strong> <%= request.getAttribute("message") %>
      <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span>&times;</span></button>
    </div>
    <% } %>

    <!-- Message erreur -->
    <% if (request.getAttribute("erreur") != null) { %>
    <div class="alert alert-danger alert-dismissible fade show" id="messageError">
      <strong><i class="fas fa-exclamation-triangle"></i> Erreur!</strong> <%= request.getAttribute("erreur") %>
      <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span>&times;</span></button>
    </div>
    <% } %>

    <h3 class="text-center text-danger mb-4"><i class="fas fa-trash-alt"></i> Supprimer une chambre</h3>

    <form id="formSupprimerChambre" action="<%= request.getContextPath() %>/ChambreServlet" method="POST">
      <input type="hidden" name="action" value="supprimer">

      <div class="form-group">
        <label for="nom">Nom de la chambre à supprimer *</label>
        <input type="text" class="form-control" id="nom" name="nom"
               value="<%= request.getAttribute("nom") != null ? request.getAttribute("nom") : "" %>"
               placeholder="Ex: Chambre 101" required>
      </div>

      <hr>

      <div class="form-row">
        <div class="col-md-6 mb-2">
          <button type="submit" class="btn btn-danger btn-block" id="btnSubmit">
            <i class="fas fa-trash-alt"></i> Supprimer
          </button>
        </div>
        <div class="col-md-6 mb-2">
          <a href="<%= request.getContextPath() %>/menu.jsp" class="btn btn-secondary btn-block">
            <i class="fas fa-arrow-left"></i> Retour
          </a>
        </div>
      </div>
    </form>

  </div>
</div>

<!-- Scripts Bootstrap -->
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>

<script>
  function remplirNomChambre(nomChambre) {
    document.getElementById('nom').value = nomChambre;

    // Animation de feedback
    const input = document.getElementById('nom');
    input.style.backgroundColor = '#f8d7da';
    setTimeout(() => {
      input.style.backgroundColor = '';
    }, 1000);
  }

  function viderFormulaire() {
    document.getElementById('formSupprimerChambre').reset();
  }

  // Auto-hide du message de succès (comme la page d'ajout)
  <% if (request.getAttribute("message") != null) { %>
  setTimeout(function () {
    const message = document.getElementById('messageSuccess');
    if (message) {
      message.style.opacity = '0';
      setTimeout(() => message.style.display = 'none', 500);
    }
    // Vider le formulaire après succès
    viderFormulaire();
  }, 5000);
  <% } %>

  // Animation du bouton lors de la soumission
  document.getElementById('formSupprimerChambre').addEventListener('submit', function () {
    const btn = document.getElementById('btnSubmit');
    btn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Suppression en cours...';
    btn.disabled = true;
  });
</script>
</body>
</html>