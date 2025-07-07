package tp.objets;
import org.bson.Document;

public class Commodite {

    private int idCommodite;
    private String description;
    private double surplusPrix;

    public Commodite() {
        // Constructeur vide requis par certains frameworks (ex. MongoDB, JPA, etc.)
    }

    public Commodite(Document d) {
        this.idCommodite = d.getInteger("idCommodite");
        this.description = d.getString("description");
        this.surplusPrix = d.getDouble("surplusPrix");
    }

    public Commodite(int idCommodite, String description, double surplusPrix) {
        this.idCommodite = idCommodite;
        this.description = description;
        this.surplusPrix = surplusPrix;
    }

    public int getIdCommodite() {
        return idCommodite;
    }

    public void setIdCommodite(int idCommodite) {
        this.idCommodite = idCommodite;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getSurplusPrix() {
        return surplusPrix;
    }

    public void setSurplusPrix(double surplusPrix) {
        this.surplusPrix = surplusPrix;
    }

    public Document toDocument() {
        Document d = new Document();
        d.append("idCommodite", idCommodite);
        d.append("description", description);
        d.append("surplusPrix", surplusPrix);
        return d;
    }

    @Override
    public String toString() {
        return "Commodite{" +
                "idCommodite=" + idCommodite +
                ", description='" + description + '\'' +
                ", surplusPrix=" + surplusPrix +
                '}';
    }
}
