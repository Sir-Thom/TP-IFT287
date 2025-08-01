package tp.collections;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import tp.TpExeception;
import tp.bdd.Connexion;
import tp.objets.Client;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class Clients extends GestionCollection{
    private final Connexion cx;
    private final MongoCollection<Document> collectionClients;
    Reservations reservations;
    public Clients(Connexion cx) {
        super(cx);
        this.cx = cx;
        this.collectionClients = cx.getDatabase().getCollection("Clients");

        this.reservations = new Reservations(cx, new Chambres(cx), this);
    }

    private int getNextId() {
        int maxId = 0;
        for (Document doc : collectionClients.find()) {
            Integer id = doc.getInteger("idClient");
            if (id != null && id > maxId) {
                maxId = id;
            }
        }
        return maxId + 1;
    }

    public boolean existe(String nomClient) throws TpExeception {
        try{
            if (nomClient == null || nomClient.isEmpty()) {
                throw new TpExeception("Le nom du client ne peut pas être vide.");
            }

            return collectionClients.find(new Document("nom", nomClient)).first() != null;
        } catch (TpExeception e) {
            throw new TpExeception("Erreur lors de la vérification de l'existence du client : " + e.getMessage());
        }
    }
    public Client GetClientByNomPrenom(String nom, String prenom) throws TpExeception {
        try {


            if (nom == null || prenom == null || nom.isEmpty() || prenom.isEmpty()) {
                throw new TpExeception("Le nom et le prénom du client ne peuvent pas être vides.");
            }

            Document query = new Document("nom", nom)
                    .append("prenom", prenom);


            Document clientDoc = collectionClients.find(query).first();
            if (clientDoc == null) {
                return null;
            }
            return new Client(clientDoc);

        } catch (Exception e) {
            System.err.println("FULL ERROR: " + e);
            throw new TpExeception("Erreur lors de la récupération du client : " + e.getMessage());
        }
    }


    public Client getClientById(int idClient) throws TpExeception {
        try {
            Document clientDoc = collectionClients.find(new Document("idClient", idClient)).first();
            if (clientDoc == null) {
                throw new TpExeception("Le client avec l'ID '" + idClient + "' n'existe pas.");
            }
            return new Client(clientDoc);
        } catch (TpExeception e) {
            throw new TpExeception("Erreur lors de la récupération du client : " + e.getMessage());
        }
    }

    public  void ajouterClient(Client client) throws TpExeception {
        try {
            if (client == null) {
                throw new TpExeception("Le client ne peut pas être null.");
            }
            if (client.getNom()  == null && client.getPrenom() == null || client.getNom().isEmpty() || client.getPrenom().isEmpty()) {
                throw new TpExeception("Le nom du client ne peut pas être vide.");
            }

            if (client.getAge() <= 0) {
                throw new TpExeception("L'âge du client doit être supérieur à 0.");
            }

            int nextId = getNextId();
            client.setId(nextId);
            Document doc = new Document("idClient", client.getIdClient())
                    .append("nom", client.getNom())
                    .append("prenom", client.getPrenom())
                    .append("age", client.getAge());
            collectionClients.insertOne(doc);

        } catch (TpExeception e) {
            throw new TpExeception("Erreur lors de l'ajout du client : " + e.getMessage());
        }
    }

    public List<Client> getClients() {
        List<Client> liste = new ArrayList<>();

            if (collectionClients != null) {
                for (Document doc : collectionClients.find()) {
                    if (doc != null) {
                        try {
                            Client client = new Client(doc);
                            liste.add(client);
                            System.out.println(doc.toJson());
                        } catch (Exception e) {
                            System.err.println("Erreur lors de la création du client à partir du document: " + e.getMessage());
                        }
                    }
                }
            } else {
                System.err.println("La collection clients n'est pas initialisée");
            }

        return liste;
    }

    public boolean supprimerClient(String prenom,String nom) throws TpExeception {
        try {
            if (nom == null || nom.isEmpty() || prenom == null || prenom.isEmpty()) {
                throw new TpExeception("Le prénom et le nom du client ne peuvent pas être vides.");
            }
            System.out.println("Suppression du client : " + prenom + " " + nom);
            Client clientASupprimer = GetClientByNomPrenom(nom, prenom);
            if (clientASupprimer == null) {
                throw new TpExeception("Le client '" + prenom + " " + nom + "' n'existe pas.");
            }

            if (reservations.clientADesReservations(clientASupprimer.getIdClient())) {
                throw new TpExeception("Le client '" + prenom + " " + nom + "' a des réservations et ne peut pas être supprimé.");
            }

            DeleteResult result = collectionClients.deleteOne(eq("idClient", clientASupprimer.getIdClient()));

            if (result.getDeletedCount() == 0) {
                throw new TpExeception("La suppression a échoué, le client n'a pas été trouvé pour la suppression.");
            }
            return result.getDeletedCount() > 0;

        } catch (TpExeception e) {
            throw e;
        } catch (Exception e) {
            throw new TpExeception("Erreur lors de la suppression du client : " + e.getMessage());
        }

    }

    public List<Client> getAllClients() {
        List<Client> liste = new ArrayList<>();
        for (Document doc : collectionClients.find()) {
            liste.add(new Client(doc));
        }
        return liste;
    }
}
