package tp.gestion;

import tp.TpExeception;
import tp.collections.Reservations;
import tp.collections.Clients;
import tp.collections.Chambres;
import tp.objets.Client;
import tp.objets.Chambre;
import tp.objets.Reservation;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class GestionReservation {
    private final Reservations reservations;
    private final Clients clients;
    private final Chambres chambres;

    public GestionReservation(Reservations reservations, Clients clients, Chambres chambres) {
        this.reservations = reservations;
        this.clients = clients;
        this.chambres = chambres;
    }

    public void reserver(String prenomClient, String nomClient, String nomChambre, String dateDebut, String dateFin) throws TpExeception {
        if (prenomClient == null || nomClient == null || nomChambre == null || dateDebut == null || dateFin == null) {
            throw new TpExeception("Tous les paramètres (prénom, nom, chambre, dates) sont requis.");
        }

        LocalDate debut, fin;
        try {
            debut = LocalDate.parse(dateDebut);
            fin = LocalDate.parse(dateFin);
        } catch (DateTimeParseException e) {
            throw new TpExeception("Le format des dates doit être AAAA-MM-JJ.");
        }

        if (!debut.isBefore(fin)) {
            throw new TpExeception("La date de début doit être antérieure à la date de fin.");
        }

        if (debut.isBefore(LocalDate.now())) {
            throw new TpExeception("Il n'est pas possible de réserver pour une date passée.");
        }

        Client client = clients.GetClientByNomPrenom(nomClient, prenomClient);
        if (client == null) {
            throw new TpExeception("Le client '" + prenomClient + " " + nomClient + "' n'existe pas.");
        }

        Chambre chambre = chambres.getChambreByNom(nomChambre);
        if (chambre == null) {
            throw new TpExeception("La chambre '" + nomChambre + "' n'existe pas.");
        }

        List<Reservation> conflits = reservations.getReservationsPourChambreEntre(chambre.getIdChambre(), dateDebut, dateFin);
        if (!conflits.isEmpty()) {
            throw new TpExeception("La chambre '" + nomChambre + "' est déjà réservée pour cette période.");
        }

        int newId = reservations.getNextId();
        Reservation nouvelleReservation = new Reservation();
        nouvelleReservation.setIdReservation(newId);
        nouvelleReservation.setPrenomClient(prenomClient);
        nouvelleReservation.setNomClient(nomClient);
        nouvelleReservation.setIdChambre(chambre.getIdChambre());
        nouvelleReservation.setDateDebut(dateDebut);
        nouvelleReservation.setDateFin(dateFin);

        reservations.ajouterReservation(nouvelleReservation);
    }
}
