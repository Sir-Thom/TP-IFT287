package com.servlet.TP;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Map;

public class TpContextListener implements ServletContextListener{


    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Contexte du TP WEB démarré : " + sce.getServletContext().getServletContextName());

        String envPath = sce.getServletContext().getRealPath("/") + ".env";
        Map<String, String> env = EnvLoader.loadEnv(envPath);

        sce.getServletContext().setAttribute("serveur", env.getOrDefault("SERVEUR", ""));
        sce.getServletContext().setAttribute("bd", env.getOrDefault("BD", ""));
        sce.getServletContext().setAttribute("user", env.getOrDefault("USER", ""));
        sce.getServletContext().setAttribute("pass", env.getOrDefault("PASS", ""));




        System.out.println("Connexion BD automatique configurée via .env.");
    }





    public void contextDestroyed(ServletContextEvent sce)
        {
            System.out.println("Le contexte de l'application GestionTP vient d'être détruit.");
        }
    }


