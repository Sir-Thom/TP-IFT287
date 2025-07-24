package tp.bdd;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Gestionnaire d'une connexion avec une BD NoSQL via MongoDB.
 */
public class Connexion {
    private MongoClient client;
    private MongoDatabase database;

    /**
     * Ouverture d'une connexion
     *
     * @param serveur serveur à utiliser (local ou dinf)
     * @param bd nom de la base de données
     * @param user userid sur le serveur MongoDB pour la BD spécifiée
     * @param pass mot de passe sur le serveur MongoDB pour la BD spécifiée
     */
    public Connexion(String serveur, String bd, String user, String pass) {
        if (serveur.equals("local")) {
            client = MongoClients.create("mongodb://localhost:27017");
        } else if (serveur.equals("dinf")) {
            String uri = String.format(
                    "mongodb://%s:%s@bd-info2.dinf.usherbrooke.ca:27017/%s?ssl=true",
                    user, pass, bd
            );
            client = MongoClients.create(uri);
        }

        database = client.getDatabase(bd);

        System.out.println("Ouverture de la connexion :\n"
                + "Connecté sur la BD MongoDB "
                + bd + " avec l'utilisateur " + user);
    }

    /**
     * fermeture d'une connexion
     */
    public void fermer() {
        client.close();
        System.out.println("Connexion fermée");
    }

    /**
     * retourne la Connection MongoDB
     */
    public MongoClient getConnection() {
        return client;
    }

    /**
     * retourne la DataBase MongoDB
     */
    public MongoDatabase getDatabase() {
        return database;
    }

    public void setIsolationReadCommited() throws SQLException
    {
        client.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
    }
}
