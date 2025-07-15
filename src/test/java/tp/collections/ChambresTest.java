package tp.collections;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import junit.framework.TestCase;

import org.bson.Document;
import tp.bdd.Connexion;
import tp.objets.Chambre;

import static org.mockito.Mockito.*;

public class ChambresTest extends TestCase {

    private Connexion mockConnexion;
    private MongoDatabase mockDatabase;
    private MongoCollection<Document> mockCollection;
    private Chambres chambres;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mockConnexion = mock(Connexion.class);
        mockDatabase = mock(MongoDatabase.class);
        mockCollection = mock(MongoCollection.class);

        when(mockConnexion.getDatabase()).thenReturn(mockDatabase);
        when(mockDatabase.getCollection("Chambres")).thenReturn(mockCollection);

        chambres = new Chambres(mockConnexion);
    }

    public void testAjouterChambreOK() throws Exception {
        Chambre chambre = new Chambre();
        chambre.setNomChambre("Suite");
        chambre.setTypeLit("Queen");
        chambre.setPrixBase(120.0);

        @SuppressWarnings("unchecked")
        FindIterable<Document> mockFindIterable = (FindIterable<Document>) mock(FindIterable.class);
        @SuppressWarnings("unchecked")
        MongoCursor<Document> mockCursor = (MongoCursor<Document>) mock(MongoCursor.class);

        when(mockCollection.find()).thenReturn(mockFindIterable);
        when(mockFindIterable.iterator()).thenReturn(mockCursor);

        when(mockCursor.hasNext()).thenReturn(true, false);
        when(mockCursor.next()).thenReturn(new Document("idChambre", 5));

        chambres.ajouterChambre(chambre);

        verify(mockCollection, times(1)).insertOne(any(Document.class));
    }

    public void testExisteTrue() throws Exception {
        @SuppressWarnings("unchecked")
        FindIterable<Document> mockFindIterable = (FindIterable<Document>) mock(FindIterable.class);
        @SuppressWarnings("unchecked")
        MongoCursor<Document> mockCursor = (MongoCursor<Document>) mock(MongoCursor.class);
        when(mockCollection.find()).thenReturn(mockFindIterable);
        when(mockFindIterable.iterator()).thenReturn(mockCursor);

        when(mockCursor.hasNext()).thenReturn(true, false);

        when(mockFindIterable.first()).thenReturn(new Document("nomChambre", "Suite"));

        assertTrue(chambres.existe("Suite"));
    }

    public void testExisteFalse() throws Exception {
        @SuppressWarnings("unchecked")
        FindIterable<Document> mockFindIterable = (FindIterable<Document>) mock(FindIterable.class);
        @SuppressWarnings("unchecked")
        MongoCursor<Document> mockCursor = (MongoCursor<Document>) mock(MongoCursor.class);
        when(mockCollection.find()).thenReturn(mockFindIterable);
        when(mockFindIterable.iterator()).thenReturn(mockCursor);

        when(mockCursor.hasNext()).thenReturn(true, false);
        when(mockFindIterable.first()).thenReturn(null);

        assertFalse(chambres.existe("Inconnue"));
    }

    public void testGetChambreByNomOK() throws Exception {
        Document doc = new Document("nomChambre", "Suite");
        @SuppressWarnings("unchecked")
        FindIterable<Document> mockFindIterable = (FindIterable<Document>) mock(FindIterable.class);
        @SuppressWarnings("unchecked")
        MongoCursor<Document> mockCursor = (MongoCursor<Document>) mock(MongoCursor.class);
        when(mockCollection.find()).thenReturn(mockFindIterable);
        when(mockFindIterable.iterator()).thenReturn(mockCursor);
        when(mockCursor.hasNext()).thenReturn(true, false);
        when(mockFindIterable.first()).thenReturn(doc);

        Chambre c = chambres.getChambreByNom("Suite");
        assertNotNull(c);
        assertEquals("Suite", c.getNomChambre());
    }

    public void testGetChambreByNomNotFound() throws Exception {
        @SuppressWarnings("unchecked")
        FindIterable<Document> mockFindIterable = (FindIterable<Document>) mock(FindIterable.class);
        @SuppressWarnings("unchecked")
        MongoCursor<Document> mockCursor = (MongoCursor<Document>) mock(MongoCursor.class);
        when(mockCollection.find()).thenReturn(mockFindIterable);
        when(mockFindIterable.iterator()).thenReturn(mockCursor);

        when(mockCursor.hasNext()).thenReturn(true, false);
        when(mockFindIterable.first()).thenReturn(null);

        Chambre c = chambres.getChambreByNom("Inconnue");
        assertNull(c);
    }

    public void testGetChambreByIdOK() throws Exception {
        Document doc = new Document("idChambre", 1).append("nomChambre", "Suite");
        @SuppressWarnings("unchecked")
        FindIterable<Document> mockFindIterable = (FindIterable<Document>) mock(FindIterable.class);
        @SuppressWarnings("unchecked")
        MongoCursor<Document> mockCursor = (MongoCursor<Document>) mock(MongoCursor.class);
        when(mockCollection.find()).thenReturn(mockFindIterable);
        when(mockFindIterable.iterator()).thenReturn(mockCursor);

        when(mockCursor.hasNext()).thenReturn(true, false);
        when(mockFindIterable.first()).thenReturn(doc);

        Chambre c = chambres.getChambreById(1);
        assertNotNull(c);
        assertEquals("Suite", c.getNomChambre());
    }

    public void testGetChambreByIdNotFound() throws Exception {
        @SuppressWarnings("unchecked")
        FindIterable<Document> mockFindIterable = (FindIterable<Document>) mock(FindIterable.class);
        @SuppressWarnings("unchecked")
        MongoCursor<Document> mockCursor = (MongoCursor<Document>) mock(MongoCursor.class);
        when(mockCollection.find()).thenReturn(mockFindIterable);
        when(mockFindIterable.iterator()).thenReturn(mockCursor);

        when(mockCursor.hasNext()).thenReturn(true, false);
        when(mockFindIterable.first()).thenReturn(null);

        Chambre c = chambres.getChambreById(999);
        assertNull(c);
    }


    public void testModifierChambre() throws Exception {
        Chambre chambre = new Chambre();
        chambre.setIdChambre(1);
        chambre.setNomChambre("Suite");
        chambre.setTypeLit("King");
        chambre.setPrixBase(200.0);

        chambres.modifierChambre(chambre);

        verify(mockCollection, times(1)).updateOne(any(Document.class), any(Document.class));
    }

    public void testSupprimerChambreOK() throws Exception {
        DeleteResult mockDeleteResult = mock(DeleteResult.class);
        when(mockDeleteResult.getDeletedCount()).thenReturn(1L);
        when(mockCollection.deleteOne(any())).thenReturn(mockDeleteResult);

        boolean res = chambres.supprimerChambre("Suite");
        assertTrue(res);
    }

    public void testSupprimerChambreNotFound() throws Exception {
        DeleteResult mockDeleteResult = mock(DeleteResult.class);
        when(mockDeleteResult.getDeletedCount()).thenReturn(0L);
        when(mockCollection.deleteOne(any())).thenReturn(mockDeleteResult);

        boolean res = chambres.supprimerChambre("Inconnue");
        assertFalse(res);
    }

    public void testGetToutesChambres() throws Exception {
        @SuppressWarnings("unchecked")
        FindIterable<Document> mockFindIterable = (FindIterable<Document>) mock(FindIterable.class);
        @SuppressWarnings("unchecked")
        MongoCursor<Document> mockCursor = (MongoCursor<Document>) mock(MongoCursor.class);

        when(mockCollection.find()).thenReturn(mockFindIterable);
        when(mockFindIterable.iterator()).thenReturn(mockCursor);

        when(mockCursor.hasNext()).thenReturn(true, false);
        when(mockCursor.next()).thenReturn(new Document("idChambre", 1).append("nomChambre", "Suite"));

        assertEquals(1, chambres.getToutesChambres().size());
    }
}
