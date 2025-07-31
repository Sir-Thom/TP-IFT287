package tp.gestion;

import tp.TpExeception;
import tp.collections.Chambres;
import tp.collections.Reservations;
import tp.objets.Chambre;
import tp.objets.Reservation;

import java.sql.SQLException;
import java.util.List;

public class GestionChambre extends GestionTransactions{
    private final Chambres chambres;
    private final Reservations reservations;

    public GestionChambre(Chambres chambres, Reservations reservations) {
        super(chambres.getConnexion());
        this.chambres = chambres;
        this.reservations = reservations;
    }
    public void ajouterChambre(String nom, String typeLit, double prixBase) throws TpExeception {
        if (chambres.existe(nom)) {
            throw new TpExeception("Une chambre avec le nom '" + nom + "' existe déjà.");
        }
        Chambre nouvelleChambre = new Chambre(0, nom, typeLit, prixBase); // id auto-généré
        chambres.ajouterChambre(nouvelleChambre);
        System.out.println("Chambre '" + nom + "' ajoutée avec succès.");
    }

    public void modifierChambre(String nomActuel, String nouveauNom, String typeLit, double prixBase) throws TpExeception {
        Chambre chambre = chambres.getChambreByNom(nomActuel);
        if (chambre == null) {
            throw new TpExeception("La chambre '" + nomActuel + "' n'existe pas.");
        }

        if (!nomActuel.equals(nouveauNom) && chambres.existe(nouveauNom)) {
            throw new TpExeception("Une autre chambre avec le nom '" + nouveauNom + "' existe déjà.");
        }

        chambre.setNomChambre(nouveauNom);
        chambre.setTypeLit(typeLit);
        chambre.setPrixBase(prixBase);
        chambres.modifierChambre(chambre);
    }

    public void supprimerChambre(String nom) throws SQLException, TpExeception {
        Chambre chambre = chambres.getChambreByNom(nom);
        if (chambre == null) {
            throw new TpExeception("La chambre '" + nom + "' n'existe pas.");
        }

        List<Reservation> futures = reservations.getReservationsFuturesPourChambre(chambre.getIdChambre());
        if (!futures.isEmpty()) {
            throw new TpExeception("Impossible de supprimer la chambre '" + nom + "' car elle a des réservations futures.");
        }

        if (!chambres.supprimerChambre(nom)) {
            throw new TpExeception("La suppression de la chambre '" + nom + "' a échoué.");
        }
    }

    public Chambre afficherChambre(String nom) throws TpExeception {
        Chambre chambre = chambres.getChambreByNom(nom);
        if (chambre == null) {
            throw new TpExeception("La chambre '" + nom + "' n'existe pas.");
        }

        System.out.println(chambre.toString());
        return chambre;
    }

    public List<Chambre> afficherChambresLibres(String dateDebut, String dateFin) throws TpExeception {
        List<Chambre> libres = reservations.getChambresLibres(dateDebut, dateFin);
        if (libres.isEmpty()) {
            System.out.println("Aucune chambre libre entre " + dateDebut + " et " + dateFin);
        } else {
            System.out.println("Chambres libres entre " + dateDebut + " et " + dateFin + " :");
            for (Chambre c : libres) {
                System.out.println("- " + c.getNomChambre() + " (lit " + c.getTypeLit() + ", " +
                        "prix : " + c.getPrixBase() + " $)");
            }
        }
        return libres;
    }

    public List<Chambre> getAllChambres() {
        return chambres.getToutesChambres();
    }

}
