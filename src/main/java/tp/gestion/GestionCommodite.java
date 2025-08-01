package tp.gestion;

import tp.bdd.Connexion;
import tp.collections.Commodites;
import tp.collections.Chambres;
import tp.objets.Commodite;
import tp.objets.Chambre;

import java.util.ArrayList;
import java.util.List;

public class GestionCommodite extends GestionTransactions {

    private final Commodites commodites;
    private final Chambres chambres;

    public GestionCommodite(Connexion cx) {
        super(cx);
        this.commodites = new Commodites(cx);
        this.chambres = new Chambres(cx);
    }
    public GestionCommodite(Connexion cx, Commodites commodites, Chambres chambres) {
        super(cx);
        this.commodites = commodites;
        this.chambres = chambres;
    }

    public GestionCommodite(Commodites commodites, Chambres chambres) {
        super(commodites.getConnexion());
        this.commodites = commodites;
        this.chambres = chambres;
    }




    /** Au cas où.
     * Ajoute une commodité dans le système.
     * Param 1 : description
     * Param 2 : surplus
     */
    public void ajouterCommodite(String description, double surplusPrix) throws Exception {
        int id = commodites.getNextId();

        if (commodites.existe(id)) {
            throw new Exception("Une commodité avec l'ID " + id + " existe déjà.");
        }

        Commodite commodite = new Commodite(id, description, surplusPrix);
        commodites.inserer(commodite);
        System.out.println("Commodité ajoutée : " + description + " (ID: " + id + ")");
    }

    /**
     * Ajoute une commodité à une chambre.
     */
    public void inclureCommodite(String nomChambre, int idCommodite) throws Exception {
        if (!commodites.existe(idCommodite)) {
            throw new Exception("La commodité ID " + idCommodite + " n'existe pas.");
        }

        Chambre chambre = chambres.getChambreByNom(nomChambre);
        if (chambre == null) {
            throw new Exception("Chambre " + nomChambre + " introuvable.");
        }

        List<Integer> liste = chambre.getCommodites();
        if (liste == null) {
            liste = new ArrayList<>();
        }

        if (liste.contains(idCommodite)) {
            throw new Exception("La commodité est déjà associée à la chambre.");
        }

        liste.add(idCommodite);
        chambre.setCommodites(liste);
        chambres.modifierChambre(chambre);
        System.out.println("Commodité ID " + idCommodite + " ajoutée à la chambre " + nomChambre);
    }

    /**
     * Enlève une commodité d'une chambre.
     */
    public void enleverCommodite(String nomChambre, int idCommodite) throws Exception {
        Chambre chambre = chambres.getChambreByNom(nomChambre);
        if (chambre == null) {
            throw new Exception("Chambre " + nomChambre + " introuvable.");
        }

        List<Integer> liste = chambre.getCommodites();
        if (liste == null || !liste.contains(idCommodite)) {
            throw new Exception("La commodité n'est pas associée à cette chambre.");
        }

        liste.remove(Integer.valueOf(idCommodite));
        chambre.setCommodites(liste);
        chambres.modifierChambre(chambre);
        System.out.println("Commodité ID " + idCommodite + " retirée de la chambre " + nomChambre);
    }

    /**
     * Liste les commodités associées à une chambre.
     */
    public List<Commodite> obtenirCommoditesChambre(String nomChambre) throws Exception {
        Chambre chambre = chambres.getChambreByNom(nomChambre);
        if (chambre == null) {
            throw new Exception("Chambre " + nomChambre + " introuvable.");
        }

        List<Commodite> resultats = new ArrayList<>();
        List<Integer> ids = chambre.getCommodites();

        if (ids != null) {
            for (Integer id : ids) {
                Commodite c = commodites.obtenirCommodite(id);
                if (c != null) {
                    resultats.add(c);
                }
            }
        }

        return resultats;
    }

}
