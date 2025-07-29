package com.servlet.TP;

import tp.TpExeception;
import tp.gestion.GestionChambre;
import tp.gestion.GestionClient;
import tp.gestion.TpGestion;
import tp.objets.Chambre;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ChambreServlet extends HttpServlet {


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

            GestionChambre gestionChambre = tpGestion.getGestionChambre();
            if (gestionChambre == null) {
                throw new TpExeception("Module de gestion des chambres indisponible");
            }

            switch (action) {
                case "afficherFormAjouter" :
                    afficherFormulaireAjouter(request, response);
                    break;
                case "afficherFormRecherche" :
                    afficherFormulaireRecherche(request, response);
                    break;
                case "afficherFormChambresLibres" :
                    afficherFormulaireChambresLibres(request, response);
                    break;
                case "afficher" :
                    afficherChambre(request, response, gestionChambre);
                    break;
                case "chambresLibres" :
                    afficherChambresLibres(request, response);
                    break;
                default :
                    request.setAttribute("erreur", "Action GET non reconnue : " + action);
                    request.getRequestDispatcher("/menu.jsp").forward(request, response);

            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erreur", "Erreur système : " + e.getMessage());
            request.getRequestDispatcher("/menu.jsp").forward(request, response);
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

            GestionChambre gestionChambre = tpGestion.getGestionChambre();
            if (gestionChambre == null) {
                throw new TpExeception("Module de gestion des chambres indisponible");
            }

            switch (action) {
                case "ajouter" :
                    ajouterChambre(request, response);
                    break;
                case "modifier" :
                    modifierChambre(request, response, gestionChambre);
                    break;
                case "supprimer" :
                    supprimerChambre(request, response, gestionChambre);
                    break;
                default : {
                    request.setAttribute("erreur", "Action POST non reconnue : " + action);
                    request.getRequestDispatcher("/menu.jsp").forward(request, response);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erreur", "Erreur système : " + e.getMessage());
            request.getRequestDispatcher("/menu.jsp").forward(request, response);
        }
    }

    // =============================
    // Méthodes GET (affichage)
    // =============================

    private void afficherFormulaireAjouter(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/chambres/ajouterChambre.jsp").forward(request, response);
    }

    private void afficherFormulaireRecherche(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("message", "Formulaire de recherche (à implémenter)");
        request.getRequestDispatcher("/menu.jsp").forward(request, response);
    }

    private void afficherFormulaireChambresLibres(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/chambres/chambresLibres.jsp").forward(request, response);
    }

    private void afficherChambre(HttpServletRequest request, HttpServletResponse response, GestionChambre gestionChambre)
            throws ServletException, IOException {
        try {
            String nom = request.getParameter("nom");
            if (nom == null || nom.trim().isEmpty()) {
                throw new TpExeception("Le nom est obligatoire");
            }

            Chambre chambre = gestionChambre.afficherChambre(nom.trim());
            request.setAttribute("chambre", chambre);
            request.setAttribute("message", "Chambre trouvée !");
            request.getRequestDispatcher("/menu.jsp").forward(request, response);

        } catch (TpExeception e) {
            request.setAttribute("erreur", e.getMessage());
            request.getRequestDispatcher("/menu.jsp").forward(request, response);
        }
    }

    private void afficherChambresLibres(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String dateDebut = request.getParameter("dateDebut");
            String dateFin = request.getParameter("dateFin");

            if (dateDebut == null || dateFin == null || dateDebut.isEmpty() || dateFin.isEmpty()) {
                throw new TpExeception("Les deux dates sont obligatoires");
            }
            GestionChambre gestionChambre = getGestionChambre(request);
            List<Chambre> chambresLibres = gestionChambre.afficherChambresLibres(dateDebut, dateFin);
            request.setAttribute("chambresLibres", chambresLibres);
            request.setAttribute("dateDebut", dateDebut);
            request.setAttribute("dateFin", dateFin);
            request.getRequestDispatcher("/WEB-INF/chambres/chambresLibres.jsp").forward(request, response);

        } catch (TpExeception e) {
            request.setAttribute("erreur", e.getMessage());
            request.setAttribute("dateDebut", request.getParameter("dateDebut"));
            request.setAttribute("dateFin", request.getParameter("dateFin"));
            request.getRequestDispatcher("/WEB-INF/chambres/chambresLibres.jsp").forward(request, response);
        }
    }

    // =============================
    // Méthodes POST (traitement)
    // =============================

    private void ajouterChambre(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nom = request.getParameter("nom");
        String typeLit = request.getParameter("typeLit");
        String prixBaseStr = request.getParameter("prixBase");

        try {
            if (nom == null || nom.trim().isEmpty()) {
                throw new TpExeception("Le nom de la chambre est obligatoire");
            }
            if (typeLit == null || typeLit.trim().isEmpty()) {
                throw new TpExeception("Le type de lit est obligatoire");
            }

            double prixBase;
            try {
                prixBase = Double.parseDouble(prixBaseStr);
                if (prixBase < 0) {
                    throw new TpExeception("Le prix de base doit être positif");
                }
            } catch (NumberFormatException e) {
                throw new TpExeception("Le prix de base doit être un nombre valide");
            }

            GestionChambre gestionChambre = getGestionChambre(request);
            gestionChambre.ajouterChambre(nom.trim(), typeLit.trim(), prixBase);

            request.setAttribute("message", "Chambre '" + nom + "' ajoutée avec succès !");
            request.getRequestDispatcher("/WEB-INF/chambres/chambresLibres.jsp").forward(request, response);

        } catch (TpExeception e) {
            request.setAttribute("nom", nom);
            request.setAttribute("typeLit", typeLit);
            request.setAttribute("prixBase", prixBaseStr);
            request.setAttribute("erreur", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/chambres/ajouterChambre.jsp").forward(request, response);
        }
    }

    private void modifierChambre(HttpServletRequest request, HttpServletResponse response, GestionChambre gestionChambre)
            throws ServletException, IOException {
        try {
            String nomActuel = request.getParameter("nomActuel");
            String nouveauNom = request.getParameter("nouveauNom");
            String typeLit = request.getParameter("typeLit");
            String prixBaseStr = request.getParameter("prixBase");

            if (nomActuel == null || nomActuel.trim().isEmpty()) {
                throw new TpExeception("Le nom actuel est obligatoire");
            }
            if (nouveauNom == null || nouveauNom.trim().isEmpty()) {
                throw new TpExeception("Le nouveau nom est obligatoire");
            }

            double prixBase = Double.parseDouble(prixBaseStr);
            gestionChambre.modifierChambre(nomActuel.trim(), nouveauNom.trim(), typeLit.trim(), prixBase);

            request.setAttribute("message", "Chambre modifiée avec succès !");
            request.getRequestDispatcher("/menu.jsp").forward(request, response);

        } catch (TpExeception | NumberFormatException e) {
            request.setAttribute("erreur", e.getMessage());
            request.getRequestDispatcher("/menu.jsp").forward(request, response);
        }
    }

    private void supprimerChambre(HttpServletRequest request, HttpServletResponse response, GestionChambre gestionChambre)
            throws ServletException, IOException {
        try {
            String nom = request.getParameter("nom");
            if (nom == null || nom.trim().isEmpty()) {
                throw new TpExeception("Le nom de la chambre est obligatoire");
            }

            gestionChambre.supprimerChambre(nom.trim());
            request.setAttribute("message", "Chambre '" + nom + "' supprimée avec succès !");
            request.getRequestDispatcher("/menu.jsp").forward(request, response);

        } catch (TpExeception | SQLException e) {
            request.setAttribute("erreur", e.getMessage());
            request.getRequestDispatcher("/menu.jsp").forward(request, response);
        }
    }

    // =============================
    // Utilitaires
    // =============================

    private TpGestion getTpGestion(HttpSession session, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        TpGestion tpGestion = InnHelper.getInnInterro(session);
        if (tpGestion == null) {
            request.setAttribute("erreur", "Session expirée. Veuillez vous reconnecter.");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
        return tpGestion;
    }

    private GestionChambre getGestionChambre(HttpServletRequest request) {
        return InnHelper.getInnInterro(request.getSession()).getGestionChambre();
    }
}