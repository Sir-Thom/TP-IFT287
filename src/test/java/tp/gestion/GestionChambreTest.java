package tp.gestion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import tp.TpExeception;
import tp.collections.Chambres;
import tp.collections.Reservations;
import tp.gestion.GestionChambre;
import tp.objets.Chambre;
import tp.objets.Reservation;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GestionChambreTest {

    private Chambres chambresMock;
    private Reservations reservationsMock;
    private GestionChambre gestionChambre;

    @BeforeEach
    public void setup() {
        chambresMock = mock(Chambres.class);
        reservationsMock = mock(Reservations.class);
        when(chambresMock.getConnexion()).thenReturn(null); // simplification
        gestionChambre = new GestionChambre(chambresMock, reservationsMock);
    }

    @Test
    public void testAjouterChambre_Succes() throws TpExeception {
        String nom = "Chambre1";
        when(chambresMock.existe(nom)).thenReturn(false);
        // Pas besoin de mock ajouterChambre car void

        gestionChambre.ajouterChambre(nom, "Queen", 120.0);

        verify(chambresMock).ajouterChambre(any(Chambre.class));
    }

    @Test
    public void testAjouterChambre_ExisteDeja() {
        String nom = "Chambre1";
        when(chambresMock.existe(nom)).thenReturn(true);

        TpExeception exception = assertThrows(TpExeception.class, () -> {
            gestionChambre.ajouterChambre(nom, "Queen", 120.0);
        });

        assertEquals("Une chambre avec le nom '" + nom + "' existe déjà.", exception.getMessage());
    }

    @Test
    public void testModifierChambre_Succes() throws TpExeception {
        Chambre chambre = new Chambre(1, "Chambre1", "Queen", 120.0);
        when(chambresMock.getChambreByNom("Chambre1")).thenReturn(chambre);
        when(chambresMock.existe("ChambreModifiee")).thenReturn(false);

        gestionChambre.modifierChambre("Chambre1", "ChambreModifiee", "King", 150.0);

        assertEquals("ChambreModifiee", chambre.getNomChambre());
        assertEquals("King", chambre.getTypeLit());
        assertEquals(150.0, chambre.getPrixBase());
        verify(chambresMock).modifierChambre(chambre);
    }

    @Test
    public void testModifierChambre_NonExistante() {
        when(chambresMock.getChambreByNom("ChambreX")).thenReturn(null);

        TpExeception ex = assertThrows(TpExeception.class, () -> {
            gestionChambre.modifierChambre("ChambreX", "Nouvelle", "Twin", 100.0);
        });

        assertEquals("La chambre 'ChambreX' n'existe pas.", ex.getMessage());
    }

    @Test
    public void testModifierChambre_NouveauNomExisteDeja() {
        Chambre chambre = new Chambre(1, "Chambre1", "Queen", 120.0);
        when(chambresMock.getChambreByNom("Chambre1")).thenReturn(chambre);
        when(chambresMock.existe("Chambre2")).thenReturn(true);

        TpExeception ex = assertThrows(TpExeception.class, () -> {
            gestionChambre.modifierChambre("Chambre1", "Chambre2", "King", 150.0);
        });

        assertEquals("Une autre chambre avec le nom 'Chambre2' existe déjà.", ex.getMessage());
    }

    @Test
    public void testSupprimerChambre_Succes() throws SQLException, TpExeception {
        Chambre chambre = new Chambre(1, "Chambre1", "Queen", 120.0);
        when(chambresMock.getChambreByNom("Chambre1")).thenReturn(chambre);
        when(reservationsMock.getReservationsFuturesPourChambre(1)).thenReturn(new ArrayList<>());
        when(chambresMock.supprimerChambre("Chambre1")).thenReturn(true);

        gestionChambre.supprimerChambre("Chambre1");

        verify(chambresMock).supprimerChambre("Chambre1");
    }

    @Test
    public void testSupprimerChambre_NonExistante() {
        when(chambresMock.getChambreByNom("ChambreX")).thenReturn(null);

        TpExeception ex = assertThrows(TpExeception.class, () -> {
            gestionChambre.supprimerChambre("ChambreX");
        });

        assertEquals("La chambre 'ChambreX' n'existe pas.", ex.getMessage());
    }

    @Test
    public void testSupprimerChambre_ReservationsFutures() {
        Chambre chambre = new Chambre(1, "Chambre1", "Queen", 120.0);
        List<Reservation> listeReservations = new ArrayList<>();
        listeReservations.add(new Reservation());
        when(chambresMock.getChambreByNom("Chambre1")).thenReturn(chambre);
        when(reservationsMock.getReservationsFuturesPourChambre(1)).thenReturn(listeReservations);

        TpExeception ex = assertThrows(TpExeception.class, () -> {
            gestionChambre.supprimerChambre("Chambre1");
        });

        assertEquals("Impossible de supprimer la chambre 'Chambre1' car elle a des réservations futures.", ex.getMessage());
    }

    @Test
    public void testSupprimerChambre_SuppressionEchoue() throws SQLException {
        Chambre chambre = new Chambre(1, "Chambre1", "Queen", 120.0);
        when(chambresMock.getChambreByNom("Chambre1")).thenReturn(chambre);
        when(reservationsMock.getReservationsFuturesPourChambre(1)).thenReturn(new ArrayList<>());
        when(chambresMock.supprimerChambre("Chambre1")).thenReturn(false);

        TpExeception ex = assertThrows(TpExeception.class, () -> {
            gestionChambre.supprimerChambre("Chambre1");
        });

        assertEquals("La suppression de la chambre 'Chambre1' a échoué.", ex.getMessage());
    }

    @Test
    public void testAfficherChambre_Succes() throws TpExeception {
        Chambre chambre = new Chambre(1, "Chambre1", "Queen", 120.0);
        when(chambresMock.getChambreByNom("Chambre1")).thenReturn(chambre);

        Chambre resultat = gestionChambre.afficherChambre("Chambre1");

        assertEquals(chambre, resultat);
    }

    @Test
    public void testAfficherChambre_NonExistante() {
        when(chambresMock.getChambreByNom("ChambreX")).thenReturn(null);

        TpExeception ex = assertThrows(TpExeception.class, () -> {
            gestionChambre.afficherChambre("ChambreX");
        });

        assertEquals("La chambre 'ChambreX' n'existe pas.", ex.getMessage());
    }

    @Test
    public void testAfficherChambresLibres() throws TpExeception {
        List<Chambre> chambresLibres = new ArrayList<>();
        chambresLibres.add(new Chambre(1, "Chambre1", "Queen", 120.0));
        when(reservationsMock.getChambresLibres("2025-07-01", "2025-07-05")).thenReturn(chambresLibres);

        List<Chambre> result = gestionChambre.afficherChambresLibres("2025-07-01", "2025-07-05");

        assertEquals(1, result.size());
        assertEquals("Chambre1", result.get(0).getNomChambre());
    }

    @Test
    public void testAfficherChambresLibres_Aucune() throws TpExeception {
        when(reservationsMock.getChambresLibres("2025-07-01", "2025-07-05")).thenReturn(new ArrayList<>());

        List<Chambre> result = gestionChambre.afficherChambresLibres("2025-07-01", "2025-07-05");

        assertTrue(result.isEmpty());
    }
}
