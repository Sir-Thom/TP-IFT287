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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        String action = request.getParameter("action");
        System.out.println("Action parameter: " + action);

        try {
            // Vérifier si on peut procéder
            if (!InnHelper.peutProceder(getServletContext(), request, response)) {
                System.out.println("Erreur: InnHelper.peutProceder returned false."); // ✅ 3. Check here
                return;
            }

            TpGestion tpGestion = InnHelper.getInnInterro(session);
            if (tpGestion == null) {
                System.out.println("Erreur: tpGestion is null. Session might be expired."); // ✅ 4. Check here
                request.setAttribute("erreur", "Session expirée. Veuillez vous reconnecter.");
               // request.getRequestDispatcher("/index.jsp").forward(request, response);
                return;
            }

            // Récupération du gestionnaire de chambres
            GestionChambre gestionChambre = tpGestion.getGestionChambre();

            switch (action) {
                // ✅ NOUVELLES ACTIONS pour afficher les formulaires
                case "afficherFormAjouter":
                    afficherFormulaireAjouter(request, response);
                    break;

                case "afficherFormRecherche":
                    afficherFormulaireRecherche(request, response);
                    break;

                case "afficherFormChambresLibres":
                    afficherFormulaireChambresLibres(request, response);
                    break;

                // ✅ ACTIONS EXISTANTES (traitement des données)
                case "ajouter":
                    ajouterChambre(request, response);
                    break;

                case "modifier":
                    modifierChambre(request, response, gestionChambre);
                    break;

                case "supprimer":
                    supprimerChambre(request, response, gestionChambre);
                    break;

                case "afficher":
                    afficherChambre(request, response, gestionChambre);
                    break;

                case "chambresLibres":
                    afficherChambresLibres(request, response, gestionChambre);
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

    // ✅ NOUVELLES MÉTHODES pour afficher les formulaires vides
    private void afficherFormulaireAjouter(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Afficher le formulaire vide pour ajouter une chambre
        request.getRequestDispatcher("/WEB-INF/chambres/ajouterChambre.jsp").forward(request, response);
    }

    private void afficherFormulaireRecherche(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Afficher le formulaire de recherche (pour plus tard)
        request.setAttribute("message", "Formulaire de recherche - à implémenter");
        request.getRequestDispatcher("/menu.jsp").forward(request, response);
    }

    private void afficherFormulaireChambresLibres(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Afficher le formulaire des chambres libres
        request.getRequestDispatcher("/WEB-INF/chambres/chambresLibres.jsp").forward(request, response);
    }

    // ✅ MÉTHODES EXISTANTES avec chemins corrigés
    private void ajouterChambre(HttpServletRequest request, HttpServletResponse response
                                ) throws ServletException, IOException {
        HttpSession session = request.getSession();

        try {
            String nom = request.getParameter("nom");
            String typeLit = request.getParameter("typeLit");
            String prixBaseStr = request.getParameter("prixBase");

            // Validation
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
            System.out.println("Attempting to add room: " + nom);
            InnHelper.getInnInterro(session);


            GestionChambre gestionChambre =
                    InnHelper.getInnInterro(session).getGestionChambre();
            if (gestionChambre == null) {
                throw new Exception("Module gestion chambre non disponible");
            }
            // Appel de la méthode métier
            gestionChambre.ajouterChambre(nom.trim(), typeLit.trim(), prixBase);

            request.setAttribute("message", "Chambre '" + nom + "' ajoutée avec succès!");
            // ✅ CHEMIN CORRIGÉ vers WEB-INF
            request.getRequestDispatcher("/WEB-INF/chambres/ajouterChambre.jsp").forward(request, response);

        } catch (TpExeception e) {
            // Préserver les valeurs saisies en cas d'erreur
            request.setAttribute("nom", request.getParameter("nom"));
            request.setAttribute("typeLit", request.getParameter("typeLit"));
            request.setAttribute("prixBase", request.getParameter("prixBase"));
            request.setAttribute("erreur", e.getMessage());
            // ✅ CHEMIN CORRIGÉ vers WEB-INF
            request.getRequestDispatcher("/WEB-INF/chambres/ajouterChambre.jsp").forward(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void modifierChambre(HttpServletRequest request, HttpServletResponse response,
                                 GestionChambre gestionChambre) throws ServletException, IOException {
        try {
            String nomActuel = request.getParameter("nomActuel");
            String nouveauNom = request.getParameter("nouveauNom");
            String typeLit = request.getParameter("typeLit");
            String prixBaseStr = request.getParameter("prixBase");

            // Validation
            if (nomActuel == null || nomActuel.trim().isEmpty()) {
                throw new TpExeception("Le nom actuel de la chambre est obligatoire");
            }
            if (nouveauNom == null || nouveauNom.trim().isEmpty()) {
                throw new TpExeception("Le nouveau nom de la chambre est obligatoire");
            }

            double prixBase = Double.parseDouble(prixBaseStr);

            gestionChambre.modifierChambre(nomActuel.trim(), nouveauNom.trim(), typeLit.trim(), prixBase);

            request.setAttribute("message", "Chambre modifiée avec succès!");
            // ✅ CHEMIN CORRIGÉ (créer ce JSP plus tard)
            request.setAttribute("message", "Chambre modifiée avec succès!");
            request.getRequestDispatcher("/menu.jsp").forward(request, response);

        } catch (TpExeception | NumberFormatException e) {
            request.setAttribute("erreur", e.getMessage());
            request.getRequestDispatcher("/menu.jsp").forward(request, response);
        }
    }

    private void supprimerChambre(HttpServletRequest request, HttpServletResponse response,
                                  GestionChambre gestionChambre) throws ServletException, IOException {
        try {
            String nom = request.getParameter("nom");

            if (nom == null || nom.trim().isEmpty()) {
                throw new TpExeception("Le nom de la chambre est obligatoire");
            }

            gestionChambre.supprimerChambre(nom.trim());

            request.setAttribute("message", "Chambre '" + nom + "' supprimée avec succès!");
            request.getRequestDispatcher("/menu.jsp").forward(request, response);

        } catch (TpExeception | SQLException e) {
            request.setAttribute("erreur", e.getMessage());
            request.getRequestDispatcher("/menu.jsp").forward(request, response);
        }
    }

    private void afficherChambre(HttpServletRequest request, HttpServletResponse response,
                                 GestionChambre gestionChambre) throws ServletException, IOException {
        try {
            String nom = request.getParameter("nom");

            if (nom == null || nom.trim().isEmpty()) {
                throw new TpExeception("Le nom de la chambre est obligatoire");
            }

            Chambre chambre = gestionChambre.afficherChambre(nom.trim());

            request.setAttribute("chambre", chambre);
            request.setAttribute("message", "Chambre trouvée!");
            request.getRequestDispatcher("/menu.jsp").forward(request, response);

        } catch (TpExeception e) {
            request.setAttribute("erreur", e.getMessage());
            request.getRequestDispatcher("/menu.jsp").forward(request, response);
        }
    }

    private void afficherChambresLibres(HttpServletRequest request, HttpServletResponse response,
                                        GestionChambre gestionChambre) throws ServletException, IOException {
        try {
            String dateDebut = request.getParameter("dateDebut");
            String dateFin = request.getParameter("dateFin");

            if (dateDebut == null || dateDebut.trim().isEmpty() ||
                    dateFin == null || dateFin.trim().isEmpty()) {
                throw new TpExeception("Les dates de début et de fin sont obligatoires");
            }

            List<Chambre> chambresLibres = gestionChambre.afficherChambresLibres(dateDebut, dateFin);

            request.setAttribute("chambresLibres", chambresLibres);
            request.setAttribute("dateDebut", dateDebut);
            request.setAttribute("dateFin", dateFin);
            // ✅ CHEMIN CORRIGÉ vers WEB-INF
            request.getRequestDispatcher("/WEB-INF/chambres/chambresLibres.jsp").forward(request, response);

        } catch (TpExeception e) {
            request.setAttribute("erreur", e.getMessage());
            request.setAttribute("dateDebut", request.getParameter("dateDebut"));
            request.setAttribute("dateFin", request.getParameter("dateFin"));
            // ✅ CHEMIN CORRIGÉ vers WEB-INF
            request.getRequestDispatcher("/WEB-INF/chambres/chambresLibres.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Rediriger les GET vers POST pour éviter les erreurs
        doPost(request, response);
    }
}