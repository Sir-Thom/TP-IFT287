package com.servlet.TP;

import tp.TpExeception;
import tp.gestion.GestionClient;
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

            // Récupération du gestionnaire de clients
            GestionClient gestionClient = tpGestion.getGestionClient();

            switch (action) {
                case "afficherFormAjouter":
                    afficherFormulaireAjouter(request, response);
                    break;

                case "ajouter":
                    ajouterClient(request, response, gestionClient);
                    break;

                case "lister":
                    listerClients(request, response, gestionClient);
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
        request.getRequestDispatcher("/WEB-INF/clients/ajouterClient.jsp").forward(request, response);
    }

    private void ajouterClient(HttpServletRequest request, HttpServletResponse response,
                               GestionClient gestionClient) throws ServletException, IOException {
        try {
            String nom = request.getParameter("nom");
            String prenom = request.getParameter("prenom");
            String ageStr = request.getParameter("age");

            System.out.println("=== DEBUG AJOUT CLIENT ===");
            System.out.println("Nom reçu: " + nom);
            System.out.println("Prénom reçu: " + prenom);
            System.out.println("Âge reçu: " + ageStr);

            // Validation
            if (nom == null || nom.trim().isEmpty()) {
                throw new TpExeception("Le nom du client est obligatoire");
            }
            if (prenom == null || prenom.trim().isEmpty()) {
                throw new TpExeception("Le prénom du client est obligatoire");
            }

            int age;
            try {
                age = Integer.parseInt(ageStr);
                if (age <= 0 || age > 120) {
                    throw new TpExeception("L'âge doit être entre 1 et 120 ans");
                }
            } catch (NumberFormatException e) {
                throw new TpExeception("L'âge doit être un nombre valide");
            }

            // Appel de la méthode métier
            System.out.println("Appel de gestionClient.ajouterClient...");
            gestionClient.ajouterClient(nom.trim(), prenom.trim(), age);
            System.out.println("Client ajouté avec succès!");

            request.setAttribute("message", "Client '" + prenom + " " + nom + "' ajouté avec succès!");
            request.getRequestDispatcher("/WEB-INF/clients/ajouterClient.jsp").forward(request, response);

        } catch (TpExeception e) {
            System.err.println("Erreur TpExeception: " + e.getMessage());
            e.printStackTrace();

            // On préserve les valeurs saisies en cas d'erreur
            request.setAttribute("nom", request.getParameter("nom"));
            request.setAttribute("prenom", request.getParameter("prenom"));
            request.setAttribute("age", request.getParameter("age"));
            request.setAttribute("erreur", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/clients/ajouterClient.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("Erreur inattendue: " + e.getMessage());
            e.printStackTrace();

            request.setAttribute("erreur", "Erreur inattendue: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/clients/ajouterClient.jsp").forward(request, response);
        }
    }

    private void listerClients(HttpServletRequest request, HttpServletResponse response,
                               GestionClient gestionClient) throws ServletException, IOException {
        try {
            List<Client> clients = gestionClient.getListClients();
            request.setAttribute("clients", clients);
            request.getRequestDispatcher("/WEB-INF/clients/listeClients.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("erreur", "Erreur lors de la récupération des clients: " + e.getMessage());
            request.getRequestDispatcher("/menu.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}