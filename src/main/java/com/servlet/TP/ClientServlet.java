package com.servlet.TP;

import tp.TpExeception;
import tp.gestion.GestionClient;
import tp.gestion.GestionReservation;
import tp.gestion.TpGestion;
import tp.objets.Client;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;


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

        // 1. Récupérer l'identifiant du client depuis le paramètre de l'URL
        String clientIdentifier = request.getParameter("id");
        if (clientIdentifier == null || clientIdentifier.isEmpty()) {
            throw new TpExeception("Aucun client n'a été spécifié.");
        }

        // 2. Extraire le prénom et le nom
        String[] parts = clientIdentifier.split("\\|");
        if (parts.length != 2) {
            throw new TpExeception("L'identifiant du client est mal formaté.");
        }
        String prenom = parts[0];
        String nom = parts[1];

        // 3. Obtenir les objets de gestion depuis la session
        TpGestion tpGestion = InnHelper.getInnInterro(request.getSession());
        GestionClient gestionClient = tpGestion.getGestionClient();
        // Assurez-vous que TpGestion peut accéder à GestionReservation
        // par une méthode comme getGestionReservation()
        GestionReservation gestionReservation = tpGestion.getGestionReservation();

        // 4. Récupérer les données du client et de ses réservations
        // (Ces méthodes doivent exister dans vos classes de gestion)
        Client client = gestionClient.GetClientByNomPrenom(nom, prenom);
        List<tp.objets.Reservation> reservations = gestionReservation.getReservationsPourClient(prenom, nom);

        if (client == null) {
            throw new TpExeception("Le client demandé n'existe pas.");
        }

        // 5. Placer les objets dans la requête pour la page JSP
        request.setAttribute("client", client);
        request.setAttribute("reservations", reservations);

        // 6. Transférer à la nouvelle page JSP
        request.getRequestDispatcher("/WEB-INF/clients/afficherClient.jsp").forward(request, response);
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
            // use envoyerErreur
        } catch (Exception e) {
        }
    }

    private void retirerClient(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, TpExeception {
        // Récupérer l'identifiant composite du client
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

            // Supprimer le client en utilisant prénom et nom
            gestionClient.supprimerClient(prenom, nom);

            // Recharger la liste des clients
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