package tp.collections;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import junit.framework.TestCase;
import org.mockito.Mockito;
import tp.TpExeception;
import tp.bdd.Connexion;
import tp.objets.Client;

import static org.mockito.Mockito.*;

public class ClientsTest extends TestCase {

    private Connexion mockConnexion;
    private MongoDatabase mockDatabase;
    private MongoCollection<Document> mockCollection;
    private Clients clients;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mockConnexion = mock(Connexion.class);
        mockDatabase = mock(MongoDatabase.class);
        mockCollection = mock(MongoCollection.class);

        when(mockConnexion.getDatabase()).thenReturn(mockDatabase);
        when(mockDatabase.getCollection("Clients")).thenReturn(mockCollection);

        clients = new Clients(mockConnexion);
    }

    public void testAjouterClientOK() throws Exception {
        Client client = new Client();
        client.setNom("Dupont");
        client.setPrenom("Jean");
        client.setAge(30);

        // Mock FindIterable et son cursor
        FindIterable<Document> mockFindIterable = mock(FindIterable.class);
        MongoCursor<Document> mockCursor = mock(MongoCursor.class);

        when(mockCollection.find()).thenReturn(mockFindIterable);
        when(mockFindIterable.iterator()).thenReturn(mockCursor);

        // Simuler qu'il y a un seul document
        when(mockCursor.hasNext()).thenReturn(true, false); // 1 seul élément puis fin
        when(mockCursor.next()).thenReturn(new Document("idClient", 1));

        clients.ajouterClient(client);

        verify(mockCollection, times(1)).insertOne(any(Document.class));
    }

    public void testAjouterClientAvecErreur() throws Exception {
        Client client = new Client();
        client.setNom("");
        client.setPrenom("");
        client.setAge(30);

        try {
            clients.ajouterClient(client);
            fail("TpExeception attendue");
        } catch (TpExeception e) {
            assertTrue(e.getMessage().contains("nom du client"));
        }
    }

    public void testExisteTrue() throws Exception {
        Document foundDoc = new Document("nom", "Dupont");
        FindIterable<Document> mockFindIterable = mock(FindIterable.class);

        when(mockCollection.find(any(Document.class))).thenReturn(mockFindIterable);
        when(mockFindIterable.first()).thenReturn(foundDoc);

        assertTrue(clients.existe("Dupont"));
    }


    public void testExisteFalse() throws Exception {
        FindIterable<Document> mockFindIterable = mock(FindIterable.class);

        when(mockCollection.find(any(Document.class))).thenReturn(mockFindIterable);
        when(mockFindIterable.first()).thenReturn(null); // pas trouvé

        assertFalse(clients.existe("Inconnu"));
    }


    public void testGetClientByIdOK() throws Exception {
        Document doc = new Document("idClient", 1).append("nom", "Dupont");
        FindIterable<Document> mockFindIterable = mock(FindIterable.class);

        when(mockCollection.find(any(Document.class))).thenReturn(mockFindIterable);
        when(mockFindIterable.first()).thenReturn(doc);

        Document result = clients.getClientById(1);
        assertEquals("Dupont", result.getString("nom"));
    }

    public void testGetClientByIdNotFound() throws Exception {
        FindIterable<Document> mockFindIterable = mock(FindIterable.class);
        MongoCursor<Document> mockCursor = mock(MongoCursor.class);

        when(mockCollection.find(any(Document.class))).thenReturn(mockFindIterable);
        when(mockFindIterable.iterator()).thenReturn(mockCursor);

        // Ici, aucun résultat
        when(mockCursor.hasNext()).thenReturn(false);

        try {
            clients.getClientById(999);
            fail("TpExeception attendue");
        } catch (TpExeception e) {
            assertTrue(e.getMessage().contains("n'existe pas"));
        }
    }




    public void testSupprimerClientOK() throws Exception {
        Client mockClient = new Client();
        mockClient.setId(1);
        mockClient.setNom("Dupont");
        mockClient.setPrenom("Jean");

        Clients spyClients = Mockito.spy(clients);
        Mockito.doReturn(mockClient).when(spyClients).GetClientByNomPrenom("Dupont", "Jean");

        Reservations mockReservations = mock(Reservations.class);
        spyClients.reservations = mockReservations;
        when(mockReservations.clientADesReservations(1)).thenReturn(false);

        DeleteResult mockDeleteResult = mock(DeleteResult.class);
        when(mockDeleteResult.getDeletedCount()).thenReturn(1L);
        when(mockCollection.deleteOne(any())).thenReturn(mockDeleteResult);

        boolean result = spyClients.supprimerClient("Jean", "Dupont");
        assertTrue(result);
    }

    public void testSupprimerClientNotFound() throws Exception {
        Clients spyClients = Mockito.spy(clients);
        Mockito.doReturn(null).when(spyClients).GetClientByNomPrenom("Inconnu", "Test");

        try {
            spyClients.supprimerClient("Test", "Inconnu");
            fail("TpExeception attendue");
        } catch (TpExeception e) {
            assertTrue(e.getMessage().contains("n'existe pas"));
        }
    }
}
