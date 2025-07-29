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
                case "lister":
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

            response.sendRedirect("ClientServlet?action=lister");

        } catch (TpExeception | NumberFormatException e) {
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