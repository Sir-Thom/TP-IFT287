package com.servlet.TP;

import tp.TpExeception;
import tp.gestion.GestionChambre;
import tp.gestion.GestionCommodite;
import tp.gestion.TpGestion;
import tp.objets.Chambre;
import tp.objets.Commodite;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class ChambreServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("=== ChambreServlet POST appelé ===");
        String action = request.getParameter("action");
        System.out.println("Action reçue: " + action);

        processRequest(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("=== ChambreServlet GET appelé ===");
        String action = request.getParameter("action");
        System.out.println("Action reçue: " + action);

        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        String action = request.getParameter("action");

        System.out.println("=== DEBUG CHAMBRE SERVLET ===");
        System.out.println("Action: " + action);
        System.out.println("Method: " + request.getMethod());
        System.out.println("URI: " + request.getRequestURI());
        System.out.println("Query: " + request.getQueryString());

        // Si pas d'action, afficher page de debug
        if (action == null || action.trim().isEmpty()) {
                        return;
        }

        try {
            // Vérifier si on peut procéder
            if (!InnHelper.peutProceder(getServletContext(), request, response)) {
                System.out.println("InnHelper.peutProceder() a échoué");
                return;
            }

            // Récupération du gestionnaire depuis la session
            TpGestion tpGestion = InnHelper.getInnInterro(session);
            if (tpGestion == null) {
                System.out.println("TpGestion est null");
                request.setAttribute("erreur", "Session expirée. Veuillez vous reconnecter.");
                request.getRequestDispatcher("/index.jsp").forward(request, response);
                return;
            }

            // Récupération du gestionnaire de chambres
            GestionChambre gestionChambre = tpGestion.getGestionChambre();
            if (gestionChambre == null) {
                System.out.println("GestionChambre est null");
                request.setAttribute("erreur", "Gestionnaire de chambres non disponible.");
                request.getRequestDispatcher("/menu.jsp").forward(request, response);
                return;
            }

            System.out.println("Tous les gestionnaires sont OK, traitement de l'action: " + action);

            switch (action) {
                case "afficherFormAjouter":
                    System.out.println("→ Redirection vers ajouterChambre.jsp");
                    request.getRequestDispatcher("/WEB-INF/chambres/ajouterChambre.jsp").forward(request, response);
                    break;

                case "afficherFormRecherche":
                    System.out.println("→ Redirection vers afficherChambre.jsp");
                    request.getRequestDispatcher("/WEB-INF/chambres/afficherChambre.jsp").forward(request, response);
                    break;

                case "afficherFormSupprimer":
                    System.out.println("→ Redirection vers supprimerChambre.jsp");
                    request.getRequestDispatcher("/WEB-INF/chambres/supprimerChambre.jsp").forward(request, response);
                    break;


                case "afficherFormChambresLibres":
                    System.out.println("→ Redirection vers chambresLibres.jsp");
                    request.getRequestDispatcher("/WEB-INF/chambres/chambresLibres.jsp").forward(request, response);
                    break;

                case "ajouter":
                    System.out.println("→ Traitement ajout chambre");
                    ajouterChambre(request, response, gestionChambre);
                    break;

                case "afficher":
                    System.out.println("→ Traitement affichage chambre");
                    afficherChambre(request, response, gestionChambre);
                    break;

                case "supprimer":
                    System.out.println("→ Traitement suppression chambre");
                    supprimerChambre(request, response, gestionChambre);
                    break;

                case "chambresLibres":
                    System.out.println("→ Traitement recherche chambres libres");
                    rechercherChambresLibres(request, response, gestionChambre, tpGestion);
                    break;


                default:
                    System.out.println(" Action non reconnue: " + action);
                    request.setAttribute("erreur", "Action non reconnue: " + action);
                    request.getRequestDispatcher("/menu.jsp").forward(request, response);
            }

        } catch (Exception e) {
            System.err.println(" Erreur dans ChambreServlet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("erreur", "Erreur système: " + e.getMessage());
            request.getRequestDispatcher("/menu.jsp").forward(request, response);
        }
    }

    private void ajouterChambre(HttpServletRequest request, HttpServletResponse response,
                                GestionChambre gestionChambre) throws ServletException, IOException {
        try {
            String nom = request.getParameter("nom");
            String typeLit = request.getParameter("typeLit");
            String prixBaseStr = request.getParameter("prixBase");

            System.out.println("=== DEBUG AJOUT CHAMBRE ===");
            System.out.println("Nom reçu: " + nom);
            System.out.println("Type lit reçu: " + typeLit);
            System.out.println("Prix reçu: " + prixBaseStr);

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

            // Appel de la méthode métier
            System.out.println("Appel de gestionChambre.ajouterChambre...");
            gestionChambre.ajouterChambre(nom.trim(), typeLit.trim(), prixBase);
            System.out.println("Chambre ajoutée avec succès!");

            // Nettoyer les champs du formulaire après succès
            request.removeAttribute("nom");
            request.removeAttribute("typeLit");
            request.removeAttribute("prixBase");

            request.setAttribute("message", "Chambre '" + nom + "' ajoutée avec succès!");
            request.getRequestDispatcher("/WEB-INF/chambres/ajouterChambre.jsp").forward(request, response);

        } catch (TpExeception e) {
            System.err.println("Erreur TpExeception: " + e.getMessage());
            e.printStackTrace();

            // Préserver les valeurs saisies en cas d'erreur
            request.setAttribute("nom", request.getParameter("nom"));
            request.setAttribute("typeLit", request.getParameter("typeLit"));
            request.setAttribute("prixBase", request.getParameter("prixBase"));
            request.setAttribute("erreur", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/chambres/ajouterChambre.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("Erreur inattendue: " + e.getMessage());
            e.printStackTrace();

            request.setAttribute("erreur", "Erreur inattendue: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/chambres/ajouterChambre.jsp").forward(request, response);
        }
    }

    private void afficherChambre(HttpServletRequest request, HttpServletResponse response,
                                 GestionChambre gestionChambre) throws ServletException, IOException {
        try {
            String nom = request.getParameter("nom");

            System.out.println("=== DEBUG AFFICHAGE CHAMBRE ===");
            System.out.println("Nom reçu: " + nom);

            // Validation
            if (nom == null || nom.trim().isEmpty()) {
                throw new TpExeception("Le nom de la chambre est obligatoire");
            }

            // Appel de la méthode métier
            System.out.println("Appel de gestionChambre.afficherChambre...");
            Chambre chambre = gestionChambre.afficherChambre(nom.trim());
            System.out.println(" Chambre trouvée: " + chambre.toString());

            // Passer la chambre à la JSP
            request.setAttribute("chambre", chambre);
            request.setAttribute("nomRecherche", nom.trim());
            request.getRequestDispatcher("/WEB-INF/chambres/afficherChambre.jsp").forward(request, response);

        } catch (TpExeception e) {
            System.err.println("Erreur TpExeception: " + e.getMessage());
            e.printStackTrace();

            // Préserver la valeur saisie en cas d'erreur
            request.setAttribute("nomRecherche", request.getParameter("nom"));
            request.setAttribute("erreur", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/chambres/afficherChambre.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println(" Erreur inattendue: " + e.getMessage());
            e.printStackTrace();

            request.setAttribute("erreur", "Erreur inattendue: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/chambres/afficherChambre.jsp").forward(request, response);
        }
    }

    private void supprimerChambre(HttpServletRequest request, HttpServletResponse response,
                                  GestionChambre gestionChambre) throws ServletException, IOException {
        try {
            String nom = request.getParameter("nom");

            System.out.println("=== DEBUG SUPPRESSION CHAMBRE ===");
            System.out.println("Nom reçu: " + nom);

            // Validation simple
            if (nom == null || nom.trim().isEmpty()) {
                throw new TpExeception("Le nom de la chambre est obligatoire");
            }

            // Appel de la méthode métier
            System.out.println("Appel de gestionChambre.supprimerChambre...");
            gestionChambre.supprimerChambre(nom.trim());
            System.out.println("Chambre supprimée avec succès!");

            // Nettoyer les champs du formulaire après succès (comme dans ajouterChambre)
            request.removeAttribute("nom");

            request.setAttribute("message", "Chambre '" + nom + "' supprimée avec succès!");
            request.getRequestDispatcher("/WEB-INF/chambres/supprimerChambre.jsp").forward(request, response);

        } catch (TpExeception e) {
            System.err.println("Erreur TpExeception: " + e.getMessage());
            e.printStackTrace();

            // Préserver les valeurs saisies en cas d'erreur
            request.setAttribute("nom", request.getParameter("nom"));
            request.setAttribute("erreur", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/chambres/supprimerChambre.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("Erreur inattendue: " + e.getMessage());
            e.printStackTrace();

            request.setAttribute("erreur", "Erreur inattendue: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/chambres/supprimerChambre.jsp").forward(request, response);
        }
    }

    private void rechercherChambresLibres(HttpServletRequest request, HttpServletResponse response,
                                          GestionChambre gestionChambre, TpGestion tpGestion) throws ServletException, IOException {
        try {
            String dateDebut = request.getParameter("dateDebut");
            String dateFin = request.getParameter("dateFin");

            System.out.println("=== DEBUG RECHERCHE CHAMBRES LIBRES ===");
            System.out.println("Date début reçue: " + dateDebut);
            System.out.println("Date fin reçue: " + dateFin);

            // Validation des dates
            if (dateDebut == null || dateDebut.trim().isEmpty()) {
                throw new TpExeception("La date de début est obligatoire");
            }
            if (dateFin == null || dateFin.trim().isEmpty()) {
                throw new TpExeception("La date de fin est obligatoire");
            }

            // Validation du format des dates
            LocalDate debut, fin;
            try {
                debut = LocalDate.parse(dateDebut);
                fin = LocalDate.parse(dateFin);
            } catch (DateTimeParseException e) {
                throw new TpExeception("Format de date invalide. Utilisez le format AAAA-MM-JJ");
            }

            // Validation logique des dates
            if (!debut.isBefore(fin)) {
                throw new TpExeception("La date de début doit être antérieure à la date de fin");
            }

            if (debut.isBefore(LocalDate.now())) {
                throw new TpExeception("La date de début ne peut pas être dans le passé");
            }

            // Appel de la méthode métier
            System.out.println("Appel de gestionChambre.afficherChambresLibres...");
            List<Chambre> chambresLibres = gestionChambre.afficherChambresLibres(dateDebut, dateFin);
            System.out.println(chambresLibres.size() + " chambre(s) libre(s) trouvée(s)");

            // Calculer le prix total pour chaque chambre (prix de base + commodités)
            Map<Integer, Double> prixTotaux = new HashMap<>();
            GestionCommodite gestionCommodite = tpGestion.getGestionCommodite();

            for (Chambre chambre : chambresLibres) {
                double prixTotal = chambre.getPrixBase();

                // Ajouter le prix des commodités
                if (chambre.getCommodites() != null && !chambre.getCommodites().isEmpty()) {
                    for (Integer idCommodite : chambre.getCommodites()) {
                        try {
                            List<Commodite> commoditesDuChambre = gestionCommodite.obtenirCommoditesChambre(chambre.getNomChambre());
                            for (Commodite commodite : commoditesDuChambre) {
                                if (commodite.getIdCommodite() == idCommodite) {
                                    prixTotal += commodite.getSurplusPrix();
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            System.err.println("Erreur lors du calcul des commodités pour chambre " + chambre.getNomChambre() + ": " + e.getMessage());
                        }
                    }
                }

                prixTotaux.put(chambre.getIdChambre(), prixTotal);
                System.out.println("Chambre " + chambre.getNomChambre() + " - Prix total: " + prixTotal + " CAD");
            }

            // Passer les données à la JSP
            request.setAttribute("chambresLibres", chambresLibres);
            request.setAttribute("prixTotaux", prixTotaux);
            request.setAttribute("dateDebut", dateDebut);
            request.setAttribute("dateFin", dateFin);

            // Calculer le nombre de nuits
            long nombreNuits = debut.until(fin).getDays();
            request.setAttribute("nombreNuits", nombreNuits);

            request.getRequestDispatcher("/WEB-INF/chambres/chambresLibres.jsp").forward(request, response);

        } catch (TpExeception e) {
            System.err.println("Erreur TpExeception: " + e.getMessage());
            e.printStackTrace();

            // Préserver les valeurs saisies en cas d'erreur
            request.setAttribute("dateDebut", request.getParameter("dateDebut"));
            request.setAttribute("dateFin", request.getParameter("dateFin"));
            request.setAttribute("erreur", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/chambres/chambresLibres.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("Erreur inattendue: " + e.getMessage());
            e.printStackTrace();

            request.setAttribute("erreur", "Erreur inattendue: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/chambres/chambresLibres.jsp").forward(request, response);
        }
    }


}