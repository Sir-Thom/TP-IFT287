package tp.gestion;

import tp.TpExeception;
import tp.bdd.Connexion;
import tp.collections.Chambres;
import tp.collections.Clients;
import tp.collections.Commodites;
import tp.collections.Reservations;

import java.sql.SQLException;

public class TpGestion {
    private Connexion cx;
    private Chambres chambres;
    private Clients clients;
    private Commodites commodites;
    private Reservations reservations;

    private GestionCommodite gestionCommodite;
    private GestionClient gestionClient;
    private GestionReservation gestionReservation;
    private GestionChambre gestionChambre;

    public TpGestion(String serveur, String bd, String user, String password)
            throws TpExeception, SQLException
    {
        // allocation des objets pour le traitement des transactions
        cx = new Connexion(serveur, bd, user, password);
        chambres = new Chambres(getConnexion());
        clients = new Clients(getConnexion());
        commodites = new Commodites(getConnexion());
        reservations = new Reservations(getConnexion(), chambres, clients);
        setGestionCommodite(new GestionCommodite(commodites, chambres));
        setGestionClient(new GestionClient(getConnexion(), clients, reservations));
        setGestionReservation(new GestionReservation(reservations, clients, chambres));
        setGestionChambre(new GestionChambre(chambres, reservations));




//        livre = new TableLivres(getConnexion());
//        membre = new TableMembres(getConnexion());
//        reservation = new TableReservations(getConnexion());
//        setGestionLivre(new GestionLivre(livre, reservation));
//        setGestionMembre(new GestionMembre(membre, reservation));
//        setGestionPret(new GestionPret(livre, membre, reservation));
//        setGestionReservation(new GestionReservation(livre, membre, reservation));
//        setGestionInterrogation(new GestionInterrogation(getConnexion()));
    }

    public void fermer() throws SQLException
    {
        // fermeture de la connexion
        getConnexion().fermer();
    }

    /**
     * @return the Connexion
     */
    public Connexion getConnexion()
    {
        return cx;
    }

    /**
     * @return the gestionCommodite
     */
    public GestionCommodite getGestionCommodite() {
        return gestionCommodite;
    }
    /**
     * @param gestionCommodite the gestionCommodite to set
     */
    public void setGestionCommodite(GestionCommodite gestionCommodite) {
        this.gestionCommodite = gestionCommodite;
    }

    /**
     * @return the gestionClient
     */
    public GestionClient getGestionClient() {
        return gestionClient;
    }
    /**
     * @param gestionClient the gestionClient to set
     */
    public void setGestionClient(GestionClient gestionClient) {
        this.gestionClient = gestionClient;
    }
    /**
     * @return the gestionReservation
     */
    public GestionReservation getGestionReservation() {
        return gestionReservation;
    }
    /**
     * @param gestionReservation the gestionReservation to set
     */
    public void setGestionReservation(GestionReservation gestionReservation) {
        this.gestionReservation = gestionReservation;
    }
    /**
     * @return the gestionChambre
     */
    public GestionChambre getGestionChambre() {
        return gestionChambre;
    }
    /**
     * @param gestionChambre the gestionChambre to set
     */
    public void setGestionChambre(GestionChambre gestionChambre) {
        this.gestionChambre = gestionChambre;
    }

}
