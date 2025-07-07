package tp.collections;

import org.bson.Document;
import tp.bdd.Connexion;
import tp.objets.Chambre;
import tp.objets.Reservation;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import static com.mongodb.client.model.Filters.*;

public class Reservations extends GestionCollection {
    private final MongoCollection<Document> collectionReservations;
    private final Chambres chambres;

    public Reservations(Connexion cx, Chambres chambres) {
        super(cx);
        this.collectionReservations = cx.getDatabase().getCollection("Reservations");
        this.chambres = chambres;
    }

    // Obtenir le prochain ID de réservation
    public int getNextId() {
        int maxId = 0;
        for (Document d : collectionReservations.find()) {
            Integer id = d.getInteger("idReservation");
            if (id != null && id > maxId) {
                maxId = id;
            }
        }
        return maxId + 1;
    }

    //  Ajouter une réservation
    public void ajouterReservation(Reservation r) {
        collectionReservations.insertOne(r.toDocument());
    }

    // ❌ Réservations futures pour une chambre
    public List<Reservation> getReservationsFuturesPourChambre(int idChambre) {
        String today = LocalDate.now().toString();
        List<Reservation> resultats = new ArrayList<>();
        MongoCursor<Document> cursor = collectionReservations.find(
                and(
                        eq("idChambre", idChambre),
                        gte("dateFin", today)
                )
        ).iterator();

        while (cursor.hasNext()) {
            resultats.add(new Reservation(cursor.next()));
        }
        return resultats;
    }

    //  Conflits de réservation pour une chambre entre deux dates
    public List<Reservation> getReservationsPourChambreEntre(int idChambre, String dateDebut, String dateFin) {
        List<Reservation> resultats = new ArrayList<>();
        MongoCursor<Document> cursor = collectionReservations.find(
                and(
                        eq("idChambre", idChambre),
                        lt("dateDebut", dateFin),
                        gt("dateFin", dateDebut)
                )
        ).iterator();

        while (cursor.hasNext()) {
            resultats.add(new Reservation(cursor.next()));
        }
        return resultats;
    }

    // 📅 Liste des chambres réservées pendant une période
    public List<Integer> getIdChambresReserveesEntre(String dateDebut, String dateFin) {
        List<Integer> ids = new ArrayList<>();
        MongoCursor<Document> cursor = collectionReservations.find(
                and(
                        lt("dateDebut", dateFin),
                        gt("dateFin", dateDebut)
                )
        ).iterator();

        while (cursor.hasNext()) {
            Document d = cursor.next();
            ids.add(d.getInteger("idChambre"));
        }
        return ids;
    }

    //  Toutes les réservations d’un client
  /*  public List<Reservation> getReservationsClient(String prenom, String nom) {
       return  Chambres
    }*/

    // Réservations en cours pour un client (date système)
    public boolean clientAReservationEnCours(String prenom, String nom) {
        String today = LocalDate.now().toString();
        return collectionReservations.find(
                and(
                        eq("prenomClient", prenom),
                        eq("nomClient", nom),
                        lte("dateDebut", today),
                        gt("dateFin", today)
                )
        ).first() != null;
    }

    public List<Chambre> getChambresLibres(String dateDebut, String dateFin) {
        List<Chambre> toutes = chambres.getToutesChambres();
        List<Integer> chambresOccupees = getIdChambresReserveesEntre(dateDebut, dateFin);

        List<Chambre> disponibles = new ArrayList<>();
        for (Chambre c : toutes) {
            if (!chambresOccupees.contains(c.getIdChambre())) {
                disponibles.add(c);
            }
        }
        return disponibles;
    }


}
