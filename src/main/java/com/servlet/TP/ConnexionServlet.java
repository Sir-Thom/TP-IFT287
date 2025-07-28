package com.servlet.TP;

import tp.TpExeception;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

//@WebServlet("/Connexion")
public class ConnexionServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("connexion");

        if ("configurer".equals(action)) {
            configurerBD(request, response);
        } else {
            request.setAttribute("erreur", "Action non reconnue");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("reconfigurer".equals(action)) {
            reconfigurer(request, response);
        } else if ("deconnecter".equals(action)) {
            deconnecter(request, response);
        } else {
            // Rediriger vers l'index par défaut
            response.sendRedirect("index.jsp");
        }
    }

    private void configurerBD(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String serveur = request.getParameter("serveur");
            String bd = request.getParameter("bd");
            String user = request.getParameter("user");
            String pass = request.getParameter("pass");

            // Validation des paramètres
            if (serveur == null || serveur.trim().isEmpty()) {
                throw new TpExeception("Le type de serveur est obligatoire");
            }
            if (bd == null || bd.trim().isEmpty()) {
                throw new TpExeception("Le nom de la base de données est obligatoire");
            }
            if (user == null || user.trim().isEmpty()) {
                throw new TpExeception("Le nom d'utilisateur est obligatoire");
            }
            if (pass == null || pass.trim().isEmpty()) {
                throw new TpExeception("Le mot de passe est obligatoire");
            }

            // Sauvegarde dans le contexte de l'application
            ServletContext context = getServletContext();
            context.setAttribute("serveur", serveur.trim());
            context.setAttribute("bd", bd.trim());
            context.setAttribute("user", user.trim());
            context.setAttribute("pass", pass.trim());

            // Test de la connexion en créant une session
            HttpSession session = request.getSession(true);
            try {
                InnHelper.creerGestionnaire(context, session);

                // Succès - rediriger vers le menu principal
                response.sendRedirect("menu.jsp");

            } catch (Exception e) {
                // Erreur de connexion - nettoyer le contexte
                context.removeAttribute("serveur");
                context.removeAttribute("bd");
                context.removeAttribute("user");
                context.removeAttribute("pass");

                throw new TpExeception("Erreur de connexion à la base de données: " + e.getMessage());
            }

        } catch (TpExeception e) {
            // Préserver les valeurs saisies en cas d'erreur
            request.setAttribute("serveur", request.getParameter("serveur"));
            request.setAttribute("bd", request.getParameter("bd"));
            request.setAttribute("user", request.getParameter("user"));
            // Ne pas préserver le mot de passe pour la sécurité

            request.setAttribute("erreur", e.getMessage());
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }

    private void reconfigurer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Nettoyer la configuration existante
        ServletContext context = getServletContext();
        context.removeAttribute("serveur");
        context.removeAttribute("bd");
        context.removeAttribute("user");
        context.removeAttribute("pass");

        // Invalider la session courante
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Rediriger vers la page de configuration
        response.sendRedirect("index.jsp");
    }

    private void deconnecter(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Invalider la session utilisateur mais garder la config BD
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Rediriger vers le menu (la BD reste configurée)
        response.sendRedirect("menu.jsp");
    }
}