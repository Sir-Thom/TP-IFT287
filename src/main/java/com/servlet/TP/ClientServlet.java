package com.servlet.TP;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoIterable;
import tp.TpExeception;
import tp.gestion.GestionClient;
import tp.gestion.TpGestion;
import tp.objets.Client; // Ajout nécessaire

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List; // Ajout nécessaire

// Ajout de l'annotation
public class ClientServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();




        String action = request.getParameter("action");

        try {


            System.out.println("session = " + session.toString());


            // Validation de session pour toutes les actions sauf afficherFormAjouter
            if (!"afficherFormAjouter".equals(action) &&
                    (session == null || InnHelper.getInnInterro(session) == null)) {
                System.out.println(InnHelper.getInnInterro(session).getConnexion());
                response.sendRedirect("index.jsp");
                return;
            }
            InnHelper.peutProceder(getServletContext(), request, response);
            TpGestion tpGestion = InnHelper.getInnInterro(session);
            if (tpGestion == null) {
                System.err.println("Erreur: tpGestion est null dans la session");
                throw new TpExeception("Session expirée ou non initialisée");
            }

            switch (action) {
                case "afficherFormAjouter":
                    afficherFormulaireAjouter(request, response);
                    break;
                case "lister": // Nouvelle action
                    listerClients(request, response);
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
            GestionClient gestionClient = InnHelper.getInnInterro(session).getGestionClient();
            switch (action) {
                case "ajouter":
                    ajouterClient(request, response);
                    break;
                default:
                    throw new TpExeception("Action POST non valide : " + action);
            }
        } catch (Exception e) {
            envoyerErreur(request, response, e);
        }
    }

    // =================================================================
    // MÉTHODES PRIVÉES
    // =================================================================

    private void afficherFormulaireAjouter(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/clients/ajouterClient.jsp").forward(request, response);
    }

    private void listerClients(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, TpExeception {

        HttpSession session = request.getSession();
        TpGestion tpGestion = InnHelper.getInnInterro(session);
        tpGestion.setGestionClient(tpGestion.getGestionClient());

        if (tpGestion == null) {
            System.err.println("Erreur: tpGestion est null dans la session");
            throw new TpExeception("Session expirée ou non initialisée");
        }

        GestionClient gestionClient = tpGestion.getGestionClient();


        if (gestionClient == null) {
            System.err.println("Erreur: gestionClient est null");
            throw new TpExeception("GestionClient non initialisé");
        }

        List<Client> clients = gestionClient.getListClients();
        System.out.println("Nombre de clients récupérés: " + clients.size());

        for (Client client : clients) {
            System.out.println("Client: " + client.getPrenom() + " " + client.getNom());
        }

        request.setAttribute("clients", clients);
        request.getRequestDispatcher("/WEB-INF/clients/listeClients.jsp").forward(request, response);
    }
    private void ajouterClient(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String ageStr = request.getParameter("age");
        GestionClient gestionClient = InnHelper.getInnInterro(request.getSession()).getGestionClient();
        try {
            // Validation des champs...
            gestionClient.ajouterClient(nom.trim(), prenom.trim(), Integer.parseInt(ageStr.trim()));

            // Redirection vers la liste après succès
            response.sendRedirect("ClientServlet?action=lister");

        } catch (TpExeception | NumberFormatException e) {
            // Gestion erreur avec conservation des données
            request.setAttribute("erreur", e.getMessage());
            request.setAttribute("nom", nom);
            request.setAttribute("prenom", prenom);
            request.setAttribute("age", ageStr);
            request.getRequestDispatcher("/WEB-INF/clients/ajouterClient.jsp").forward(request, response);
        }
    }

    private void envoyerErreur(HttpServletRequest request, HttpServletResponse response, Exception e)
            throws ServletException, IOException {
        request.setAttribute("erreur", "Erreur système : " + e.getMessage());
        request.getRequestDispatcher("/WEB-INF/messageErreur.jsp").forward(request, response);
    }
}