<%--
  Page principale de gestion Auberge-Inn
  Date : 28 juillet 2025
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%
  // Vérification de la configuration BD
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

  <!-- Bootstrap & icones (iconique!) -->
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
  <!-- google-->
  <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200&icon_names=hotel" />

  <style>
    body {
      background: white;
      font-family: 'Segoe UI', Roboto, sans-serif;
      color: #f1f1f1;
      min-height: 100vh;
    }

    /* Effet de profondeur  */
    .container, .container-fluid {
      position: relative;
      z-index: 2;
    }

    /* -------- MENU -------- */
    /* Style pour les sections de menu */
    .menu-section {
      margin-bottom: 3rem;
      position: relative;
    }

    .menu-section h2 {
      font-size: 1.8rem;
      border-bottom: 3px solid rgba(135, 206, 235, 0.6);
      padding-bottom: .8rem;
      margin-bottom: 2rem;
      color: #87ceeb;
      text-shadow: none;
      background: none;
      -webkit-background-clip: unset;
      -webkit-text-fill-color: unset;
    }

    .menu-section h2::after {
      content: '';
      position: absolute;
      bottom: -3px;
      left: 0;
      width: 100%;
      height: 3px;
      background: linear-gradient(90deg, transparent, #87ceeb, #f093fb, #87ceeb, transparent);
      animation: underlineFlow 3s ease-in-out infinite;
    }

    @keyframes underlineFlow {
      0% { transform: translateX(-100%); }
      100% { transform: translateX(100%); }
    }

    /* Cartes flottantes */
    .menu-card {
      border: 1px solid rgba(255,255,255,0.2);
      border-radius: 15px;
      background: rgba(37, 65, 89, 0.8);
      backdrop-filter: blur(10px);
      transition: all 0.4s ease;
      height: 100%;
      position: relative;
      overflow: hidden;
      box-shadow:
              0 8px 32px rgba(0,0,0,0.1),
              inset 0 1px 0 rgba(255,255,255,0.4);
    }

    .menu-card::before {
      content: '';
      position: absolute;
      top: 0;
      left: -100%;
      width: 100%;
      height: 100%;
      background: linear-gradient(90deg,
      transparent,
      rgba(135, 206, 235, 0.2),
      rgba(240, 147, 251, 0.2),
      transparent);
      transition: left 0.6s ease;
    }

    .menu-card:hover::before {
      left: 100%;
    }

    .menu-card:hover {
      transform: translateY(-10px) scale(1.02);
      box-shadow:
              0 20px 40px rgba(0,0,0,0.2),
              0 0 20px rgba(135, 206, 235, 0.3),
              inset 0 1px 0 rgba(255,255,255,0.6);
      border-color: rgba(135, 206, 235, 0.5);
    }

    /* bouton Accès */
    .Bouton {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      border: none;
      color: #FFFFFF;
      border-radius: 8px;
      padding: 10px 20px;
      font-weight: 600;
      text-transform: uppercase;
      letter-spacing: 1px;
      transition: all 0.3s ease;
      position: relative;
      overflow: hidden;
    }

    .Bouton::before {
      content: '';
      position: absolute;
      top: 0;
      left: -100%;
      width: 100%;
      height: 100%;
      background: linear-gradient(90deg, transparent, rgba(255,255,255,0.2), transparent);
      transition: left 0.5s ease;
    }

    .Bouton:hover::before {
      left: 100%;
    }

    .Bouton:hover {
      background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      transform: translateY(-2px);
      box-shadow: 0 8px 20px rgba(240, 147, 251, 0.4);
      color: white;
    }

    /* Navigation */
    .navbar-dark {
      background: rgba(37, 65, 89, 0.8) !important;
      backdrop-filter: blur(10px);
      border-bottom: 1px solid rgba(255,255,255,0.1);
    }

    .navbar-brand {
      font-weight: 700;
      font-size: 1.5rem;
      color: #77F2EA !important;
      text-shadow: 0 0 10px rgba(135, 206, 235, 0.5);
    }

    /* Messages d'alerte stylés */
    .alert {
      backdrop-filter: blur(10px);
      border-radius: 12px;
      border: 1px solid rgba(255,255,255,0.2);
    }

    .alert-success {
      background: linear-gradient(135deg, rgba(40, 167, 69, 0.9), rgba(25, 135, 84, 0.9));
      color: white;
    }

    .alert-danger {
      background: linear-gradient(135deg, rgba(220, 53, 69, 0.9), rgba(157, 30, 47, 0.9));
      color: white;
    }

    /* BOUTON DE FEU */
    .deleteButton {
      position: relative;
      display: inline-flex;
      justify-content: center;
      align-items: center;
      width: 140px;
      height: 60px;
      border: 3px solid #ff4500;
      border-radius: 15px;
      font-size: 14px;
      font-weight: bold;
      letter-spacing: 1px;
      cursor: pointer;
      overflow: hidden;
      background: transparent;
      color: #fff;
      text-decoration: none;
      text-shadow: 2px 2px 4px rgba(0,0,0,0.9);
      z-index: 1;
      transition: all 0.3s ease;
    }

    .deleteButton::before {
      content: "";
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background-image: url('assets/fire.gif');
      background-repeat: no-repeat;
      background-size: cover;
      background-position: center;
      opacity: 0.9;
      z-index: -1;
      transition: all 0.3s ease;
      animation: subtleFlame 2s ease-in-out infinite;
    }

    .deleteButton::after {
      content: "";
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background:
              radial-gradient(circle at 30% 70%, rgba(255, 69, 0, 0.3) 0%, transparent 70%),
              radial-gradient(circle at 70% 30%, rgba(255, 165, 0, 0.2) 0%, transparent 70%);
      z-index: -1;
      opacity: 0.7;
      transition: all 0.3s ease;
    }

    .deleteButton:hover {
      color: #ffff00 !important;
      text-decoration: none;
      transform: scale(1.08);
      border-color: #ff2500;
      text-shadow:
              0 0 10px rgba(255,255,0,0.8),
              0 0 20px rgba(255,165,0,0.6),
              2px 2px 4px rgba(0,0,0,0.9);
      box-shadow:
              0 0 25px rgba(255, 69, 0, 0.7),
              inset 0 0 20px rgba(255, 165, 0, 0.2);
      animation: buttonIntense 0.5s ease-in-out;
    }

    .deleteButton:hover::before {
      opacity: 1;
      transform: scale(1.02);
      filter: brightness(1.3) saturate(1.5) contrast(1.2);
      animation: subtleFlame 0.5s ease-in-out infinite;
    }

    .deleteButton:hover::after {
      opacity: 1;
      background:
              radial-gradient(circle at 30% 70%, rgba(255, 69, 0, 0.5) 0%, transparent 70%),
              radial-gradient(circle at 70% 30%, rgba(255, 165, 0, 0.4) 0%, transparent 70%),
              linear-gradient(45deg, rgba(255, 100, 0, 0.3), rgba(255, 200, 0, 0.2));
    }

    .deleteButton:active {
      transform: scale(0.95);
      box-shadow:
              0 0 40px rgba(255, 69, 0, 1),
              inset 0 0 30px rgba(255, 100, 0, 0.5);
    }

    .deleteButton:focus {
      outline: none;
      box-shadow:
              0 0 25px rgba(255, 69, 0, 0.7),
              0 0 0 3px rgba(255, 255, 0, 0.5);
    }

    @keyframes buttonIntense {
      0% {
        transform: scale(1.08);
        box-shadow: 0 0 25px rgba(255, 69, 0, 0.7);
      }
      50% {
        transform: scale(1.12);
        box-shadow: 0 0 35px rgba(255, 69, 0, 0.9);
      }
      100% {
        transform: scale(1.08);
        box-shadow: 0 0 25px rgba(255, 69, 0, 0.7);
      }
    }

    @keyframes subtleFlame {
      0%, 100% {
        filter: brightness(1) saturate(1);
      }
      50% {
        filter: brightness(1.1) saturate(1.2);
      }
    }
    @media (max-width: 768px) {
      .menu-section h2 {
        font-size: 1.5rem;
      }
      .menu-card {
        margin-bottom: 1.5rem;
      }
    }
  </style>
</head>
<body>

<div style="width:100%; height:400px; overflow:hidden;">
  <img src="<c:url <%= request.getContextPath() %>/assets/header-bg.jpg />"
       alt="image du inn"
       style="width:100%; height:100%; object-fit:cover;">
</div>

<div class="container-fluid px-0">

  <!-- La navigation -->
  <jsp:include page="/WEB-INF/navigation.jsp" />

  <div class="container">

    <!-- ============== GESTION DES CHAMBRES ============== -->
    <div class="menu-section">
      <h2 style="color:#254159;"><span class="material-symbols-outlined" style="color: #254159;" >hotel</span> Gestion des Chambres</h2>
      <div class="row">
        <!-- AJOUTER CHAMBRE -->
        <div class="col-md-4 mb-4">
          <div class="card menu-card text-center">
            <div class="card-body">
              <h5>Ajouter une nouvelle chambre</h5>
              <a href="chambres?action=afficherFormAjouter" class="btn Bouton">Accéder</a>
            </div>
          </div>
        </div>

        <!-- SUPPRIMER CHAMBRE -->
        <div class="col-md-4 mb-4">
          <div class="card menu-card text-center">
            <div class="card-body">
              <h5>Supprimer une chambre</h5>
              <p class="text-muted">Retirer définitivement une chambre</p>
              <a href="chambres?action=afficherFormSupprimer" class="deleteButton">
                🔥 DÉTRUIRE 🔥
              </a>
            </div>
          </div>
        </div>

        <!-- AFFICHER CHAMBRE -->
        <div class="col-md-4 mb-4">
          <div class="card menu-card text-center">
            <div class="card-body">
              <h5>Afficher une chambre</h5>
              <p class="text-muted">Rechercher et voir les détails d'une chambre</p>
              <a href="chambres?action=afficherFormRecherche" class="btn Bouton">Accéder</a>
            </div>
          </div>
        </div>

        <!-- CHAMBRES LIBRES -->
        <div class="col-md-4 mb-4">
          <div class="card menu-card text-center">
            <div class="card-body">
              <h5>Afficher les chambres libres</h5>
              <a href="chambres?action=afficherFormChambresLibres" class="btn Bouton">Accéder</a>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- ============== GESTION DES CLIENTS ============== -->
    <div class="menu-section">
      <h2 style="color:#254159;"><i class="fas fa-users" style="color:#254159;"></i> Gestion des Clients</h2>
      <div class="row">
        <!-- AJOUTER CLIENT -->
        <div class="col-md-4 mb-4">
          <div class="card menu-card text-center">
            <div class="card-body">
              <h5>Ajouter un client</h5>
              <a href="client?action=afficherFormAjouter" class="btn Bouton">Accéder</a>
            </div>
          </div>
        </div>
        <!-- RETIRER CLIENT -->
        <div class="col-md-4 mb-4">
          <div class="card menu-card text-center">
            <div class="card-body">
              <h5>Retirer un client</h5>
              <a href="client?action=afficherFormRetirer" class="deleteButton">🔥 DÉTRUIRE 🔥</a>
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
              <a href="client?action=lister" class="btn Bouton">Accéder</a>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- AFFICHER LES CLIENTS -->
    <div class="col-md-4 mb-4">
      <div class="card menu-card text-center">
        <div class="card-body">
          <h5>Afficher les clients</h5>
          <p class="text-muted">Voir la liste de tous les clients enregistrés</p>
          <a href="client?action=lister" class="btn Bouton">Accéder</a>
        </div>
      </div>
    </div>

    <!-- ============== GESTION DES COMMODITÉS ============== -->
    <div class="menu-section">
      <h2 style="color:#254159;"><i class="fas fa-concierge-bell" style="color:#254159;"></i> Gestion des Commodités</h2>
      <div class="row">
        <!-- AJOUTER COMMODITÉ -->
        <div class="col-md-4 mb-4">
          <div class="card menu-card text-center">
            <div class="card-body">
              <h5>Ajouter une commodité</h5>
              <a href="CommoditeServlet?action=afficherFormAjouter" class="btn Bouton">Accéder</a>
            </div>
          </div>
        </div>

        <!-- INCLURE COMMODITÉ -->
        <div class="col-md-4 mb-4">
          <div class="card menu-card text-center">
            <div class="card-body">
              <h5>Inclure une commodité</h5>
              <a href="CommoditeServlet?action=afficherFormInclure" class="btn Bouton">Accéder</a>
            </div>
          </div>
        </div>

        <!-- ENLEVER COMMODITÉ -->
        <div class="col-md-4 mb-4">
          <div class="card menu-card text-center">
            <div class="card-body">
              <h5>Enlever une commodité</h5>
              <a href="CommoditeServlet?action=afficherFormEnlever" class="deleteButton">🔥 DÉTRUIRE 🔥</a>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- ============== GESTION DES RÉSERVATIONS ============== -->
    <div class="menu-section">
      <h2 style="color:#254159;"><i class="fas fa-calendar-check" style="color:#254159;"></i> Gestion des Reservations</h2>
      <div class="row">
        <!-- RÉSERVER -->
        <div class="col-md-4 mb-4">
          <div class="card menu-card text-center">
            <div class="card-body">
              <h5>Reserver ici !</h5>
              <a href="ReservationServlet?action=afficherFormAjouter" class="btn Bouton">Accéder</a>
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

<script>
  // Animation supplémentaire au clic sur le bouton de feu
  document.querySelector('.deleteButton').addEventListener('click', function(e) {
    // Créer un effet de "explosion" temporaire
    this.style.transform = 'scale(1.2)';
    this.style.boxShadow = '0 0 30px rgba(255, 100, 0, 0.8)';

    setTimeout(() => {
      this.style.transform = '';
      this.style.boxShadow = '';
    }, 200);
  });
</script>
</body>
</html>