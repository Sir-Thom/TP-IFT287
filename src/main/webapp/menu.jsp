<%--
  Page principale de gestion Auberg-Inn
  Auteur : zowes
  Date : 28 juillet 2025
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
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
  <!-- google-->
  <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200&icon_names=hotel" />

  <!-- Les styles ! -->
  <style>
    body {
      background: #252459;
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
      color: #231a8a;
    }
    .menu-card {
      border: 1px solid #252459;
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
      background-color: #252459;
      border-color: #9591F2;
      color: #FFFFFF;
    }

     .material-symbols-outlined {
       font-variation-settings:
               'FILL' 0,
               'wght' 400,
               'GRAD' 0,
               'opsz' 24
     }

  </style>
</head>
<body>

<div class="container-fluid px-0">

  <!-- La navigation -->
  <jsp:include page="/WEB-INF/navigation.jsp" />

  <!-- En tete -->
  <div class="jumbotron text-center text-white mb-4">
    <h1 class="display-4"><i class="fas fa-hotel"></i> Auberge-Inn</h1>
  </div>

  <div class="container">


    <!-- ============== GESTION DES CHAMBRES ============== -->
    <div class="menu-section">
      <h2 style="color:white;"><span class="material-symbols-outlined" style="color:white;" >hotel</span> Gestion des Chambres</h2>
      <div class="row">
        <!-- AJOUTER CHAMBRE -->
        <div class="col-md-4 mb-4">
          <div class="card menu-card text-center">
            <div class="card-body">
              <h5>Ajouter une nouvelle chambre</h5>
              <a href="chambres?action=afficherFormAjouter" class="btn myBtn">Accéder</a>
            </div>
          </div>
        </div>

        <!-- SUPPRIMER CHAMBRE -->
        <div class="col-md-4 mb-4">
          <div class="card menu-card text-center">
            <div class="card-body">
              <h5>Supprimer une chambre</h5>
              <a href="chambres?action=afficherFormSupprimer" class="btn myBtn">Accéder</a>
            </div>
          </div>
        </div>

        <!-- AFFICHER CHAMBRE -->
        <div class="col-md-4 mb-4">
          <div class="card menu-card text-center">
            <div class="card-body">
              <h5>Voir une chambre</h5>
              <a href="chambres?action=afficherFormRecherche" class="btn myBtn">Accéder</a>
            </div>
          </div>
        </div>

        <!-- CHAMBRES LIBRES -->
        <div class="col-md-4 mb-4">
          <div class="card menu-card text-center">
            <div class="card-body">
              <h5>Afficher les chambres libres</h5>
              <a href="chambres?action=afficherFormChambresLibres" class="btn myBtn">Accéder</a>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- ============== GESTION DES CLIENTS ============== -->
    <div class="menu-section">
      <h2 style="color:white;"><i class="fas fa-users" style="color:white;"></i> Gestion des Clients</h2>
      <div class="row">
        <!-- AJOUTER CLIENT -->
        <div class="col-md-4 mb-4">
          <div class="card menu-card text-center">
            <div class="card-body">
              <h5>Ajouter un client</h5>
              <a href="client?action=afficherFormAjouter" class="btn myBtn">Accéder</a>
            </div>
          </div>
        </div>
        <!-- RETIRER CLIENT -->
        <div class="col-md-4 mb-4">
          <div class="card menu-card text-center">
            <div class="card-body">
              <h5>Retirer un client</h5>
              <a href="client?action=afficherFormRetirer" class="btn myBtn">Accéder</a>
            </div>
          </div>
        </div>


        <!-- AFFICHER CLIENTS -->
        <div class="col-md-4 mb-4">
          <div class="card menu-card text-center">
            <div class="card-body">
              <h5>Afficher un client</h5>
              <p> Affiche toutes les informations sur un client, incluant les réservations présentes et
                passées. Les réservations contiennent le prix total de la réservation, sans les taxes.</p>
              <a href="client?action=lister" class="btn myBtn">Accéder</a>
            </div>
          </div>
        </div>
      </div>
    </div>


    <!-- ============== GESTION DES COMMODITÉS ============== -->
    <div class="menu-section">
      <h2 style="color:white;"><i class="fas fa-concierge-bell" style="color:white;"></i> Gestion des Commodités</h2>
      <div class="row">
        <!-- AJOUTER COMMODITÉ -->
        <div class="col-md-4 mb-4">
          <div class="card menu-card text-center">
            <div class="card-body">
              <h5>Ajouter une commodité</h5>
              <a href="CommoditeServlet?action=afficherFormAjouter" class="btn myBtn">Accéder</a>
            </div>
          </div>
        </div>

        <!-- INCLURE COMMODITÉ -->
        <div class="col-md-4 mb-4">
          <div class="card menu-card text-center">
            <div class="card-body">
              <h5>Inclure une commodité</h5>
              <a href="CommoditeServlet?action=afficherFormInclure" class="btn myBtn">Accéder</a>
            </div>
          </div>
        </div>

        <!-- ENLEVER COMMODITÉ -->
        <div class="col-md-4 mb-4">
          <div class="card menu-card text-center">
            <div class="card-body">
              <h5>Enlever une commodité</h5>
              <a href="CommoditeServlet?action=afficherFormEnlever" class="btn myBtn">Accéder</a>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- ============== GESTION DES RÉSERVATIONS ============== -->
    <div class="menu-section">
      <h2 style="color:white;"><i class="fas fa-calendar-check" style="color:white;"></i> Gestion des Reservations</h2>
      <div class="row">
        <!-- RÉSERVER -->
        <div class="col-md-4 mb-4">
          <div class="card menu-card text-center">
            <div class="card-body">
              <h5>Reserver ici !</h5>
              <a href="ReservationServlet?action=afficherFormAjouter" class="btn myBtn">Accéder</a>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Messages d'erreur -->
    <jsp:include page="/WEB-INF/messageErreur.jsp" />

  </div> <!-- close container -->

</div> <!-- close container-fluid -->

<!-- Scripts -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>