package com.servlet.TP;

import tp.TpExeception;
import tp.gestion.GestionClient;
import tp.gestion.GestionReservation;
import tp.gestion.TpGestion;
import tp.objets.Chambre;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;


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

        String clientIdentifier = request.getParameter("clientIdentifier");
        String[] parts = clientIdentifier.split("\\|");
        String prenomClient = parts.length > 0 ? parts[0] : "";
        String nomClient = parts.length > 1 ? parts[1] : "";

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
        GestionClient gestionClient = gestion.getGestionClient(); // Simplified this line

        List<tp.objets.Client> clients = gestionClient.getListClients();
        List<Chambre> chambres = gestion.getGestionChambre().getAllChambres();
        // Put in request scope
        request.setAttribute("clients", clients);
        request.setAttribute("chambres", chambres);       request.setAttribute("clients", clients);
        request.setAttribute("chambres", chambres);

        GestionReservation gestionReservation = gestion.getGestionReservation();

        gestionReservation.reserver(prenomClient, nomClient, nomChambre, dateDebut, dateFin);

        request.setAttribute("message", "Réservation réussie.");
        request.getRequestDispatcher("/WEB-INF/Reservation/reserver.jsp").forward(request, response);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Optional but good practice to wrap in a try-catch
        // Add this check at the beginning
        if (!InnHelper.peutProceder(getServletContext(), request, response)) {
            InnHelper.DispatchToBDConnect(request, response);
            return;
        }

        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        TpGestion gestion = InnHelper.getInnInterro(session);

        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action manquante");
            return;
        }

        switch (action) {
            case "afficherFormAjouter":
                GestionClient gestionClient = gestion.getGestionClient(); // Simplified this line
                // Fetch clients and chambres
                List<tp.objets.Client> clients = gestionClient.getListClients();
                List<Chambre> chambres = gestion.getGestionChambre().getAllChambres();
                // Put in request scope
                request.setAttribute("clients", clients);
                request.setAttribute("chambres", chambres);
                // Forward to form
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/Reservation/reserver.jsp");
                dispatcher.forward(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Action inconnue : " + action);
                break;
        }
    }

}
