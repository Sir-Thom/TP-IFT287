package com.servlet.TP;

import javax.servlet.http.HttpServlet;
import tp.gestion.GestionCommodite;
import tp.gestion.TpGestion;
import tp.objets.Commodite;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class CommoditeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        String action = request.getParameter("action");

        try {
            if (!InnHelper.peutProceder(getServletContext(), request, response)) {
                return;
            }

            TpGestion tpGestion = getTpGestion(session, request, response);
            if (tpGestion == null) return;

            GestionCommodite gestionCommodite = tpGestion.getGestionCommodite();

            switch (action) {
                case "lister":
                    String chambreNom = request.getParameter("chambreNom");
                    List<Commodite> commodites = gestionCommodite.obtenirCommoditesChambre(chambreNom);
                    request.setAttribute("commodites", commodites);
                    request.setAttribute("chambreNom", chambreNom);
                    request.getRequestDispatcher("/WEB-INF/commodites/listeCommodites.jsp").forward(request, response);
                    break;

                case "afficherFormAjouter":
                    request.getRequestDispatcher("/WEB-INF/commodites/ajouterCommodite.jsp").forward(request, response);
                    break;

                case "afficherFormInclure":
                    // NOUVEAU : Charger toutes les commodités disponibles
                    chargerCommoditesDisponibles(request, gestionCommodite);
                    request.getRequestDispatcher("/WEB-INF/commodites/inclureCommodite.jsp").forward(request, response);
                    break;

                case "afficherFormEnlever":
                    // NOUVEAU : Charger toutes les commodités disponibles
                    chargerCommoditesDisponibles(request, gestionCommodite);
                    request.getRequestDispatcher("/WEB-INF/commodites/enleverCommodite.jsp").forward(request, response);
                    break;

                default:
                    response.sendRedirect("menu.jsp");
            }
        } catch (Exception e) {
            envoyerErreur(request, response, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        String action = request.getParameter("action");

        try {
            if (!InnHelper.peutProceder(getServletContext(), request, response)) {
                return;
            }

            TpGestion tpGestion = getTpGestion(session, request, response);
            if (tpGestion == null) return;

            GestionCommodite gestionCommodite = tpGestion.getGestionCommodite();

            switch (action) {
                case "ajouter":
                    ajouterCommodite(request, response, gestionCommodite);
                    break;

                case "inclure":
                    inclureCommodite(request, response, gestionCommodite);
                    break;

                case "enlever":
                    enleverCommodite(request, response, gestionCommodite);
                    break;

                default:
                    throw new Exception("Action POST non valide : " + action);
            }
        } catch (Exception e) {
            envoyerErreur(request, response, e);
        }
    }

    private void ajouterCommodite(HttpServletRequest request, HttpServletResponse response, GestionCommodite gestionCommodite)
            throws ServletException, IOException {
        try {
            String description = request.getParameter("description");
            String surplusStr = request.getParameter("surplus");

            // Validation
            if (description == null || description.trim().isEmpty()) {
                throw new Exception("La description est obligatoire");
            }

            double surplus;
            try {
                surplus = Double.parseDouble(surplusStr);
                if (surplus < 0) {
                    throw new Exception("Le surplus de prix ne peut pas être négatif");
                }
            } catch (NumberFormatException e) {
                throw new Exception("Le surplus de prix doit être un nombre valide");
            }

            // Ajouter la commodité
            gestionCommodite.ajouterCommodite(description.trim(), surplus);

            // SUCCÈS : Vider les champs et afficher le message de succès
            request.setAttribute("message", "Commodité '" + description.trim() + "' ajoutée avec succès!");
            // Ne pas remettre les valeurs dans les champs après succès
            request.removeAttribute("description");
            request.removeAttribute("surplus");

            System.out.println("Commodité ajoutée avec succès : " + description);

        } catch (Exception e) {
            // ERREUR : Préserver les valeurs saisies
            request.setAttribute("description", request.getParameter("description"));
            request.setAttribute("surplus", request.getParameter("surplus"));
            request.setAttribute("erreur", e.getMessage());

            System.err.println("Erreur lors de l'ajout de commodité : " + e.getMessage());
            e.printStackTrace();
        }

        // Dans tous les cas, rester sur la page d'ajout
        request.getRequestDispatcher("/WEB-INF/commodites/ajouterCommodite.jsp").forward(request, response);
    }

    private void inclureCommodite(HttpServletRequest request, HttpServletResponse response, GestionCommodite gestionCommodite)
            throws ServletException, IOException {
        try {
            String chambreNom = request.getParameter("chambreNom");
            String idCommoditeStr = request.getParameter("idCommodite");

            // Validation
            if (chambreNom == null || chambreNom.trim().isEmpty()) {
                throw new Exception("Le nom de la chambre est obligatoire");
            }

            int idCommodite;
            try {
                idCommodite = Integer.parseInt(idCommoditeStr);
                if (idCommodite <= 0) {
                    throw new Exception("L'ID de la commodité doit être supérieur à 0");
                }
            } catch (NumberFormatException e) {
                throw new Exception("L'ID de la commodité doit être un nombre valide");
            }

            gestionCommodite.inclureCommodite(chambreNom.trim(), idCommodite);

            // SUCCÈS : Message et vider les champs
            request.setAttribute("message", "Commodité incluse avec succès dans la chambre " + chambreNom);
            request.removeAttribute("chambreNom");
            request.removeAttribute("idCommodite");

        } catch (Exception e) {
            // ERREUR : Préserver les valeurs
            request.setAttribute("chambreNom", request.getParameter("chambreNom"));
            request.setAttribute("idCommodite", request.getParameter("idCommodite"));
            request.setAttribute("erreur", e.getMessage());
        }

        // Recharger les commodités disponibles
        chargerCommoditesDisponibles(request, gestionCommodite);
        request.getRequestDispatcher("/WEB-INF/commodites/inclureCommodite.jsp").forward(request, response);
    }

    private void enleverCommodite(HttpServletRequest request, HttpServletResponse response, GestionCommodite gestionCommodite)
            throws ServletException, IOException {
        try {
            String chambreNom = request.getParameter("chambreNom");
            String idCommoditeStr = request.getParameter("idCommodite");

            // Validation
            if (chambreNom == null || chambreNom.trim().isEmpty()) {
                throw new Exception("Le nom de la chambre est obligatoire");
            }

            int idCommodite;
            try {
                idCommodite = Integer.parseInt(idCommoditeStr);
                if (idCommodite <= 0) {
                    throw new Exception("L'ID de la commodité doit être supérieur à 0");
                }
            } catch (NumberFormatException e) {
                throw new Exception("L'ID de la commodité doit être un nombre valide");
            }

            gestionCommodite.enleverCommodite(chambreNom.trim(), idCommodite);

            // SUCCÈS : Message et vider les champs
            request.setAttribute("message", "Commodité enlevée avec succès de la chambre " + chambreNom);
            request.removeAttribute("chambreNom");
            request.removeAttribute("idCommodite");

        } catch (Exception e) {
            // ERREUR : Préserver les valeurs
            request.setAttribute("chambreNom", request.getParameter("chambreNom"));
            request.setAttribute("idCommodite", request.getParameter("idCommodite"));
            request.setAttribute("erreur", e.getMessage());
        }

        // Recharger les commodités disponibles
        chargerCommoditesDisponibles(request, gestionCommodite);
        request.getRequestDispatcher("/WEB-INF/commodites/enleverCommodite.jsp").forward(request, response);
    }

    /**
     * NOUVELLE MÉTHODE : Charge toutes les commodités disponibles
     */
    private void chargerCommoditesDisponibles(HttpServletRequest request, GestionCommodite gestionCommodite) {
        try {
            List<Commodite> commoditesDisponibles = gestionCommodite.obtenirToutesLesCommodites();
            request.setAttribute("commoditesDisponibles", commoditesDisponibles);

            if (commoditesDisponibles.isEmpty()) {
                request.setAttribute("messageInfo", "Aucune commodité disponible. Créez-en une d'abord !");
            }

            System.out.println("Chargé " + commoditesDisponibles.size() + " commodités disponibles");

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des commodités : " + e.getMessage());
            request.setAttribute("commoditesDisponibles", java.util.Collections.emptyList());
            request.setAttribute("messageInfo", "Erreur lors du chargement des commodités : " + e.getMessage());
        }
    }

    private TpGestion getTpGestion(HttpSession session, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        TpGestion tpGestion = InnHelper.getInnInterro(session);
        if (tpGestion == null) {
            request.setAttribute("erreur", "Session expirée. Veuillez vous reconnecter.");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
        return tpGestion;
    }

    private void envoyerErreur(HttpServletRequest request, HttpServletResponse response, Exception e)
            throws ServletException, IOException {
        e.printStackTrace(); // Pour le debug
        request.setAttribute("erreur", "Erreur système : " + e.getMessage());
        request.getRequestDispatcher("/WEB-INF/messageErreur.jsp").forward(request, response);
    }
}