<%--
  Page principale de gestion Auberg-Inn
  Auteur : zowes
  Date : 28 juillet 2025
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
  // Vérifier la configuration BD
  if (!com.servlet.TP.InnHelper.infoBDValide(application)) {
    response.sendRedirect("index.jsp");
    return;
  }
%>
<!DOCTYPE html>
<html lang="fr">
<head>
  <title>Auberg-Inn • Tableau de bord</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <!-- Bootstrap & icones  -->
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">

  <!-- Les styles ! -->
  <style>
    body {
      background: #f9fafc;
      font-family: 'Segoe UI', Roboto, sans-serif;
      color: #343a40;
    }
    .jumbotron {
      background: linear-gradient(135deg, #87CEEB 0%, #6CA6CD 100%); /* Bleu clair */
      padding: 3rem 2rem;
      border-radius: 0;
      box-shadow: 0 5px 15px rgba(0,0,0,0.2);
    }
    .jumbotron h1 {
      font-weight: 700;
      letter-spacing: 1px;
    }
    .menu-section {
      margin-bottom: 3rem;
    }
    .menu-section h2 {
      font-size: 1.6rem;
      border-bottom: 2px solid #eaeaea;
      padding-bottom: .5rem;
      margin-bottom: 1.5rem;
      color: #87CEEB;
    }
    .menu-card {
      border: none;
      border-radius: 10px;
      background: #ffffff;
      transition: transform .2s, box-shadow .2s;
      height: 100%;
    }
    .menu-card:hover {
      transform: translateY(-6px);
      box-shadow: 0 6px 25px rgba(0,0,0,0.1);
    }
    .menu-card .card-body h5 {
      font-weight: 600;
    }
    .alert-secondary {
      background: #f1f3f5;
      border: none;
      border-radius: 8px;
    }
    .btn-outline-warning, .btn-outline-secondary {
      border-width: 2px;
    }
    .myBtn {
        background-color: #87CEEB;
        border-color: #87CEEB;

    }
  </style>

</head>
<body>

<div class="container-fluid px-0">

  <!-- Navigation -->
  <jsp:include page="/WEB-INF/navigation.jsp" />

  <!-- Header -->
  <div class="jumbotron text-center text-white mb-4">
    <h1 class="display-4"><i class="fas fa-hotel"></i> Auberg-Inn</h1>
  </div>

  <div class="container">

    <!-- Chambres -->
    <div class="menu-section">
      <h2><i class="fas fa-bed"></i> Gestion des Chambres</h2>
      <div class="row">
        <div class="col-md-4 mb-4">
          <div class="card menu-card text-center">
            <div class="card-body">
              <h5>Ajouter</h5>
              <p>Créer une nouvelle chambre</p>
              <a href="ChambreServlet?action=afficherFormAjouter" class="btn myBtn">Accéder</a>
            </div>
          </div>
        </div>

        <div class="col-md-4 mb-4">
          <div class="card menu-card text-center">
            <div class="card-body">
              <h5> Disponibilités</h5>
              <p>Voir les chambres libres par date</p>
              <a href="ChambreServlet?action=afficherFormChambresLibres" class="btn myBtn">Accéder</a>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Clients -->
    <!-- Ajouter un client -->
    <div class="menu-section">
      <h2><i class="fas fa-users"></i> Gestion des Clients</h2>
      <div class="row">
        <div class="col-md-4 mb-4">
          <div class="card menu-card text-center">
            <div class="card-body">
              <h5> Ajouter</h5>
              <p>Enregistrer un nouveau client</p>
              <a href="ClientServlet?action=afficherFormAjouter" class="btn myBtn">Accéder</a>
            </div>
          </div>
        </div>
    <!-- Afficher le client -->
        <div class="col-md-4 mb-4">
          <div class="card menu-card text-center">
            <div class="card-body">
              <h5> Liste</h5>
              <p>Afficher tous les clients enregistrés</p>
              <a href="ClientServlet?action=lister" class="btn myBtn">Accéder</a>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- Supprimer un client -->
    <div class="col-md-4 mb-4">
      <div class="card menu-card text-center">
        <div class="card-body">
          <h5> Liste</h5>
          <p>Afficher tous les clients enregistrés</p>
          <a href="ClientServlet?action=lister" class="btn myBtn">Accéder</a>
        </div>
      </div>
    </div>
  </div>
</div>


    <!-- Commodités -->
    <div class="menu-section">
      <h2><i class="fas fa-concierge-bell"></i> Gestion des Commodités</h2>
      <div class="row">
        <div class="col-md-4 mb-4">
          <div class="card menu-card text-center">
            <div class="card-body">
              <h5> Créer</h5>
              <p>Nouvelle commodité</p>
              <a href="CommoditeServlet?action=afficherFormAjouter" class="btn myBtn">Accéder</a>
            </div>
          </div>
        </div>
        <div class="col-md-4 mb-4">
          <div class="card menu-card text-center">
            <div class="card-body">
              <h5> Ajouter à une chambre</h5>
              <p>Associer commodités</p>
              <a href="CommoditeServlet?action=afficherFormInclure" class="btn myBtn">Accéder</a>
            </div>
          </div>
        </div>
        <div class="col-md-4 mb-4">
          <div class="card menu-card text-center">
            <div class="card-body">
              <h5> Retirer d'une chambre</h5>
              <p>Enlever commodités</p>
              <a href="CommoditeServlet?action=afficherFormEnlever" class="btn myBtn">Accéder</a>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Réservations -->
    <div class="menu-section">
      <h2><i class="fas fa-calendar-check"></i> Gestion des Réservations</h2>
      <div class="row">
        <div class="col-md-4 mb-4">
          <div class="card menu-card text-center">
            <div class="card-body">
              <h5> Nouvelle</h5>
              <p>Créer une réservation</p>
              <a href="ReservationServlet?action=afficherFormAjouter" class="btn myBtn">Accéder</a>
            </div>
          </div>
        </div>
        <div class="col-md-4 mb-4">
          <div class="card menu-card text-center">
            <div class="card-body">
              <h5>Liste</h5>
              <p>Voir toutes les réservations</p>
              <a href="ReservationServlet?action=lister" class="btn myBtn">Accéder</a>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Configuration système -->
    <div class="alert alert-secondary mt-4">
      <div class="row align-items-center">
        <div class="col-md-6">
          <h5 class="mb-1"> Configuration Système</h5>
          <small><strong>Base de données :</strong>
            <%= application.getAttribute("serveur") %> — <%= application.getAttribute("bd") %>
          </small>
        </div>
        <div class="col-md-6 text-md-right mt-3 mt-md-0">
          <a href="Connexion?action=reconfigurer" class="btn btn-outline-warning btn-sm mr-2">
            <i class="fas fa-tools"></i> Reconfigurer
          </a>
          <a href="Connexion?action=deconnecter" class="btn btn-outline-secondary btn-sm">
            <i class="fas fa-sign-out-alt"></i> Déconnexion
          </a>
        </div>
      </div>
    </div>

    <!-- Erreurs -->
    <jsp:include page="/WEB-INF/messageErreur.jsp" />

  </div>
</div>

<!-- Scripts -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
