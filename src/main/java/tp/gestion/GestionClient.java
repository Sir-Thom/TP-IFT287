package tp.gestion;

import tp.TpExeception;
import tp.bdd.Connexion;
import tp.collections.Clients;
import tp.collections.Reservations;
import tp.objets.Chambre;
import tp.objets.Client;

public class GestionClient extends GestionTransactions {
    private final Clients clients;
    private final Reservations reservations;
    public GestionClient(Connexion cx) {
        super(cx);

        this.clients = new Clients(cx);
        this.reservations = new Reservations(cx, null, clients); // Passer null pour Chambres car non utilisé ici

    }
    //constructor pour injecter une instance de Clients pour les tests
    public GestionClient(Connexion cx, Clients clients,Reservations reservations) {
        super(cx);
        this.clients = clients;
        this.reservations = reservations;
    }

    public void ajouterClient(String nom, String prenom, int age) throws TpExeception {
        if(clients.existe(nom)) {
            throw new TpExeception("Le client avec le nom '" + nom + "' existe déjà.");
        }
        Client nouveauClient = new Client(0, nom, prenom, age);
        clients.ajouterClient(nouveauClient);
    }

    public Client afficherClients(String nom,String prenom) throws TpExeception {
        Client client = clients.GetClientByNomPrenom(nom, prenom);

        if (client == null) {
            throw new TpExeception("Le client '" + nom + " " + prenom + "' n'existe pas.");
        }
        System.out.println(client.toString());
        return client;
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
