package tp.gestion;

import tp.TpExeception;
import tp.bdd.Connexion;
import tp.collections.Clients;
import tp.objets.Chambre;
import tp.objets.Client;

public class GestionClient extends GestionTransactions {
    private final Clients clients;
    public GestionClient(Connexion cx) {
        super(cx);
        this.clients = new Clients(cx);
    }
    //constructor pour injecter une instance de Clients pour les tests
    public GestionClient(Connexion cx, Clients clients) {
        super(cx);
        this.clients = clients;
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

    public void supprimerClient(String prenom,String nom) throws TpExeception {
        Client client = clients.GetClientByNomPrenom(prenom, nom);
        if (client == null) {
            throw new TpExeception("Le client '" + prenom + " " + nom + "' n'existe pas.");
        }

        clients.supprimerClient(prenom, nom);

    }


}
