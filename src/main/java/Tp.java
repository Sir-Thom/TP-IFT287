
import tp.TpExeception;
import tp.bdd.Connexion;
import tp.gestion.GestionChambre;
import tp.collections.Chambres;
import tp.collections.Reservations;
import tp.gestion.GestionClient;

import java.io.*;
import java.sql.SQLException;
import java.util.StringTokenizer;
public class Tp {
    private static Connexion cx;
    private static GestionChambre gestionChambre;
    private static GestionClient gestionClient;

    public static void main(String[] args) throws Exception {
        if (args.length < 4) {
            System.out.println("Usage: java MainGestionChambre <serveur> <bd> <user> <password> [<fichier-transactions>]");
            return;
        }

        cx = null;
        BufferedReader reader = null;

        try {
            cx = new Connexion(args[0], args[1], args[2], args[3]);
            Chambres chambres = new Chambres(cx);
            Reservations reservations = new Reservations(cx,chambres);
            gestionChambre = new GestionChambre(chambres, reservations);
            gestionClient = new GestionClient(cx);

            String nomFichier = null;
            if (args.length == 5) nomFichier = args[4];
            reader = ouvrirFichier(nomFichier);

            traiterTransactions(reader, nomFichier != null);

        } catch (Exception e) {
            e.printStackTrace(System.out);
        } finally {
            if (reader != null) reader.close();
            if (cx != null) cx.fermer();
        }
    }

    private static BufferedReader ouvrirFichier(String nomFichier) throws FileNotFoundException {
        if (nomFichier == null) {
            return new BufferedReader(new InputStreamReader(System.in));
        } else {
            return new BufferedReader(new InputStreamReader(new FileInputStream(nomFichier)));
        }
    }

    private static void traiterTransactions(BufferedReader reader, boolean echo) throws Exception {
        afficherAide();
        String transaction = lireTransaction(reader, echo);

        while (!finTransaction(transaction)) {
            StringTokenizer tokenizer = new StringTokenizer(transaction, " ");
            if (tokenizer.hasMoreTokens()) {
                executerTransaction(tokenizer);
            }
            transaction = lireTransaction(reader, echo);
        }
    }

    private static String lireTransaction(BufferedReader reader, boolean echo) throws IOException {
        System.out.print("> ");
        String transaction = reader.readLine();
        if (echo) System.out.println(transaction);
        return transaction;
    }

    private static boolean finTransaction(String transaction) {
        if (transaction == null) return true;
        StringTokenizer tokenizer = new StringTokenizer(transaction, " ");
        if (!tokenizer.hasMoreTokens()) return false;
        return tokenizer.nextToken().equals("exit");
    }

    private static void executerTransaction(StringTokenizer tokenizer) throws Exception {
        try {
            String command = tokenizer.nextToken();

            if (command.equals("aide")) {
                afficherAide();
            } else if (command.equals("ajouterChambre")) {
                String nom = readString(tokenizer);
                String typeLit = readString(tokenizer);
                double prixBase = readDouble(tokenizer);
                gestionChambre.ajouterChambre(nom, typeLit, prixBase);

            } else if (command.equals("modifierChambre")) {
                String nomActuel = readString(tokenizer);
                String nouveauNom = readString(tokenizer);
                String typeLit = readString(tokenizer);
                double prixBase = readDouble(tokenizer);
                gestionChambre.modifierChambre(nomActuel, nouveauNom, typeLit, prixBase);

            } else if (command.equals("supprimerChambre")) {
                String nom = readString(tokenizer);
                gestionChambre.supprimerChambre(nom);

            } else if (command.equals("afficherChambre")) {
                String nom = readString(tokenizer);
                gestionChambre.afficherChambre(nom);

            } else if (command.equals("afficherChambresLibres")) {
                String dateDebut = readString(tokenizer);
                String dateFin = readString(tokenizer);
                gestionChambre.afficherChambresLibres(dateDebut, dateFin);

            } else if (command.equals("ajouterClient")) {
                String nom = readString(tokenizer);
                String prenom = readString(tokenizer);
                int age = readInt(tokenizer);
                gestionClient.ajouterClient(nom, prenom, age);
            } else if (command.equals("afficherClient")) {
                String prenom = readString(tokenizer);
                String nom = readString(tokenizer);
                gestionClient.afficherClients(nom, prenom);
                
            } else if (command.equals("exit")) {
                System.out.println("Fin du programme.");

            } else if (command.equals("--")) {
                // commentaire
            } else {
                System.out.println("Transaction inconnue. Essayez \"aide\".");
            }
        } catch (TpExeception | SQLException e) {
            System.out.println("** " + e.getMessage());
        }
    }

    private static void afficherAide() {
        System.out.println();
        System.out.println("Transactions disponibles :");
        System.out.println("  aide");
        System.out.println("  exit");
        System.out.println("  ajouterChambre <nom> <typeLit> <prixBase>");
        System.out.println("  modifierChambre <nomActuel> <nouveauNom> <typeLit> <prixBase>");
        System.out.println("  supprimerChambre <nom>");
        System.out.println("  afficherChambre <nom>");
        System.out.println("  afficherChambresLibres <dateDebut> <dateFin>");
        System.out.println("  ajouterClient <nom> <prenom> <age>");
        System.out.println("  afficherClient <nom> <prenom>");
    }

    private static String readString(StringTokenizer tokenizer) throws TpExeception {
        if (tokenizer.hasMoreElements()) {
            return tokenizer.nextToken();
        } else {
            throw new TpExeception("Paramètre manquant");
        }
    }

    private static double readDouble(StringTokenizer tokenizer) throws TpExeception {
        if (tokenizer.hasMoreElements()) {
            String token = tokenizer.nextToken();
            try {
                return Double.parseDouble(token);
            } catch (NumberFormatException e) {
                throw new TpExeception("Nombre attendu à la place de \"" + token + "\"");
            }
        } else {
            throw new TpExeception("Paramètre manquant");
        }
    }

    private  static  int readInt(StringTokenizer tokenizer) throws TpExeception {
        if (tokenizer.hasMoreElements()) {
            String token = tokenizer.nextToken();
            try {
                return Integer.parseInt(token);
            } catch (NumberFormatException e) {
                throw new TpExeception("Nombre entier attendu à la place de \"" + token + "\"");
            }
        } else {
            throw new TpExeception("Paramètre manquant");
        }
    }
}
