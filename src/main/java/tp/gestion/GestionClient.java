package tp.gestion;

import tp.TpExeception;
import tp.bdd.Connexion;
import tp.collections.Clients;
import tp.collections.Reservations;
import tp.objets.Chambre;
import tp.objets.Client;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GestionClient extends GestionTransactions {
    private final Clients clients;
    private final Reservations reservations;
    public GestionClient(Connexion cx) {
        super(cx);

        this.clients = new Clients(cx);
        this.reservations = new Reservations(cx, null, clients); // Passer null pour Chambres car non utilisé ici

    }
    //constructor pour injecter une instance de Clients pour les tests..
    public GestionClient(Connexion cx, Clients clients,Reservations reservations) {
        super(cx);
        this.clients = clients;
        this.reservations = reservations;
    }


    public void ajouterClient(String nom, String prenom, int age) throws TpExeception {
        try {
            Client clientExistant = clients.GetClientByNomPrenom(nom, prenom);
            if (clientExistant != null) {
                throw new TpExeception("Le client '" + prenom + " " + nom + "' existe déjà.");
            }
            Client nouveauClient = new Client(0, nom, prenom, age);
            clients.ajouterClient(nouveauClient); // Ajout.
        } catch (TpExeception e) {
            // Si le client n'existe pas, c'est ce qu'on veut -> on continue
            if (!e.getMessage().contains("n'existe pas")) {
                throw e;
            }
        }


    }

    public Client afficherClients(String nom,String prenom) throws TpExeception {
        Client client = clients.GetClientByNomPrenom(nom, prenom);

        if (client == null) {
            throw new TpExeception("Le client '" + nom + " " + prenom + "' n'existe pas.");
        }
        System.out.println(client.toString());
        return client;
    }

    /**
     *
     * extra vérification lorsqu'il y a pas de client !
     */

    public List<Client> getListClients()  {
            return clients.getClients();

    }


    public void supprimerClient(String prenom, String nom) throws TpExeception {
        Client client = clients.GetClientByNomPrenom(prenom, nom);

        if (client == null) {
            throw new TpExeception("Client introuvable");
        }

        boolean aDesResa = reservations.clientADesReservations(client.getIdClient());
        if (aDesResa) {
            throw new TpExeception("Impossible de supprimer : le client a des réservations existantes.");
        }

        clients.supprimerClient(prenom, nom);
        System.out.println("Client supprimé : " + prenom + " " + nom);
    }
}
