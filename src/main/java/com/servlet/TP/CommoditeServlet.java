package com.servlet.TP;

import tp.TpExeception;
import tp.gestion.GestionCommodite;
import tp.gestion.TpGestion;
import tp.objets.Commodite;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class CommoditeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        String action = request.getParameter("action");

        try {
            // Vérifier si on peut procéder
            if (!InnHelper.peutProceder(getServletContext(), request, response)) {
                return;
            }

            // Récupération du gestionnaire depuis la session
            TpGestion tpGestion = InnHelper.getInnInterro(session);
            if (tpGestion == null) {
                request.setAttribute("erreur", "Session expirée. Veuillez vous reconnecter.");
                request.getRequestDispatcher("/index.jsp").forward(request, response);
                return;
            }

            // Récupération du gestionnaire de commodités
            GestionCommodite gestionCommodite = tpGestion.getGestionCommodite();

            switch (action) {
                case "afficherFormAjouter":
                    afficherFormulaireAjouter(request, response);
                    break;

                case "ajouter":
                    ajouterCommodite(request, response, gestionCommodite);
                    break;

                case "afficherFormInclure":
                    afficherFormulaireInclure(request, response);
                    break;

                case "inclure":
                    inclureCommodite(request, response, gestionCommodite);
                    break;

                case "afficherFormEnlever":
                    afficherFormulaireEnlever(request, response);
                    break;

                case "enlever":
                    enleverCommodite(request, response, gestionCommodite);
                    break;

                case "lister":
                    listerCommodites(request, response, gestionCommodite);
                    break;

                default:
                    request.setAttribute("erreur", "Action non reconnue: " + action);
                    request.getRequestDispatcher("/menu.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erreur", "Erreur système: " + e.getMessage());
            request.getRequestDispatcher("/menu.jsp").forward(request, response);
        }
    }

    private void afficherFormulaireAjouter(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/commodites/ajouterCommodite.jsp").forward(request, response);
    }

    private void ajouterCommodite(HttpServletRequest request, HttpServletResponse response,
                                  GestionCommodite gestionCommodite) throws ServletException, IOException {
        try {
            String description = request.getParameter("description");
            String surplusPrixStr = request.getParameter("surplusPrix");

            System.out.println("=== DEBUG AJOUT COMMODITÉ ===");
            System.out.println("Description reçue: " + description);
            System.out.println("Surplus prix reçu: " + surplusPrixStr);

            // Validation
            if (description == null || description.trim().isEmpty()) {
                throw new TpExeception("La description de la commodité est obligatoire");
            }

            double surplusPrix;
            try {
                surplusPrix = Double.parseDouble(surplusPrixStr);
                if (surplusPrix < 0) {
                    throw new TpExeception("Le surplus de prix doit être positif ou nul");
                }
            } catch (NumberFormatException e) {
                throw new TpExeception("Le surplus de prix doit être un nombre valide");
            }

            // Appel de la méthode métier
            System.out.println("Appel de gestionCommodite.ajouterCommodite...");
            gestionCommodite.ajouterCommodite(description.trim(), surplusPrix);
            System.out.println("Commodité ajoutée avec succès!");

            request.setAttribute("message", "Commodité '" + description + "' ajoutée avec succès!");
            request.getRequestDispatcher("/WEB-INF/commodites/ajouterCommodite.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("Erreur lors de l'ajout de commodité: " + e.getMessage());
            e.printStackTrace();

            // Préserver les valeurs saisies en cas d'erreur
            request.setAttribute("description", request.getParameter("description"));
            request.setAttribute("surplusPrix", request.getParameter("surplusPrix"));
            request.setAttribute("erreur", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/commodites/ajouterCommodite.jsp").forward(request, response);
        }
    }

    private void afficherFormulaireInclure(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/commodites/inclureCommodite.jsp").forward(request, response);
    }

    private void inclureCommodite(HttpServletRequest request, HttpServletResponse response,
                                  GestionCommodite gestionCommodite) throws ServletException, IOException {
        try {
            String nomChambre = request.getParameter("nomChambre");
            String idCommoditeStr = request.getParameter("idCommodite");

            System.out.println("=== DEBUG INCLURE COMMODITÉ ===");
            System.out.println("Nom chambre: " + nomChambre);
            System.out.println("ID commodité: " + idCommoditeStr);

            // Validation
            if (nomChambre == null || nomChambre.trim().isEmpty()) {
                throw new TpExeception("Le nom de la chambre est obligatoire");
            }

            int idCommodite;
            try {
                idCommodite = Integer.parseInt(idCommoditeStr);
            } catch (NumberFormatException e) {
                throw new TpExeception("L'ID de la commodité doit être un nombre valide");
            }

            // Appel de la méthode métier
            gestionCommodite.inclureCommodite(nomChambre.trim(), idCommodite);

            request.setAttribute("message", "Commodité ajoutée à la chambre '" + nomChambre + "' avec succès!");
            request.getRequestDispatcher("/WEB-INF/commodites/inclureCommodite.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("Erreur lors de l'inclusion de commodité: " + e.getMessage());
            e.printStackTrace();

            request.setAttribute("nomChambre", request.getParameter("nomChambre"));
            request.setAttribute("idCommodite", request.getParameter("idCommodite"));
            request.setAttribute("erreur", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/commodites/inclureCommodite.jsp").forward(request, response);
        }
    }

    private void afficherFormulaireEnlever(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/commodites/enleverCommodite.jsp").forward(request, response);
    }

    private void enleverCommodite(HttpServletRequest request, HttpServletResponse response,
                                  GestionCommodite gestionCommodite) throws ServletException, IOException {
        try {
            String nomChambre = request.getParameter("nomChambre");
            String idCommoditeStr = request.getParameter("idCommodite");

            System.out.println("=== DEBUG ENLEVER COMMODITÉ ===");
            System.out.println("Nom chambre: " + nomChambre);
            System.out.println("ID commodité: " + idCommoditeStr);

            // Validation
            if (nomChambre == null || nomChambre.trim().isEmpty()) {
                throw new TpExeception("Le nom de la chambre est obligatoire");
            }

            int idCommodite;
            try {
                idCommodite = Integer.parseInt(idCommoditeStr);
            } catch (NumberFormatException e) {
                throw new TpExeception("L'ID de la commodité doit être un nombre valide");
            }

            // Appel de la méthode métier
            gestionCommodite.enleverCommodite(nomChambre.trim(), idCommodite);

            request.setAttribute("message", "Commodité retirée de la chambre '" + nomChambre + "' avec succès!");
            request.getRequestDispatcher("/WEB-INF/commodites/enleverCommodite.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("Erreur lors du retrait de commodité: " + e.getMessage());
            e.printStackTrace();

            request.setAttribute("nomChambre", request.getParameter("nomChambre"));
            request.setAttribute("idCommodite", request.getParameter("idCommodite"));
            request.setAttribute("erreur", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/commodites/enleverCommodite.jsp").forward(request, response);
        }
    }

    private void listerCommodites(HttpServletRequest request, HttpServletResponse response,
                                  GestionCommodite gestionCommodite) throws ServletException, IOException {
        try {
            // À implémenter quand la méthode sera disponible dans GestionCommodite
            request.setAttribute("message", "Liste des commodités - à implémenter");
            request.getRequestDispatcher("/menu.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("erreur", "Erreur lors de la récupération des commodités: " + e.getMessage());
            request.getRequestDispatcher("/menu.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}