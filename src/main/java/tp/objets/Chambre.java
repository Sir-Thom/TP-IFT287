package tp.objets;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class Chambre {
    private int idChambre;
    private String nomChambre;
    private String typeLit;
    private double prixBase;
    private List<Integer> commodites = new ArrayList<>();

    public Chambre() {
        // Constructeur vide
    }


    public Chambre(Document d) {
        idChambre = d.getInteger("idChambre");
        nomChambre = d.getString("nomChambre");
        typeLit = d.getString("typeLit");
        Double prix = d.getDouble("prixBase"); // récupère l'objet Double (peut être null)
        if (prix == null) {
            this.prixBase = 0.0;
        } else {
            this.prixBase = prix;
        }
    }

    public Chambre(int idChambre, String nomChambre, String typeLit, double prixBase) {
        this.idChambre = idChambre;
        this.nomChambre = nomChambre;
        this.typeLit = typeLit;
        this.prixBase = prixBase;
    }
    // Constructeur pour créer une chambre avec un nom de suite, un ID et un prix de base pour les tests
    public Chambre(String suite, int i, double v) {
        this.nomChambre = suite;
        this.idChambre = i;
        this.prixBase = v;
        this.typeLit = "Lit simple"; // Valeur par défaut, peut être modifiée
    }


    public List<Integer> getCommodites() {
        return commodites;
    }

    public void setCommodites(List<Integer> commodites) {
        this.commodites = commodites;
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
