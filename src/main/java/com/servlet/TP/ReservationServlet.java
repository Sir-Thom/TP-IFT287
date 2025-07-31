package com.servlet.TP;

import tp.TpExeception;
import tp.gestion.GestionReservation;
import tp.gestion.TpGestion;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;


public class ReservationServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            if (!InnHelper.peutProceder(getServletContext(), request, response)) {
                InnHelper.DispatchToBDConnect(request, response);
                return;
            }

            if ("reserver".equals(action)) {
                reserver(request, response);
            } else {
                response.sendRedirect("menu.jsp");
            }
        } catch (TpExeception e) {
            e.printStackTrace();
            request.setAttribute("erreur", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/Reservation/reserver.jsp").forward(request, response);
        }
    }

    private void reserver(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, TpExeception {

        HttpSession session = request.getSession();

        String prenomClient = request.getParameter("prenomClient");
        String nomClient = request.getParameter("nomClient");
        String nomChambre = request.getParameter("nomChambre");
        String dateDebut = request.getParameter("dateDebut");
        String dateFin = request.getParameter("dateFin");

        // Pour garder les valeurs en cas d'erreur
        request.setAttribute("prenomClient", prenomClient);
        request.setAttribute("nomClient", nomClient);
        request.setAttribute("nomChambre", nomChambre);
        request.setAttribute("dateDebut", dateDebut);
        request.setAttribute("dateFin", dateFin);

        TpGestion gestion = InnHelper.getInnInterro(session);
        GestionReservation gestionReservation = gestion.getGestionReservation();

        gestionReservation.reserver(prenomClient, nomClient, nomChambre, dateDebut, dateFin);

        request.setAttribute("message", "Réservation réussie.");
        request.getRequestDispatcher("/WEB-INF/Reservation/reserver.jsp").forward(request, response);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action manquante");
            return;
        }

        switch (action) {
            case "afficherFormAjouter":
                // Afficher la page pour réserver
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/Reservation/reserver.jsp");
                dispatcher.forward(request, response);
                break;

            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Action inconnue : " + action);
                break;
        }
    }

}
