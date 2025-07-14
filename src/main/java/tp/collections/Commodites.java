package tp.collections;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import tp.bdd.Connexion;
import tp.objets.Commodite;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class Commodites {

    private final MongoCollection<Document> collection;

    public Commodites(Connexion cx) {
        this.collection = cx.getDatabase().getCollection("Commodite");
    }

    /**
     * Insère une nouvelle commodité dans la base
     */
    public void inserer(Commodite commodite) {
        Document doc = new Document("idCommodite", commodite.getIdCommodite())
                .append("description", commodite.getDescription())
                .append("surplusPrix", commodite.getSurplusPrix());
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
     * Retourne toutes les commodités
     */
    public List<Commodite> obtenirToutesLesCommodites() {
        List<Commodite> liste = new ArrayList<>();
        for (Document doc : collection.find()) {
            liste.add(new Commodite(
                    doc.getInteger("idCommodite"),
                    doc.getString("description"),
                    doc.getDouble("surplusPrix")
            ));
        }
        return liste;
    }

    /**
     * Calcule l’ID suivant pour les commodités (manuellement, comme dans SQL auto_increment)
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
}
