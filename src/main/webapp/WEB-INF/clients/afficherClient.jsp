<%-- /WEB-INF/clients/afficherClient.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.temporal.ChronoUnit" %>
<%@ page import="tp.objets.Reservation" %>

<!DOCTYPE html>
<html>
<head>
    <title>Détails de ${client.prenom} ${client.nom} - Auberg-Inn</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <meta charset="UTF-8">
    <style>
        body {
            background: url('<%= request.getContextPath() %>/assets/happyfamilylol.png') no-repeat center center fixed;
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
        .reservation-card {
            border-left: 4px solid #007bff;
            transition: all 0.3s ease;
        }
        .reservation-card:hover {
            box-shadow: 0 4px 15px rgba(0,123,255,0.2);
            transform: translateY(-2px);
        }
        .prix-total {
            font-size: 1.1rem;
            font-weight: bold;
        }
        .status-badge {
            font-size: 0.8rem;
        }
        .terminated { border-left-color: #6c757d; }
        .upcoming { border-left-color: #007bff; }
        .current { border-left-color: #28a745; }
    </style>
</head>
<body>

<!-- <div class="container"> -->
    <div class="container d-flex justify-content-center align-items-center flex-column" style="min-height: 100vh;">
        <div class="col-md-8 form-overlay">

    <%-- Informations du client --%>
    <div class="card my-4 shadow">
        <div class="card-header bg-primary text-white">
            <h3 class="mb-0"><i class="fas fa-user"></i> Informations du Client</h3>
        </div>
        <div class="card-body">
            <div class="row">
                <div class="col-md-6">
                    <h4 class="card-title text-primary">${client.prenom} ${client.nom}</h4>
                    <p class="card-text">
                        <strong><i class="fas fa-birthday-cake"></i> Âge :</strong> ${client.age} ans<br>
                        <strong><i class="fas fa-hashtag"></i> ID Client :</strong> ${client.idClient}
                    </p>
                </div>
                <div class="col-md-6 text-right">
                    <div class="btn-group">
                        <a href="${pageContext.request.contextPath}/ReservationServlet?action=afficherFormAjouter"
                           class="btn btn-success">
                            <i class="fas fa-calendar-plus"></i> Nouvelle réservation
                        </a>
                        <a href="${pageContext.request.contextPath}/client?action=afficherFormRetirer"
                           class="btn btn-outline-danger">
                            <i class="fas fa-user-minus"></i> Supprimer client
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <%-- Liste des réservations avec calculs --%>
    <div class="card shadow">
        <div class="card-header bg-info text-white">
            <h4 class="mb-0"><i class="fas fa-calendar-alt"></i> Historique des réservations</h4>
        </div>
        <div class="card-body">
            <c:choose>
                <c:when test="${empty reservations}">
                    <div class="alert alert-info text-center">
                        <i class="fas fa-info-circle fa-2x mb-3"></i>
                        <h5>Aucune réservation</h5>
                        <p>Ce client n'a encore effectué aucune réservation.</p>
                        <a href="${pageContext.request.contextPath}/ReservationServlet?action=afficherFormAjouter"
                           class="btn btn-primary">
                            <i class="fas fa-plus"></i> Créer une réservation
                        </a>
                    </div>
                </c:when>
                <c:otherwise>
                    <%
                        @SuppressWarnings("unchecked")
                        Map<Integer, Double> prixTotaux = (Map<Integer, Double>) request.getAttribute("prixTotaux");
                        @SuppressWarnings("unchecked")
                        Map<Integer, String> nomsChambre = (Map<Integer, String>) request.getAttribute("nomsChambre");
                        double totalGeneral = 0.0;
                        int nombreReservations = 0;

                        if (prixTotaux != null) {
                            for (Double prix : prixTotaux.values()) {
                                if (prix != null) {
                                    totalGeneral += prix;
                                    nombreReservations++;
                                }
                            }
                        }
                    %>

                    <div class="row">
                        <c:forEach items="${reservations}" var="res" varStatus="status">
                            <%
                                Reservation res = (Reservation) pageContext.getAttribute("res");
                                Double prixTotal = (prixTotaux != null) ? prixTotaux.get(res.getIdReservation()) : null;
                                String nomChambre = (nomsChambre != null) ? nomsChambre.get(res.getIdReservation()) : null;

                                if (prixTotal == null) prixTotal = 0.0;
                                if (nomChambre == null) nomChambre = "Chambre #" + res.getIdChambre();

                                // Calculer le statut de la réservation
                                LocalDate aujourdhui = LocalDate.now();
                                LocalDate dateDebut = LocalDate.parse(res.getDateDebut());
                                LocalDate dateFin = LocalDate.parse(res.getDateFin());
                                long nombreJours = ChronoUnit.DAYS.between(dateDebut, dateFin);

                                String statut = "";
                                String badgeClass = "";
                                String cardClass = "";

                                if (dateFin.isBefore(aujourdhui)) {
                                    statut = "Terminée";
                                    badgeClass = "badge-secondary";
                                    cardClass = "terminated";
                                } else if (dateDebut.isAfter(aujourdhui)) {
                                    statut = "À venir";
                                    badgeClass = "badge-primary";
                                    cardClass = "upcoming";
                                } else {
                                    statut = "En cours";
                                    badgeClass = "badge-success";
                                    cardClass = "current";
                                }
                            %>

                            <div class="col-md-6 mb-3">
                                <div class="card reservation-card h-100 <%= cardClass %>">
                                    <div class="card-header d-flex justify-content-between align-items-center">
                                        <h6 class="mb-0">
                                            <i class="fas fa-bed"></i> Réservation #${res.idReservation}
                                        </h6>
                                        <span class="badge status-badge <%= badgeClass %>"><%= statut %></span>
                                    </div>
                                    <div class="card-body">
                                        <div class="row">
                                            <div class="col-8">
                                                <p class="mb-1">
                                                    <strong><i class="fas fa-door-open"></i> Chambre :</strong><br>
                                                    <span class="text-primary"><%= nomChambre %></span>
                                                    <small class="text-muted">(ID: ${res.idChambre})</small>
                                                </p>
                                                <p class="mb-1">
                                                    <strong><i class="fas fa-calendar"></i> Période :</strong><br>
                                                    <small>${res.dateDebut} au ${res.dateFin}</small><br>
                                                    <small class="text-info">(<%= nombreJours %> jour<%= nombreJours > 1 ? "s" : "" %>)</small>
                                                </p>
                                            </div>
                                            <div class="col-4 text-right">
                                                <div class="prix-total text-success">
                                                    <i class="fas fa-dollar-sign"></i>
                                                    <%= String.format("%.2f", prixTotal) %> CAD
                                                </div>
                                                <small class="text-muted">Total payé</small>
                                            </div>
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </c:forEach>
                    </div>

                    <!-- Résumé financier -->
                    <div class="card bg-light mt-4">
                        <div class="card-body">
                            <div class="row text-center">
                                <div class="col-md-4">
                                    <h5 class="text-primary"><%= nombreReservations %></h5>
                                    <small class="text-muted">Réservation(s) total(es)</small>
                                </div>
                                <div class="col-md-4">
                                    <h5 class="text-success">$<%= String.format("%.2f", totalGeneral) %> CAD</h5>
                                    <small class="text-muted">Chiffre d'affaires total</small>
                                </div>
                                <div class="col-md-4">
                                    <h5 class="text-info">$<%= nombreReservations > 0 ? String.format("%.2f", totalGeneral / nombreReservations) : "0.00" %> CAD</h5>
                                    <small class="text-muted">Prix moyen par réservation</small>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <!-- Boutons d'action -->
    <div class="row mt-4">
        <div class="col text-center">
            <div class="btn-group">
                <a href="client?action=lister" class="btn btn-secondary">
                    <i class="fas fa-arrow-left"></i> Retour à la liste des clients
                </a>
                <a href="${pageContext.request.contextPath}/menu.jsp" class="btn btn-outline-secondary">
                    <i class="fas fa-home"></i> Menu principal
                </a>
            </div>
        </div>
    </div>
</div>

<!-- Modal pour les détails d'une réservation -->
<div class="modal fade" id="detailsModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Détails de la réservation</h5>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body" id="modalContent">
                <!-- Contenu dynamique -->
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Fermer</button>
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
    function voirDetails(idReservation, nomChambre, dateDebut, dateFin, prixTotal, nombreJours) {
        const prixParNuit = nombreJours > 0 ? (prixTotal / nombreJours) : 0;

        const content = `
        <div class="table-responsive">
            <table class="table table-sm">
                <tr>
                    <td><strong>ID Réservation :</strong></td>
                    <td>${idReservation}</td>
                </tr>
                <tr>
                    <td><strong>Chambre :</strong></td>
                    <td>${nomChambre}</td>
                </tr>
                <tr>
                    <td><strong>Date d'arrivée :</strong></td>
                    <td>${dateDebut}</td>
                </tr>
                <tr>
                    <td><strong>Date de départ :</strong></td>
                    <td>${dateFin}</td>
                </tr>
                <tr>
                    <td><strong>Durée :</strong></td>
                    <td>${nombreJours} jour${nombreJours > 1 ? 's' : ''}</td>
                </tr>
                <tr>
                    <td><strong>Prix par nuit :</strong></td>
                    <td class="text-info">$${prixParNuit.toFixed(2)} CAD</td>
                </tr>
                <tr>
                    <td><strong>Prix total payé :</strong></td>
                    <td class="text-success font-weight-bold">$${prixTotal.toFixed(2)} CAD</td>
                </tr>
            </table>
        </div>
        <small class="text-muted">
            <i class="fas fa-info-circle"></i>
            Le prix inclut le prix de base de la chambre et tous les suppléments des commodités.
        </small>
    `;

        document.getElementById('modalContent').innerHTML = content;
        $('#detailsModal').modal('show');
    }


    // Auto-hide des alertes après 5 secondes
    setTimeout(function() {
        $('.alert').fadeOut('slow');
    }, 5000);
</script>
</body>
</html>