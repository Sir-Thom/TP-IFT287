package tp.collections;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Before;
import org.junit.Test;
import tp.bdd.Connexion;
import tp.objets.Commodite;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CommoditesTest {
    private MongoDatabase mockDatabase;
    private Connexion mockConnexion;
    private MongoCollection<Document> mockCollection;
    private Commodites commodites;

    @Before
    public void setUp() throws Exception {
        mockConnexion = mock(Connexion.class);
        mockCollection = mock(MongoCollection.class);
        mockDatabase = mock(MongoDatabase.class);
        when(mockConnexion.getDatabase()).thenReturn(mockDatabase);
        when(mockDatabase.getCollection("Commodite")).thenReturn(mockCollection);

        commodites = new Commodites(mockConnexion);
    }

    @Test
    public void testInserer() {
        Commodite commodite = new Commodite(1, "Télévision", 25.0);
        commodites.inserer(commodite);
        verify(mockCollection, times(1)).insertOne(any(Document.class));
    }

    @Test
    public void testExisteTrue() {
        @SuppressWarnings("unchecked")
        FindIterable<Document> mockFind = (FindIterable<Document>) mock(FindIterable.class);
        when(mockCollection.find((Bson) any())).thenReturn(mockFind);
        when(mockFind.first()).thenReturn(new Document("idCommodite", 1));

        assertTrue(commodites.existe(1));
    }

    @Test
    public void testExisteFalse() {
        @SuppressWarnings("unchecked")
        FindIterable<Document> mockFind = (FindIterable<Document>) mock(FindIterable.class);
        when(mockCollection.find((Bson) any())).thenReturn(mockFind);
        when(mockFind.first()).thenReturn(null);

        assertFalse(commodites.existe(999));
    }

    @Test
    public void testObtenirCommoditeOK() {
        @SuppressWarnings("unchecked")
        FindIterable<Document> mockFind = (FindIterable<Document>) mock(FindIterable.class);
        when(mockCollection.find((Bson) any())).thenReturn(mockFind);

        when(mockFind.first()).thenReturn(
                new Document("idCommodite", 1)
                        .append("description", "Télévision")
                        .append("surplusPrix", 25.0)
        );

        Commodite c = commodites.obtenirCommodite(1);
        assertNotNull(c);
        assertEquals(1, c.getIdCommodite());
        assertEquals("Télévision", c.getDescription());
        assertEquals(25.0, c.getSurplusPrix(), 0.001);
    }

    @Test
    public void testObtenirCommoditeNotFound() {
        @SuppressWarnings("unchecked")
        FindIterable<Document> mockFind = (FindIterable<Document>) mock(FindIterable.class);
        when(mockCollection.find((Bson) any())).thenReturn(mockFind);
        when(mockFind.first()).thenReturn(null);

        Commodite c = commodites.obtenirCommodite(999);
        assertNull(c);
    }

    @Test
    public void testObtenirToutesLesCommodites() {
        @SuppressWarnings("unchecked")
        FindIterable<Document> mockFind = (FindIterable<Document>) mock(FindIterable.class);
        MongoCursor<Document> mockCursor = (MongoCursor<Document>) mock(MongoCursor.class);

        when(mockCollection.find()).thenReturn(mockFind);
        when(mockFind.iterator()).thenReturn(mockCursor);

        when(mockCursor.hasNext()).thenReturn(true, false);
        when(mockCursor.next()).thenReturn(
                new Document("idCommodite", 1)
                        .append("description", "Télévision")
                        .append("surplusPrix", 25.0)
        );

        assertEquals(1, commodites.obtenirToutesLesCommodites().size());
    }

    @Test
    public void testGetNextId() {
        @SuppressWarnings("unchecked")
        FindIterable<Document> mockFind = (FindIterable<Document>) mock(FindIterable.class);
        MongoCursor<Document> mockCursor = (MongoCursor<Document>) mock(MongoCursor.class);

        when(mockCollection.find()).thenReturn(mockFind);
        when(mockFind.iterator()).thenReturn(mockCursor);

        when(mockCursor.hasNext()).thenReturn(true, true, false);
        when(mockCursor.next())
                .thenReturn(new Document("idCommodite", 1))
                .thenReturn(new Document("idCommodite", 3));

        assertEquals(4, commodites.getNextId());
    }
}
