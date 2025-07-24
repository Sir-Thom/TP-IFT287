package com.servlet.TP;

import tp.TpExeception;
import tp.bdd.Securite;

import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;


/**
 * Servlet qui g�re la connexion d'un utilisateur au syst�me de gestion de
 * biblioth�que
 *
 * <pre>
 * Vincent Ducharme
 * Université de Sherbrooke
 * Version 1.0 - 11 novembre 2018
 * IFT287 - Exploitation de BD relationnelles et OO
 *
 * Frédéric Bergeron
 * Université de Sherbrooke
 * Version 2.0 - 14 novembre 2019
 * IFT287 - Exploitation de BD relationnelles et OO
 * </pre>
 */

public class Accueil extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Servlet Accueil : GET");
        if (InnHelper.peutProceder(getServletContext(), request, response)) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/accueil.jsp");
            dispatcher.forward(request, response);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response); // POST = GET, plus besoin de login
    }


}
