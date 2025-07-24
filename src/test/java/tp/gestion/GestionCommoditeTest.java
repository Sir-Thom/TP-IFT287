package tp.gestion;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import tp.collections.Commodites;
import tp.collections.Chambres;
import tp.objets.Chambre;
import tp.objets.Commodite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GestionCommoditeTest {

    @Mock
    private Commodites mockCommodites;

    @Mock
    private Chambres mockChambres;

    @InjectMocks
    private GestionCommodite gestionCommodite;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        gestionCommodite = new GestionCommodite(mockCommodites, mockChambres);
    }

    @Test
    public void testAjouterCommodite_OK() throws Exception {
        when(mockCommodites.getNextId()).thenReturn(1);
        when(mockCommodites.existe(1)).thenReturn(false);

        gestionCommodite.ajouterCommodite("Balcon", 50.0);

        verify(mockCommodites).inserer(any(Commodite.class));
    }

    @Test(expected = Exception.class)
    public void testAjouterCommodite_DejaExiste() throws Exception {
        when(mockCommodites.getNextId()).thenReturn(1);
        when(mockCommodites.existe(1)).thenReturn(true);

        gestionCommodite.ajouterCommodite("Balcon", 50.0);
    }

    @Test
    public void testAjouterCommodite_AvecId_OK() throws Exception {
        when(mockCommodites.existe(10)).thenReturn(false);

        gestionCommodite.ajouterCommodite(10, "Piscine", 100.0);

        verify(mockCommodites).inserer(any(Commodite.class));
    }

    @Test(expected = Exception.class)
    public void testAjouterCommodite_AvecId_DejaExiste() throws Exception {
        when(mockCommodites.existe(10)).thenReturn(true);

        gestionCommodite.ajouterCommodite(10, "Piscine", 100.0);
    }

    @Test
    public void testInclureCommodite_OK() throws Exception {
        int idCommodite = 5;
        String nomChambre = "Suite";
        Chambre chambre = new Chambre("Suite", 2, 200.0);

        chambre.setCommodites(new ArrayList<>()); // vide au début

        when(mockCommodites.existe(idCommodite)).thenReturn(true);
        when(mockChambres.getChambreByNom(nomChambre)).thenReturn(chambre);

        gestionCommodite.inclureCommodite(nomChambre, idCommodite);

        assertTrue(chambre.getCommodites().contains(idCommodite));
        verify(mockChambres).modifierChambre(chambre);
    }

    @Test(expected = Exception.class)
    public void testInclureCommodite_NonExistante() throws Exception {
        when(mockCommodites.existe(5)).thenReturn(false);

        gestionCommodite.inclureCommodite("Suite", 5);
    }

    @Test(expected = Exception.class)
    public void testInclureCommodite_ChambreIntrouvable() throws Exception {
        when(mockCommodites.existe(5)).thenReturn(true);
        when(mockChambres.getChambreByNom("Suite")).thenReturn(null);

        gestionCommodite.inclureCommodite("Suite", 5);
    }

    @Test(expected = Exception.class)
    public void testInclureCommodite_DejaPresente() throws Exception {
        Chambre chambre = new Chambre("Suite", 2, 200.0);
        chambre.setCommodites(new ArrayList<>(Arrays.asList(5)));

        when(mockCommodites.existe(5)).thenReturn(true);
        when(mockChambres.getChambreByNom("Suite")).thenReturn(chambre);

        gestionCommodite.inclureCommodite("Suite", 5);
    }

    @Test
    public void testEnleverCommodite_OK() throws Exception {
        Chambre chambre = new Chambre("Suite", 2, 200.0);
        chambre.setCommodites(new ArrayList<>(Arrays.asList(5)));

        when(mockChambres.getChambreByNom("Suite")).thenReturn(chambre);

        gestionCommodite.enleverCommodite("Suite", 5);

        assertFalse(chambre.getCommodites().contains(5));
        verify(mockChambres).modifierChambre(chambre);
    }

    @Test(expected = Exception.class)
    public void testEnleverCommodite_ChambreIntrouvable() throws Exception {
        when(mockChambres.getChambreByNom("Suite")).thenReturn(null);

        gestionCommodite.enleverCommodite("Suite", 5);
    }

    @Test(expected = Exception.class)
    public void testEnleverCommodite_NonPresente() throws Exception {
        Chambre chambre = new Chambre("Suite", 2, 200.0);
        chambre.setCommodites(new ArrayList<>());

        when(mockChambres.getChambreByNom("Suite")).thenReturn(chambre);

        gestionCommodite.enleverCommodite("Suite", 5);
    }

    @Test
    public void testObtenirCommoditesChambre_OK() throws Exception {
        Chambre chambre = new Chambre("Suite", 2, 200.0);
        chambre.setCommodites(Arrays.asList(1, 2));

        Commodite commodite1 = new Commodite(1, "Balcon", 50.0);
        Commodite commodite2 = new Commodite(2, "Piscine", 100.0);

        when(mockChambres.getChambreByNom("Suite")).thenReturn(chambre);
        when(mockCommodites.obtenirCommodite(1)).thenReturn(commodite1);
        when(mockCommodites.obtenirCommodite(2)).thenReturn(commodite2);

        List<Commodite> resultats = gestionCommodite.obtenirCommoditesChambre("Suite");

        assertEquals(2, resultats.size());
        assertTrue(resultats.contains(commodite1));
        assertTrue(resultats.contains(commodite2));
    }

    @Test(expected = Exception.class)
    public void testObtenirCommoditesChambre_ChambreIntrouvable() throws Exception {
        when(mockChambres.getChambreByNom("Suite")).thenReturn(null);

        gestionCommodite.obtenirCommoditesChambre("Suite");
    }

}
