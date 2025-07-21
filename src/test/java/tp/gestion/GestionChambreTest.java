package tp.gestion;

import org.junit.Before;
import org.junit.Test;
import tp.TpExeception;
import tp.collections.Chambres;
import tp.collections.Reservations;
import tp.objets.Chambre;
import tp.objets.Reservation;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GestionChambreTest {

    private Chambres mockChambres;
    private Reservations mockReservations;
    private GestionChambre gestionChambre;

    @Before
    public void setUp() {
        mockChambres = mock(Chambres.class);
        mockReservations = mock(Reservations.class);
        gestionChambre = new GestionChambre(mockChambres, mockReservations);
    }

    @Test
    public void testAjouterChambre_Succes() throws TpExeception {
        when(mockChambres.existe("Chambre1")).thenReturn(false);

        gestionChambre.ajouterChambre("Chambre1", "Queen", 100.0);

        verify(mockChambres, times(1)).ajouterChambre(any(Chambre.class));
    }

    @Test(expected = TpExeception.class)
    public void testAjouterChambre_DejaExistante() throws TpExeception {
        when(mockChambres.existe("Chambre1")).thenReturn(true);

        gestionChambre.ajouterChambre("Chambre1", "Queen", 100.0);
    }

    @Test
    public void testModifierChambre_Succes() throws TpExeception {
        Chambre chambre = new Chambre(1, "Chambre1", "Queen", 100.0);
        when(mockChambres.getChambreByNom("Chambre1")).thenReturn(chambre);
        when(mockChambres.existe("Chambre1")).thenReturn(false);

        gestionChambre.modifierChambre("Chambre1", "Chambre1", "King", 120.0);

        verify(mockChambres, times(1)).modifierChambre(any(Chambre.class));
        assertEquals("King", chambre.getTypeLit());
        assertEquals(120.0, chambre.getPrixBase(), 0.01);
    }

    @Test(expected = TpExeception.class)
    public void testModifierChambre_ChambreInexistante() throws TpExeception {
        when(mockChambres.getChambreByNom("Chambre1")).thenReturn(null);

        gestionChambre.modifierChambre("Chambre1", "Chambre2", "King", 120.0);
    }

    @Test(expected = TpExeception.class)
    public void testModifierChambre_NouveauNomDejaPris() throws TpExeception {
        Chambre chambre = new Chambre(1, "Chambre1", "Queen", 100.0);
        when(mockChambres.getChambreByNom("Chambre1")).thenReturn(chambre);
        when(mockChambres.existe("Chambre2")).thenReturn(true);

        gestionChambre.modifierChambre("Chambre1", "Chambre2", "King", 120.0);
    }

    @Test
    public void testSupprimerChambre_Succes() throws TpExeception, SQLException {
        Chambre chambre = new Chambre(1, "Chambre1", "Queen", 100.0);
        when(mockChambres.getChambreByNom("Chambre1")).thenReturn(chambre);
        when(mockReservations.getReservationsFuturesPourChambre(1)).thenReturn(Collections.emptyList());
        when(mockChambres.supprimerChambre("Chambre1")).thenReturn(true);

        gestionChambre.supprimerChambre("Chambre1");

        verify(mockChambres, times(1)).supprimerChambre("Chambre1");
    }

    @Test(expected = TpExeception.class)
    public void testSupprimerChambre_ExistanteAvecReservations() throws TpExeception, SQLException {
        Chambre chambre = new Chambre(1, "Chambre1", "Queen", 100.0);
        when(mockChambres.getChambreByNom("Chambre1")).thenReturn(chambre);
        when(mockReservations.getReservationsFuturesPourChambre(1))
                .thenReturn(List.of(new Reservation()));

        gestionChambre.supprimerChambre("Chambre1");
    }

    @Test(expected = TpExeception.class)
    public void testSupprimerChambre_Inexistante() throws TpExeception, SQLException {
        when(mockChambres.getChambreByNom("Chambre1")).thenReturn(null);

        gestionChambre.supprimerChambre("Chambre1");
    }

    @Test
    public void testAfficherChambre_Succes() throws TpExeception {
        Chambre chambre = new Chambre(1, "Chambre1", "Queen", 100.0);
        when(mockChambres.getChambreByNom("Chambre1")).thenReturn(chambre);

        Chambre resultat = gestionChambre.afficherChambre("Chambre1");

        assertEquals(chambre, resultat);
    }

    @Test(expected = TpExeception.class)
    public void testAfficherChambre_Inexistante() throws TpExeception {
        when(mockChambres.getChambreByNom("Chambre1")).thenReturn(null);

        gestionChambre.afficherChambre("Chambre1");
    }

    @Test
    public void testAfficherChambresLibres() throws TpExeception {
        Chambre chambre1 = new Chambre(1, "Chambre1", "Queen", 100.0);
        when(mockReservations.getChambresLibres("2025-07-01", "2025-07-10"))
                .thenReturn(List.of(chambre1));

        List<Chambre> resultat = gestionChambre.afficherChambresLibres("2025-07-01", "2025-07-10");

        assertEquals(1, resultat.size());
        assertEquals("Chambre1", resultat.get(0).getNomChambre());
    }

    @Test
    public void testAfficherChambresLibres_Aucune() throws TpExeception {
        when(mockReservations.getChambresLibres("2025-07-01", "2025-07-10"))
                .thenReturn(Collections.emptyList());

        List<Chambre> resultat = gestionChambre.afficherChambresLibres("2025-07-01", "2025-07-10");

        assertTrue(resultat.isEmpty());
    }
}
