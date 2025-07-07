package tp.objets;

import org.bson.Document;

public class Chambre {
    private int idChambre;
    private String nomChambre;
    private String typeLit;
    private double prixBase;

    public Chambre() {
        // Constructeur vide
    }

    public Chambre(Document d) {
        idChambre = d.getInteger("idChambre");
        nomChambre = d.getString("nomChambre");
        typeLit = d.getString("typeLit");
        prixBase = d.getDouble("prixBase");
    }

    public Chambre(int idChambre, String nomChambre, String typeLit, double prixBase) {
        this.idChambre = idChambre;
        this.nomChambre = nomChambre;
        this.typeLit = typeLit;
        this.prixBase = prixBase;
    }

    public int getIdChambre() {
        return idChambre;
    }
    public void setIdChambre(int idChambre) {
        this.idChambre = idChambre;
    }
    public String getNomChambre() {
        return nomChambre;
    }
    public void setNomChambre(String nomChambre) {
        this.nomChambre = nomChambre;
    }
    public String getTypeLit() {
        return typeLit;
    }
    public void setTypeLit(String typeLit) {
        this.typeLit = typeLit;
    }
    public double getPrixBase() {
        return prixBase;
    }
    public void setPrixBase(double prixBase) {
        this.prixBase = prixBase;
    }
    @Override
    public String toString() {
        return "Chambre{" +
                "idChambre=" + idChambre +
                ", nomChambre='" + nomChambre + '\'' +
                ", typeLit='" + typeLit + '\'' +
                ", prixBase=" + prixBase +
                '}';
    }

}
