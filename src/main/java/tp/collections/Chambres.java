package tp.collections;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import tp.bdd.Connexion;
import tp.objets.Chambre;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class Chambres  extends GestionCollection {

    private final Connexion cx;
    private final MongoCollection<Document> collectionChambres;

    public Chambres(Connexion cx) {
        super(cx);
        this.cx = cx;
        this.collectionChambres = cx.getDatabase().getCollection("Chambres");
    }

    private int getNextId() {
        int maxId = 0;
        for (Document doc : collectionChambres.find()) {
            Integer id = doc.getInteger("idChambre");
            if (id != null && id > maxId) {
                maxId = id;
            }
        }
        return maxId + 1;
    }

    public boolean existe(String nomChambre) {
        return collectionChambres.find(eq("nomChambre", nomChambre)).first() != null;
    }

    public Chambre getChambreByNom(String nomChambre) {
        Document d = collectionChambres.find(eq("nomChambre", nomChambre)).first();
        if (d == null) return null;
        return new Chambre(d);
    }

    public Chambre getChambreById(int idChambre) {
        Document d = collectionChambres.find(eq("idChambre", idChambre)).first();
        if (d == null) return null;
        return new Chambre(d);
    }

    public void ajouterChambre(Chambre chambre) {
        int nextId = getNextId();
        chambre.setIdChambre(nextId);
        Document doc = new Document("idChambre", chambre.getIdChambre())
                .append("nomChambre", chambre.getNomChambre())
                .append("typeLit", chambre.getTypeLit())
                .append("prixBase", chambre.getPrixBase());
        collectionChambres.insertOne(doc);
    }

    public void modifierChambre(Chambre chambre) {
        Document filter = new Document("idChambre", chambre.getIdChambre());
        Document update = new Document("$set",
                new Document("nomChambre", chambre.getNomChambre())
                        .append("typeLit", chambre.getTypeLit())
                        .append("prixBase", chambre.getPrixBase()));
        collectionChambres.updateOne(filter, update);
    }

    public boolean supprimerChambre(String nomChambre) {
        // À adapter pour vérifier s’il y a des réservations futures avant suppression
        DeleteResult result = collectionChambres.deleteOne(eq("nomChambre", nomChambre));
        return result.getDeletedCount() > 0;
    }

    public List<Chambre> getToutesChambres() {
        List<Chambre> liste = new ArrayList<>();
        for (Document doc : collectionChambres.find()) {
            liste.add(new Chambre(doc));
        }
        return liste;
    }

}
