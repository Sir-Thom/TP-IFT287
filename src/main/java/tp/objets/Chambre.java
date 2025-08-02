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
        Double prix = d.getDouble("prixBase");
        if (prix == null) {
            this.prixBase = 0.0;
        } else {
            this.prixBase = prix;
        }

        //  Charger la liste des commodités du document
        @SuppressWarnings("unchecked")
        List<Integer> commoditesFromDoc = (List<Integer>) d.get("commodites");
        if (commoditesFromDoc != null) {
            this.commodites = new ArrayList<>(commoditesFromDoc);
        } else {
            this.commodites = new ArrayList<>();
        }
    }

    public Chambre(int idChambre, String nomChambre, String typeLit, double prixBase) {
        this.idChambre = idChambre;
        this.nomChambre = nomChambre;
        this.typeLit = typeLit;
        this.prixBase = prixBase;
        this.commodites = new ArrayList<>();
    }

    public Chambre(String suite, int i, double v) {
        this.nomChambre = suite;
        this.idChambre = i;
        this.prixBase = v;
        this.typeLit = "Lit simple"; // Valeur par défaut, peut être modifiée
        this.commodites = new ArrayList<>();
    }

    public Document toDocument() {
        Document doc = new Document("idChambre", idChambre)
                .append("nomChambre", nomChambre)
                .append("typeLit", typeLit)
                .append("prixBase", prixBase);

        // Ajouter la liste des commodités
        if (commodites != null && !commodites.isEmpty()) {
            doc.append("commodites", commodites);
        } else {
            doc.append("commodites", new ArrayList<>());
        }

        return doc;
    }

    public List<Integer> getCommodites() {
        if (commodites == null) {
            commodites = new ArrayList<>();
        }
        return commodites;
    }

    public void setCommodites(List<Integer> commodites) {
        this.commodites = commodites != null ? commodites : new ArrayList<>();
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
                ", commodites=" + commodites +
                '}';
    }
}