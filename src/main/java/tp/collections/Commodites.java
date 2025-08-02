package tp.collections;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import tp.TpExeception;
import tp.bdd.Connexion;
import tp.objets.Commodite;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class Commodites extends GestionCollection {

    private final Connexion cx;
    private final MongoCollection<Document> collection;

    public Commodites(Connexion cx) {
        super(cx);
        this.cx = cx;
        this.collection = cx.getDatabase().getCollection("Commodite");
    }

    /**
     * Insère une nouvelle commodité dans la base
     */
    public void inserer(Commodite commodite) {
        Document doc = commodite.toDocument();
        collection.insertOne(doc);
    }

    /**
     * Vérifie si une commodité existe par ID
     */
    public boolean existe(int idCommodite) {
        return collection.find(eq("idCommodite", idCommodite)).first() != null;
    }

    /**
     * Retourne une commodité par ID
     */
    public Commodite obtenirCommodite(int idCommodite) {
        Document doc = collection.find(eq("idCommodite", idCommodite)).first();
        if (doc == null) return null;

        return new Commodite(
                doc.getInteger("idCommodite"),
                doc.getString("description"),
                doc.getDouble("surplusPrix")
        );
    }

    /**
     * Retourne une commodité par ID avec exception si non trouvée
     */
    public Commodite obtenirCommoditeAvecException(int idCommodite) throws TpExeception {
        Document doc = collection.find(eq("idCommodite", idCommodite)).first();
        if (doc == null) {
            throw new TpExeception("Commodité avec l'ID " + idCommodite + " n'existe pas.");
        }

        return new Commodite(doc);
    }

    /**
     *  Supprime une commodité du système
     */
    public boolean supprimerCommodite(int idCommodite) throws TpExeception {
        if (!existe(idCommodite)) {
            throw new TpExeception("La commodité avec l'ID " + idCommodite + " n'existe pas.");
        }

        DeleteResult result = collection.deleteOne(eq("idCommodite", idCommodite));
        return result.getDeletedCount() > 0;
    }

    /**
     * Retourne toutes les commodités
     */
    public List<Commodite> obtenirToutesLesCommodites() {
        List<Commodite> liste = new ArrayList<>();
        for (Document doc : collection.find()) {
            try {
                liste.add(new Commodite(doc));
            } catch (Exception e) {
                System.err.println("Erreur lors de la lecture d'une commodité: " + e.getMessage());
                // Continuer avec les autres commodités
            }
        }
        return liste;
    }

    /**
     *  Retourne toutes les commodités avec leur ID et description pour affichage
     */
    public List<String> obtenirListeDescriptions() {
        List<String> descriptions = new ArrayList<>();
        for (Document doc : collection.find()) {
            try {
                int id = doc.getInteger("idCommodite");
                String description = doc.getString("description");
                double surplus = doc.getDouble("surplusPrix");
                descriptions.add(String.format("ID %d - %s (+$%.2f)", id, description, surplus));
            } catch (Exception e) {
                System.err.println("Erreur lors de la lecture d'une commodité: " + e.getMessage());
            }
        }
        return descriptions;
    }

    /**
     * Calcule l'ID suivant pour les commodités
     */
    public int getNextId() {
        int maxId = 0;
        for (Document doc : collection.find()) {
            Integer id = doc.getInteger("idCommodite");
            if (id != null && id > maxId) {
                maxId = id;
            }
        }
        return maxId + 1;
    }

    /**
     *  Compte le nombre total de commodités
     */
    public long compterCommodites() {
        return collection.countDocuments();
    }

    /**
     * Modifie une commodité existante
     */
    public void modifierCommodite(Commodite commodite) throws TpExeception {
        if (!existe(commodite.getIdCommodite())) {
            throw new TpExeception("La commodité avec l'ID " + commodite.getIdCommodite() + " n'existe pas.");
        }

        Document filter = new Document("idCommodite", commodite.getIdCommodite());
        Document update = new Document("$set",
                new Document("description", commodite.getDescription())
                        .append("surplusPrix", commodite.getSurplusPrix()));

        collection.updateOne(filter, update);
    }
}