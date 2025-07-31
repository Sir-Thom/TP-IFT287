<%--
  Created by IntelliJ IDEA.
  User: zowes
  Date: 2025-07-29
  Time: 8:58 a.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="tp.objets.Client" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Auberg-Inn - Liste des Clients</title>
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
                    <li class="breadcrumb-item active">Liste des Clients</li>
                </ol>
            </nav>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12">
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

            <div class="card">
                <div class="card-header bg-info text-white">
                    <div class="row">
                        <div class="col-md-8">
                            <h3 class="mb-0">👥 Liste des Clients Enregistrés</h3>
                        </div>
                        <div class="col-md-4 text-right">
                            <a href="../../ClientServlet?action=afficherFormAjouter" class="btn btn-success btn-sm">
                                <i class="fas fa-user-plus"></i> Ajouter un client
                            </a>
                        </div>
                    </div>
                </div>
                <div class="card-body">
                    <%
                        List<Client> clients = (List<Client>) request.getAttribute("clients");

                        if (clients == null) {
                            clients = new ArrayList<>();
                        }
                    %>

                    <% if (clients.isEmpty()) { %>
                    <div class="alert alert-warning text-center">
                        <h5><i class="fas fa-user-slash"></i> Aucun client enregistré</h5>
                        <p>Commencez par ajouter votre premier client !</p>
                        <a href="../../ClientServlet?action=afficherFormAjouter" class="btn btn-success">
                            <i class="fas fa-user-plus"></i> Ajouter un client
                        </a>
                    </div>
                    <% } else { %>

                    <!-- Statistiques rapides -->
                    <div class="row mb-4">
                        <div class="col-md-12">
                            <div class="alert alert-info">
                                <strong><i class="fas fa-chart-pie"></i> Statistiques :</strong>
                                <span class="badge badge-primary ml-2"><%= clients.size() %> client(s) enregistré(s)</span>
                            </div>
                        </div>
                    </div>

                    <!-- Tableau des clients -->
                    <div class="table-responsive">
                        <table class="table table-striped table-hover">
                            <thead class="thead-dark">
                            <tr>
                                <th scope="col">#</th>
                                <th scope="col">Nom</th>
                                <th scope="col">Prénom</th>
                                <th scope="col">Âge</th>
                                <th scope="col">Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            <%
                                int index = 1;
                                for (Client client : clients) {
                            %>
                            <tr>
                                <td><strong><%= index++ %></strong></td>
                                <td>
                                    <i class="fas fa-user text-muted"></i>
                                    <%= client.getNom() != null ? client.getNom() : "N/A" %>
                                </td>
                                <td><%= client.getPrenom() != null ? client.getPrenom() : "N/A" %></td>
                                <td>
                                        <span class="badge badge-secondary">
                                            <%= client.getAge() %> ans
                                        </span>
                                </td>
                                <td>
                                    <div class="btn-group btn-group-sm" role="group">
                                        <button type="button" class="btn btn-outline-info"
                                                onclick="afficherDetailsClient('<%= client.getPrenom() %>', '<%= client.getNom() %>', '<%= client.getAge() %>')">
                                            <i class="fas fa-eye"></i> Détails
                                        </button>
                                        <button type="button" class="btn btn-outline-warning" disabled>
                                            <i class="fas fa-edit"></i> Modifier
                                        </button>
                                        <button type="button" class="btn btn-outline-danger"
                                                onclick="confirmerSuppressionClient('<%= client.getPrenom() %>', '<%= client.getNom() %>')">
                                            <i class="fas fa-trash"></i> Supprimer
                                        </button>
                                    </div>
                                </td>
                            </tr>
                            <% } %>
                            </tbody>
                        </table>
                    </div>
                    <% } %>
                </div>
                <div class="card-footer text-muted">
                    <div class="row">
                        <div class="col-md-6">
                            <small><i class="fas fa-info-circle"></i> Liste mise à jour automatiquement</small>
                        </div>
                        <div class="col-md-6 text-right">
                            <a href="<%= request.getContextPath() %>/menu.jsp" class="btn btn-secondary btn-sm">
                                <i class="fas fa-arrow-left"></i> Retour au menu
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Modal pour les détails du client -->
<div class="modal fade" id="modalDetailsClient" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header bg-info text-white">
                <h5 class="modal-title">
                    <i class="fas fa-user"></i> Détails du Client
                </h5>
                <button type="button" class="close text-white" data-dismiss="modal">
                    <span>&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-4"><strong>Prénom :</strong></div>
                    <div class="col-md-8" id="modalPrenom">-</div>
                </div>
                <hr>
                <div class="row">
                    <div class="col-md-4"><strong>Nom :</strong></div>
                    <div class="col-md-8" id="modalNom">-</div>
                </div>
                <hr>
                <div class="row">
                    <div class="col-md-4"><strong>Âge :</strong></div>
                    <div class="col-md-8" id="modalAge">-</div>
                </div>
                <hr>
                <div class="row">
                    <div class="col-md-4"><strong>Réservations :</strong></div>
                    <div class="col-md-8">
                        <span class="badge badge-info">À implémenter</span>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Fermer</button>
            </div>
        </div>
    </div>
</div>

<!-- Scripts -->
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>

<script>
    function afficherDetailsClient(prenom, nom, age) {
        document.getElementById('modalPrenom').textContent = prenom;
        document.getElementById('modalNom').textContent = nom;
        document.getElementById('modalAge').textContent = age + ' ans';
        $('#modalDetailsClient').modal('show');
    }

    function confirmerSuppressionClient(prenom, nom) {
        if (confirm('Êtes-vous sûr de vouloir supprimer le client ' + prenom + ' ' + nom + ' ?')) {
            // Rediriger vers l'action de suppression
            window.location.href = '../../ClientServlet?action=supprimer&prenom=' +
                encodeURIComponent(prenom) + '&nom=' + encodeURIComponent(nom);
        }
    }
</script>
</body>
</html>
