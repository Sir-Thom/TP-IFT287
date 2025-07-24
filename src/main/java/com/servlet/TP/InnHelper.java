package com.servlet.TP;


import tp.TpExeception;
import tp.gestion.TpGestion;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.LinkedList;


/**
 * Classe utilitaire pour la gestion de l'application Inn.
 */
public class InnHelper {


    /**
     * Vérifie si les informations de connexion BD sont valides dans le contexte
     */
    public static boolean infoBDValide(ServletContext context)
    {
        return context.getAttribute("serveur") != null;
    }

    /**
     * Vérifie si on peut procéder (utilisateur connecté ET BD configurée)
     */
    public static boolean peutProceder(ServletContext context, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        if(infoBDValide(context))
        {
            HttpSession session = request.getSession(false);
            if (InnHelper.estConnecte(session))
            {
                return true;
            }
            DispatchToLogin(request, response);
            return false;
        }
        else
        {
            DispatchToBDConnect(request, response);
            return false;
        }
    }

    /**
     * Vérifie si on peut procéder au login (BD configurée)
     */
    public static boolean peutProcederLogin(ServletContext context, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        if(infoBDValide(context))
        {
            HttpSession session = request.getSession(false);
            if (session != null)
            {
                // Invalider la session existante pour un nouveau login
                session.invalidate();
            }
            return true;
        }
        else
        {
            DispatchToBDConnect(request, response);
            return false;
        }
    }

    /**
     * Redirige vers la servlet de login
     */
    public static void DispatchToLoginServlet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        HttpSession session = request.getSession(false);
        if (InnHelper.estConnecte(session))
        {
            session.invalidate();
        }
        // Afficher le menu de connexion principal de l'application
        RequestDispatcher dispatcher = request.getRequestDispatcher("/Login");
        dispatcher.forward(request, response);
    }

    /**
     * Redirige vers la page de login
     */
    public static void DispatchToLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        HttpSession session = request.getSession(false);
        if (InnHelper.estConnecte(session))
        {
            session.invalidate();
        }
        // Afficher le menu de connexion principal de l'application
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/login.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Redirige vers la page de configuration BD
     */
    public static void DispatchToBDConnect(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        HttpSession session = request.getSession(false);
        if (InnHelper.estConnecte(session))
        {
            session.invalidate();
        }
        // Afficher le menu de connexion principal de l'application
        RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Vérifie si l'utilisateur est connecté
     */
    public static boolean estConnecte(HttpSession session)
    {
        if(session == null)
            return false;
        return session.getAttribute("etat") != null;
    }

    /**
     *  Vérifie si les gestionnaires sont crées pour cette session
     */
    public static boolean gestionnairesCrees(HttpSession session)
    {
        if(session == null)
            return false;
        // Vérificaion (interrogation gestionnaires crées)
        return session.getAttribute("TpGestion") != null;
    }


    public static void creerGestionnaire(ServletContext c, HttpSession s) throws SQLException, TpExeception
    {
        String serveur = (String) c.getAttribute("serveur");
        String bd = (String) c.getAttribute("bd");
        String userIdBD = (String) c.getAttribute("user");
        String pass = (String) c.getAttribute("pass");

        TpGestion InInterrogation = new TpGestion(serveur, bd, userIdBD, pass);
        InInterrogation.getConnexion();
        s.setAttribute("TpGestion", InInterrogation);

        TpGestion innUpdate = new TpGestion(serveur, bd, userIdBD, pass);
        s.setAttribute("TpUpdate", innUpdate);
    }

    public static TpGestion getInnInterro(HttpSession session)
    {
        return (TpGestion)session.getAttribute("TpGestion");
    }

    public static TpGestion getInnUpdate(HttpSession session)
    {
        return (TpGestion)session.getAttribute("InnUpdate");
    }


    public static int ConvertirInt(String v, String nom) throws TpExeception
    {
        try
        {
            return Integer.parseInt(v);
        }
        catch(Exception e)
        {
            throw new TpExeception(nom + " ne doit etre compose que de chiffre.");
        }
    }

    public static long ConvertirLong(String v, String nom) throws TpExeception
    {
        try
        {
            return Long.parseLong(v);
        }
        catch(Exception e)
        {
            throw new TpExeception(nom + " ne doit etre compose que de chiffre.");
        }
    }
}
