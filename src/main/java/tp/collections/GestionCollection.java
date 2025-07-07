package tp.collections;

import tp.bdd.Connexion;

public abstract class GestionCollection {

    protected final Connexion cx;

    public GestionCollection(Connexion cx) {
        this.cx = cx;

    }

    public Connexion getConnexion() {
        return cx;
    }
}

