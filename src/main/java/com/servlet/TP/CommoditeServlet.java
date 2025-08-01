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
                    request.getRequestDispatcher("/WEB-INF/commodites/inclureCommodite.jsp").forward(request, response);
                    break;
                case "afficherFormEnlever":
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
            TpGestion tpGestion = getTpGestion(session, request, response);
            if (tpGestion == null) return;

            GestionCommodite gestionCommodite = tpGestion.getGestionCommodite();

            switch (action) {
                case "ajouter":
                    String description = request.getParameter("description");
                    double surplus = Double.parseDouble(request.getParameter("surplus"));
                    gestionCommodite.ajouterCommodite(description, surplus);
                    response.sendRedirect("commodite?action=afficherFormAjouter&message=Ajout+réussi");
                    break;

                case "inclure":
                    String chambre = request.getParameter("chambreNom");
                    int idCommodite = Integer.parseInt(request.getParameter("idCommodite"));
                    gestionCommodite.inclureCommodite(chambre, idCommodite);
                    response.sendRedirect("commodite?action=lister&chambreNom=" + chambre);
                    break;

                case "enlever":
                    chambre = request.getParameter("chambreNom");
                    idCommodite = Integer.parseInt(request.getParameter("idCommodite"));
                    gestionCommodite.enleverCommodite(chambre, idCommodite);
                    response.sendRedirect("commodite?action=lister&chambreNom=" + chambre);
                    break;

                default:
                    throw new Exception("Action POST non valide : " + action);
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

    private void envoyerErreur(HttpServletRequest request, HttpServletResponse response, Exception e)
            throws ServletException, IOException {
        request.setAttribute("erreur", "Erreur : " + e.getMessage());
        request.getRequestDispatcher("/WEB-INF/messageErreur.jsp").forward(request, response);
    }
}

