package tp.collections;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import tp.bdd.Connexion;
import tp.gestion.GestionCommodite;
import tp.objets.Commodite;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class Commodites extends GestionCommodite {

    private MongoCollection<Document> collectionCommodite;
    private MongoCollection<Document> collectionChambres;
    private Connexion cx;

    public Commodites(Connexion cx) {
        super(cx);
        this.cx = cx;
        this.collectionCommodite = cx.getDatabase().getCollection("Commodite");
        this.collectionChambres = cx.getDatabase().getCollection("Chambres");
    }

    private int getNextId() {
        int maxId = 0;
        for (Document doc : collectionCommodite.find()) {
            Integer id = doc.getInteger("idCommodite");
            if (id != null && id > maxId) {
                maxId = id;
            }
        }
        return maxId + 1;
    }

    // Methode qui ajoute n nouveau service offert par l'entreprise
    public void ajouterCommodite(int idCommodite, String description, Double surplusPrix) {
        int nextId = getNextId();
        Commodite commodite = new Commodite(nextId, description, surplusPrix);

        Document doc = new Document("idCommodite", commodite.getIdCommodite())
                .append("description", commodite.getDescription())
                .append("surplusPrix", commodite.getSurplusPrix());
        collectionCommodite.insertOne(doc);
    }

    // Methode qui ajoute une commodité à un chambre
    public void inclureCommodite(String nomChambre, int idCommodite) {
        // Vérifier que la chambre existe
        Document chambre = collectionChambres.find(eq("nomChambre", nomChambre)).first();
        if (chambre == null) {
            System.err.println("Chambre introuvable : " + nomChambre);
            return;
        }

        // Vérifier que la commodité existe
        Document commodite = collectionCommodite.find(eq("idCommodite", idCommodite)).first();
        if (commodite == null) {
            System.err.println("Commodité introuvable : " + idCommodite);
            return;
        }

        // Vérifier que la commodité n'est pas déjà dans la chambre
        @SuppressWarnings("unchecked")
        List<Integer> commodites = (List<Integer>) chambre.get("commodites");
        if (commodites != null && commodites.contains(idCommodite)) {
            System.err.println("Commodité déjà associée à cette chambre");
            return;
        }

        // Ajouter la commodité à la chambre (utiliser $push pour ajouter à l'array)
        Document updateQuery = new Document("$push", new Document("commodites", idCommodite));
        UpdateResult result = collectionChambres.updateOne(eq("nomChambre", nomChambre), updateQuery);

        if (result.getModifiedCount() > 0) {
            System.out.println("Commodité " + idCommodite + " ajoutée à la chambre " + nomChambre);
        }
    }

    // Méthode qui enlève une commodité d'une chambre
    public void enleverCommodite(String nomChambre, int idCommodite) {
        // Vérifier que la chambre existe
        Document chambre = collectionChambres.find(eq("nomChambre", nomChambre)).first();
        if (chambre == null) {
            System.err.println("Chambre introuvable : " + nomChambre);
            return;
        }

        // Vérifier que la commodité est bien dans la chambre
        @SuppressWarnings("unchecked")
        List<Integer> commodites = (List<Integer>) chambre.get("commodites");
        if (commodites == null || !commodites.contains(idCommodite)) {
            System.err.println("Commodité non associée à cette chambre");
            return;
        }

        // Retirer la commodité de la chambre (utiliser $pull pour retirer de l'array)
        Document updateQuery = new Document("$pull", new Document("commodites", idCommodite));
        UpdateResult result = collectionChambres.updateOne(eq("nomChambre", nomChambre), updateQuery);

        if (result.getModifiedCount() > 0) {
            System.out.println("Commodité " + idCommodite + " retirée de la chambre " + nomChambre);
        }
    }

    public boolean existe(int idCommodite) {
        return collectionCommodite.find(eq("idCommodite", idCommodite)).first() != null;
    }

    // Méthode (pratique) pour récupérer une commodité par son ID
    public Commodite obtenirCommodite(int idCommodite) {
        Document doc = collectionCommodite.find(eq("idCommodite", idCommodite)).first();
        if (doc == null) {
            return null;
        }

        return new Commodite(
                doc.getInteger("idCommodite"),
                doc.getString("description"),
                doc.getDouble("surplusPrix")
        );
    }

    // Méthode pour obtenir toutes les commodités
    public List<Commodite> obtenirToutesLesCommodites() {
        List<Commodite> commodites = new ArrayList<>();
        for (Document doc : collectionCommodite.find()) {
            commodites.add(new Commodite(
                    doc.getInteger("idCommodite"),
                    doc.getString("description"),
                    doc.getDouble("surplusPrix")
            ));
        }
        return commodites;
    }

    // Méthode pour obtenir les commodités d'une chambre spécifique
    public List<Commodite> obtenirCommoditesChambre(String nomChambre) {
        List<Commodite> commoditesChambre = new ArrayList<>();

        Document chambre = collectionChambres.find(eq("nomChambre", nomChambre)).first();
        if (chambre == null) {
            return commoditesChambre; // Retourne une liste vide.
        }

        @SuppressWarnings("unchecked")
        List<Integer> idsCommodites = (List<Integer>) chambre.get("commodites");
        if (idsCommodites != null) {
            for (Integer idCommodite : idsCommodites) {
                Commodite commodite = obtenirCommodite(idCommodite);
                if (commodite != null) {
                    commoditesChambre.add(commodite);
                }
            }
        }

        return commoditesChambre;
    }
}