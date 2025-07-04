package tp.gestion;

import tp.bdd.Connexion;

public abstract class GestionTransactions {
    protected final Connexion cx;

    protected GestionTransactions(Connexion cx) {
        this.cx = cx;
    }
}
