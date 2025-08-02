package com.servlet.TP;

import tp.TpExeception;
import tp.gestion.GestionChambre;
import tp.gestion.GestionClient;
import tp.gestion.GestionCommodite;
import tp.gestion.GestionReservation;
import tp.gestion.TpGestion;
import tp.objets.Chambre;
import tp.objets.Client;
import tp.objets.Commodite;
import tp.objets.Reservation;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ClientServlet extends HttpServlet {

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

            if (!"afficherFormAjouter".equals(action) &&
                    (session == null || InnHelper.getInnInterro(session) == null)) {
                response.sendRedirect("index.jsp");
                return;
            }

            switch (action) {
                case "afficherFormAjouter":
                    afficherFormulaireAjouter(request, response);
                    break;
                case "afficherFormRetirer":
                    afficherFormulaireRetirer(request, response);
                    break;
                case "lister":
                    listerClients(request, response);
                    break;
                case "afficherClient":
                    afficherClient(request, response);
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

        if (InnHelper.getInnInterro(session) == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        String action = request.getParameter("action");

        try {
            switch (action) {
                case "ajouter":
                    ajouterClient(request, response);
                    break;
                case "retirer":
                    retirerClient(request, response);
                    break;

                default:
                    throw new TpExeception("Action POST non valide : " + action);
            }
        } catch (Exception e) {
            envoyerErreur(request, response, e);
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

    private void afficherFormulaireAjouter(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/WEB-INF/clients/ajouterClient.jsp").forward(request, response);
    }

    private void afficherFormulaireRetirer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, TpExeception {
        TpGestion tpGestion = InnHelper.getInnInterro(request.getSession());
        GestionClient gestionClient = tpGestion.getGestionClient();
        List<Client> clients = gestionClient.getListClients();
        request.setAttribute("clients", clients);
        request.getRequestDispatcher("/WEB-INF/clients/retirerClient.jsp").forward(request, response);
    }

    private void afficherClient(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, TpExeception {

        String clientIdentifier = request.getParameter("id");
        if (clientIdentifier == null || clientIdentifier.isEmpty()) {
            throw new TpExeception("Aucun client n'a été spécifié.");
        }

        String[] parts = clientIdentifier.split("\\|");
        if (parts.length != 2) {
            throw new TpExeception("L'identifiant du client est mal formaté.");
        }
        String prenom = parts[0];
        String nom = parts[1];

        TpGestion tpGestion = InnHelper.getInnInterro(request.getSession());
        GestionClient gestionClient = tpGestion.getGestionClient();
        GestionReservation gestionReservation = tpGestion.getGestionReservation();
        GestionChambre gestionChambre = tpGestion.getGestionChambre();
        GestionCommodite gestionCommodite = tpGestion.getGestionCommodite();

        Client client = gestionClient.GetClientByNomPrenom(nom, prenom);
        List<Reservation> reservations = gestionReservation.getReservationsPourClient(prenom, nom);

        if (client == null) {
            throw new TpExeception("Le client demandé n'existe pas.");
        }

        // Calcul des prix totaux et noms des chambres
        Map<Integer, Double> prixTotaux = new HashMap<>();
        Map<Integer, String> nomsChambre = new HashMap<>();

        for (Reservation reservation : reservations) {
            try {
                // Récupérer la chambre
                Chambre chambre = gestionChambre.afficherChambre(getChambreNameById(gestionChambre, reservation.getIdChambre()));

                if (chambre != null) {
                    // Stocker le nom de la chambre
                    nomsChambre.put(reservation.getIdReservation(), chambre.getNomChambre());

                    // Calculer le nombre de jours
                    LocalDate dateDebut = LocalDate.parse(reservation.getDateDebut());
                    LocalDate dateFin = LocalDate.parse(reservation.getDateFin());
                    long nombreJours = ChronoUnit.DAYS.between(dateDebut, dateFin);

                    // Prix de base
                    double prixBase = chambre.getPrixBase();

                    // Calculer le prix des commodités
                    double prixCommodites = 0.0;
                    if (chambre.getCommodites() != null && !chambre.getCommodites().isEmpty()) {
                        for (Integer idCommodite : chambre.getCommodites()) {
                            try {
                                List<Commodite> commoditesChambre = gestionCommodite.obtenirCommoditesChambre(chambre.getNomChambre());
                                for (Commodite commodite : commoditesChambre) {
                                    if (commodite.getIdCommodite() == idCommodite) {
                                        prixCommodites += commodite.getSurplusPrix();
                                        break;
                                    }
                                }
                            } catch (Exception e) {
                                System.err.println("Erreur lors du calcul des commodités pour la chambre " + chambre.getNomChambre() + ": " + e.getMessage());
                            }
                        }
                    }

                    // Prix total = (prix base + prix commodités) * nombre de jours
                    double prixTotal = (prixBase + prixCommodites) * nombreJours;
                    prixTotaux.put(reservation.getIdReservation(), prixTotal);

                } else {
                    // Chambre non trouvée
                    nomsChambre.put(reservation.getIdReservation(), "Chambre #" + reservation.getIdChambre() + " (supprimée)");
                    prixTotaux.put(reservation.getIdReservation(), 0.0);
                }

            } catch (Exception e) {
                System.err.println("Erreur lors du calcul du prix pour la réservation " + reservation.getIdReservation() + ": " + e.getMessage());
                nomsChambre.put(reservation.getIdReservation(), "Chambre #" + reservation.getIdChambre() + " (erreur)");
                prixTotaux.put(reservation.getIdReservation(), 0.0);
            }
        }

        // Ajouter les attributs à la requête
        request.setAttribute("client", client);
        request.setAttribute("reservations", reservations);
        request.setAttribute("prixTotaux", prixTotaux);
        request.setAttribute("nomsChambre", nomsChambre);

        request.getRequestDispatcher("/WEB-INF/clients/afficherClient.jsp").forward(request, response);
    }

    /**
     * Méthode utilitaire pour récupérer le nom d'une chambre par son ID
     */
    private String getChambreNameById(GestionChambre gestionChambre, int idChambre) {
        try {
            List<Chambre> toutesChambres = gestionChambre.getAllChambres();
            for (Chambre chambre : toutesChambres) {
                if (chambre.getIdChambre() == idChambre) {
                    return chambre.getNomChambre();
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la recherche de la chambre ID " + idChambre + ": " + e.getMessage());
        }
        return "Chambre #" + idChambre;
    }

    private void listerClients(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, TpExeception {

        TpGestion tpGestion = InnHelper.getInnInterro(request.getSession());

        if (tpGestion == null) {
            throw new TpExeception("Session expirée ou non initialisée");
        }

        GestionClient gestionClient = tpGestion.getGestionClient();

        if (gestionClient == null) {
            throw new TpExeception("GestionClient non initialisé");
        }

        List<Client> clients = gestionClient.getListClients();
        request.setAttribute("clients", clients);
        request.getRequestDispatcher("/WEB-INF/clients/listeClients.jsp").forward(request, response);
    }

    private void ajouterClient(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String ageStr = request.getParameter("age");

        try {
            int age = Integer.parseInt(ageStr.trim());

            GestionClient gestionClient = InnHelper.getInnInterro(request.getSession()).getGestionClient();
            gestionClient.ajouterClient(nom.trim(), prenom.trim(), age);

            response.sendRedirect("client?action=lister");

        } catch (TpExeception | NumberFormatException e) {
            request.setAttribute("erreur", e.getMessage());
            System.out.println(request.getAttribute("erreur"));
            request.setAttribute("nom", nom);
            request.setAttribute("prenom", prenom);
            request.setAttribute("age", ageStr);
            request.getRequestDispatcher("/WEB-INF/clients/ajouterClient.jsp").forward(request, response);
        } catch (Exception e) {
        }
    }

    private void retirerClient(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, TpExeception {
        String clientIdentifier = request.getParameter("clientIdentifier");

        if (clientIdentifier == null || clientIdentifier.trim().isEmpty()) {
            request.setAttribute("erreur", "Veuillez sélectionner un client.");
            afficherFormulaireRetirer(request, response);
            return;
        }

        try {
            // Séparer le prénom et le nom (séparés par '|')
            String[] parts = clientIdentifier.split("\\|");
            if (parts.length != 2) {
                throw new TpExeception("Identifiant client invalide");
            }

            String prenom = parts[0];
            String nom = parts[1];

            TpGestion tpGestion = InnHelper.getInnInterro(request.getSession());
            GestionClient gestionClient = tpGestion.getGestionClient();

            gestionClient.supprimerClient(prenom, nom);

            List<Client> clients = gestionClient.getListClients();
            request.setAttribute("clients", clients);
            request.setAttribute("message", "Le client " + prenom + " " + nom + " a été retiré avec succès.");
            request.getRequestDispatcher("/WEB-INF/clients/retirerClient.jsp").forward(request, response);

        } catch (TpExeception e) {
            request.setAttribute("erreur", e.getMessage());
            afficherFormulaireRetirer(request, response);
        }
    }

    private void envoyerErreur(HttpServletRequest request, HttpServletResponse response, Exception e)
            throws ServletException, IOException {

        request.setAttribute("erreur", "Erreur système : " + e.getMessage());
        request.getRequestDispatcher("/WEB-INF/messageErreur.jsp").forward(request, response);
    }
}