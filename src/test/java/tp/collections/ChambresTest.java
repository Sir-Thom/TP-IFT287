package tp.collections;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Before;
import org.junit.Test;
import tp.TpExeception;
import tp.bdd.Connexion;
import tp.objets.Chambre;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ChambresTest {

    private Connexion mockConnexion;
    private MongoDatabase mockDatabase;
    private MongoCollection<Document> mockCollection;
    private Chambres chambres;

    @Before
    public void setUp() {
        mockConnexion = mock(Connexion.class);
        mockDatabase = mock(MongoDatabase.class);
        mockCollection = mock(MongoCollection.class);

        when(mockConnexion.getDatabase()).thenReturn(mockDatabase);
        when(mockDatabase.getCollection("Chambres")).thenReturn(mockCollection);

        chambres = new Chambres(mockConnexion);
    }

    @Test
    public void testAjouterChambreOK() throws Exception {
        Chambre chambre = new Chambre();
        chambre.setNomChambre("Suite");
        chambre.setTypeLit("Queen");
        chambre.setPrixBase(120.0);

        FindIterable<Document> mockFindIterable = mock(FindIterable.class);
        MongoCursor<Document> mockCursor = mock(MongoCursor.class);

        when(mockCollection.find()).thenReturn(mockFindIterable);
        when(mockFindIterable.iterator()).thenReturn(mockCursor);

        when(mockCursor.hasNext()).thenReturn(true, false);
        when(mockCursor.next()).thenReturn(new Document("idChambre", 5));

        chambres.ajouterChambre(chambre);

        verify(mockCollection, times(1)).insertOne(any(Document.class));
    }

    @Test
    public void testExisteTrue() {
        FindIterable<Document> mockFindIterable = mock(FindIterable.class);
        when(mockCollection.find(any(Bson.class))).thenReturn(mockFindIterable);
        when(mockFindIterable.first()).thenReturn(new Document("nomChambre", "Suite"));

        assertTrue(chambres.existe("Suite"));
    }

    @Test
    public void testExisteFalse() {
        FindIterable<Document> mockFindIterable = mock(FindIterable.class);
        when(mockCollection.find(any(Bson.class))).thenReturn(mockFindIterable);
        when(mockFindIterable.first()).thenReturn(null);

        assertFalse(chambres.existe("Inconnue"));
    }

    @Test
    public void testGetChambreByNomOK() throws TpExeception {
        Document doc = new Document("nomChambre", "Suite")
                .append("typeLit", "Queen")
                .append("prixBase", 120.0)
                .append("idChambre", 1);
        FindIterable<Document> mockFindIterable = mock(FindIterable.class);
        when(mockCollection.find(any(Bson.class))).thenReturn(mockFindIterable);
        when(mockFindIterable.first()).thenReturn(doc);

        Chambre c = chambres.getChambreByNom("Suite");
        assertNotNull(c);
        assertEquals("Suite", c.getNomChambre());
    }

    @Test(expected = TpExeception.class)
    public void testGetChambreByNomNotFound() throws TpExeception {
        FindIterable<Document> mockFindIterable = mock(FindIterable.class);
        when(mockCollection.find(any(Bson.class))).thenReturn(mockFindIterable);
        when(mockFindIterable.first()).thenReturn(null);

        Chambre c = chambres.getChambreByNom("Inconnue");
        assertNull(c);

    }

    @Test
    public void testGetChambreByIdOK() {
        Document doc = new Document("idChambre", 1).append("nomChambre", "Suite");
        FindIterable<Document> mockFindIterable = mock(FindIterable.class);
        when(mockCollection.find(any(Bson.class))).thenReturn(mockFindIterable);
        when(mockFindIterable.first()).thenReturn(doc);

        Chambre c = chambres.getChambreById(1);
        assertNotNull(c);
        assertEquals("Suite", c.getNomChambre());
    }

    @Test
    public void testGetChambreByIdNotFound() {
        FindIterable<Document> mockFindIterable = mock(FindIterable.class);
        when(mockCollection.find(any(Bson.class))).thenReturn(mockFindIterable);
        when(mockFindIterable.first()).thenReturn(null);

        Chambre c = chambres.getChambreById(999);
        assertNull(c);
    }

    @Test
    public void testModifierChambre() {
        Chambre chambre = new Chambre();
        chambre.setIdChambre(1);
        chambre.setNomChambre("Suite");
        chambre.setTypeLit("King");
        chambre.setPrixBase(200.0);

        chambres.modifierChambre(chambre);

        verify(mockCollection, times(1)).updateOne(any(Bson.class), any(Document.class));
    }

    @Test
    public void testSupprimerChambreOK() {
        DeleteResult mockDeleteResult = mock(DeleteResult.class);
        when(mockDeleteResult.getDeletedCount()).thenReturn(1L);
        when(mockCollection.deleteOne(any())).thenReturn(mockDeleteResult);

        boolean res = chambres.supprimerChambre("Suite");
        assertTrue(res);
    }

    @Test
    public void testSupprimerChambreNotFound() {
        DeleteResult mockDeleteResult = mock(DeleteResult.class);
        when(mockDeleteResult.getDeletedCount()).thenReturn(0L);
        when(mockCollection.deleteOne(any())).thenReturn(mockDeleteResult);

        boolean res = chambres.supprimerChambre("Inconnue");
        assertFalse(res);
    }

    @Test
    public void testGetToutesChambres() {
        FindIterable<Document> mockFindIterable = mock(FindIterable.class);
        MongoCursor<Document> mockCursor = mock(MongoCursor.class);

        when(mockCollection.find()).thenReturn(mockFindIterable);
        when(mockFindIterable.iterator()).thenReturn(mockCursor);

        when(mockCursor.hasNext()).thenReturn(true, false);
        when(mockCursor.next()).thenReturn(new Document("idChambre", 1).append("nomChambre", "Suite"));

        assertEquals(1, chambres.getToutesChambres().size());
    }
}
