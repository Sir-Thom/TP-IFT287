package tp.objets;

import org.bson.Document;

public class Reservation {
    private int idReservation;
    private int idChambre;
    private String prenomClient;
    private String nomClient;
    private String dateDebut;  // au format ISO-8601 : "2025-07-04"
    private String dateFin;  // au format ISO-8601 : "2025-07-10"

    public Reservation() {
        // Constructeur vide requis
    }

    public Reservation(Document d) {
        this.idReservation = d.getInteger("idReservation");
        this.idChambre = d.getInteger("idChambre");
        this.prenomClient = d.getString("prenomClient");
        this.nomClient = d.getString("nomClient");
        this.dateDebut = d.getString("dateDebut");
        this.dateFin = d.getString("dateFin");
    }

    public Reservation(int idReservation, int idChambre, String prenomClient, String nomClient, String dateDebut, String dateFin) {
        this.idReservation = idReservation;
        this.idChambre = idChambre;
        this.prenomClient = prenomClient;
        this.nomClient = nomClient;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    // Getters et Setters
    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public int getIdChambre() {
        return idChambre;
    }

    public void setIdChambre(int idChambre) {
        this.idChambre = idChambre;
    }

    public String getPrenomClient() {
        return prenomClient;
    }

    public void setPrenomClient(String prenomClient) {
        this.prenomClient = prenomClient;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public Document toDocument() {
        Document doc = new Document();
        doc.append("idReservation", idReservation);
        doc.append("idChambre", idChambre);
        doc.append("prenomClient", prenomClient);
        doc.append("nomClient", nomClient);
        doc.append("dateDebut", dateDebut);
        doc.append("dateFin", dateFin);
        return doc;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "idReservation=" + idReservation +
                ", idChambre=" + idChambre +
                ", client='" + prenomClient + " " + nomClient + '\'' +
                ", dateDebut='" + dateDebut + '\'' +
                ", dateFin='" + dateFin + '\'' +
                '}';
    }
}

