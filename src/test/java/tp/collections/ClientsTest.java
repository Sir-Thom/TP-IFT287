package tp.collections;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import tp.TpExeception;
import tp.bdd.Connexion;
import tp.objets.Client;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ClientsTest {

    private Connexion mockConnexion;
    private MongoDatabase mockDatabase;
    private MongoCollection<Document> mockCollection;
    private Clients clients;

    @Before
    public void setUp() throws Exception {
        mockConnexion = mock(Connexion.class);
        mockDatabase = mock(MongoDatabase.class);
        mockCollection = mock(MongoCollection.class);

        when(mockConnexion.getDatabase()).thenReturn(mockDatabase);
        when(mockDatabase.getCollection("Clients")).thenReturn(mockCollection);

        clients = new Clients(mockConnexion);
    }
    @Test
    public void testGetClients_ReturnsListOfClients() {
        Document doc1 = new Document("idClient",1).append("nom", "Dupont").append("prenom", "Jean").append("age", 30);
        Document doc2 = new Document("idClient",2).append("nom", "Martin").append("prenom", "Claire").append("age", 25);

        FindIterable<Document> mockFindIterable = mock(FindIterable.class);
        MongoCursor<Document> mockCursor = mock(MongoCursor.class);

        when(mockCollection.find()).thenReturn(mockFindIterable);
        when(mockFindIterable.iterator()).thenReturn(mockCursor);

        when(mockCursor.hasNext()).thenReturn(true, true, false);
        when(mockCursor.next()).thenReturn(doc1, doc2);

        List<Client> clientsList = clients.getClients();

        assertNotNull(clientsList);
        assertEquals(2, clientsList.size());
        assertEquals("Dupont", clientsList.get(0).getNom());
        assertEquals("Martin", clientsList.get(1).getNom());
    }


    @Test
    public void testGetClients_EmptyCollection_ReturnsEmptyList() {
        FindIterable<Document> mockFindIterable = mock(FindIterable.class);
        MongoCursor<Document> mockCursor = mock(MongoCursor.class);

        when(mockCollection.find()).thenReturn(mockFindIterable);
        when(mockFindIterable.iterator()).thenReturn(mockCursor);
        when(mockCursor.hasNext()).thenReturn(false); // aucun document

        List<Client> clientsList = clients.getClients();

        assertNotNull(clientsList);
        assertTrue(clientsList.isEmpty());
    }



    @Test
    public void testAjouterClientOK() throws Exception {
        Client client = new Client();
        client.setNom("Dupont");
        client.setPrenom("Jean");
        client.setAge(30);

        FindIterable<Document> mockFindIterable = mock(FindIterable.class);
        MongoCursor<Document> mockCursor = mock(MongoCursor.class);

        when(mockCollection.find()).thenReturn(mockFindIterable);
        when(mockFindIterable.iterator()).thenReturn(mockCursor);
        when(mockCursor.hasNext()).thenReturn(true, false);
        when(mockCursor.next()).thenReturn(new Document("idClient", 1));

        clients.ajouterClient(client);

        verify(mockCollection, times(1)).insertOne(any(Document.class));
    }

    @Test(expected = TpExeception.class)
    public void testAjouterClientAvecErreur() throws Exception {
        Client client = new Client();
        client.setNom("");
        client.setPrenom("");
        client.setAge(30);

        clients.ajouterClient(client);
    }
    @Test
    public void testGetClientByNomPrenomOK() throws Exception {
        Document doc = new Document("idClient", 1)
                .append("nom", "Dupont")
                .append("prenom", "Jean")
                .append("age", 30);

        FindIterable<Document> mockFindIterable = mock(FindIterable.class);

        // Quand on fait find avec n'importe quel document (query), on retourne le mockFindIterable
        when(mockCollection.find(any(Document.class))).thenReturn(mockFindIterable);
        // Quand on fait first() sur ce FindIterable, on retourne le document
        when(mockFindIterable.first()).thenReturn(doc);

        Client result = clients.GetClientByNomPrenom("Dupont", "Jean");
        assertNotNull(result);
        assertEquals("Dupont", result.getNom());
        assertEquals("Jean", result.getPrenom());
    }

    @Test
    public void testGetClientByNomPrenomNotFound() throws Exception {
        FindIterable<Document> mockFindIterable = mock(FindIterable.class);

        when(mockCollection.find(any(Document.class))).thenReturn(mockFindIterable);
        when(mockFindIterable.first()).thenReturn(null);

        Client result = clients.GetClientByNomPrenom("Inconnu", "Test");
        assertNull(result);
    }



    @Test
    public void testExisteTrue() throws Exception {
        Document foundDoc = new Document("nom", "Dupont");
        FindIterable<Document> mockFindIterable = mock(FindIterable.class);

        when(mockCollection.find(any(Document.class))).thenReturn(mockFindIterable);
        when(mockFindIterable.first()).thenReturn(foundDoc);

        assertTrue(clients.existe("Dupont"));
    }

    @Test
    public void testExisteFalse() throws Exception {
        FindIterable<Document> mockFindIterable = mock(FindIterable.class);

        when(mockCollection.find(any(Document.class))).thenReturn(mockFindIterable);
        when(mockFindIterable.first()).thenReturn(null);

        assertFalse(clients.existe("Inconnu"));
    }

    @Test
    public void testGetClientByIdOK() throws Exception {
        Document doc = new Document("idClient", 1).append("nom", "Dupont");
        FindIterable<Document> mockFindIterable = mock(FindIterable.class);

        when(mockCollection.find(any(Document.class))).thenReturn(mockFindIterable);
        when(mockFindIterable.first()).thenReturn(doc);

        Document result = clients.getClientById(1).toDocument();
        assertEquals("Dupont", result.getString("nom"));
    }

    @Test(expected = TpExeception.class)
    public void testGetClientByIdNotFound() throws Exception {
        FindIterable<Document> mockFindIterable = mock(FindIterable.class);
        MongoCursor<Document> mockCursor = mock(MongoCursor.class);

        when(mockCollection.find(any(Document.class))).thenReturn(mockFindIterable);
        when(mockFindIterable.iterator()).thenReturn(mockCursor);
        when(mockCursor.hasNext()).thenReturn(false);

        clients.getClientById(999);
    }

    @Test
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

    @Test(expected = TpExeception.class)
    public void testSupprimerClientNotFound() throws Exception {
        Clients spyClients = Mockito.spy(clients);
        Mockito.doReturn(null).when(spyClients).GetClientByNomPrenom("Inconnu", "Test");

        spyClients.supprimerClient("Test", "Inconnu");
    }

    @Test(expected = TpExeception.class)
    public void testSupprimerClientAvecReservations() throws Exception {
        Client mockClient = new Client();
        mockClient.setId(1);
        mockClient.setNom("Dupont");
        mockClient.setPrenom("Jean");

        Clients spyClients = Mockito.spy(clients);
        Mockito.doReturn(mockClient).when(spyClients).GetClientByNomPrenom("Dupont", "Jean");

        Reservations mockReservations = mock(Reservations.class);
        spyClients.reservations = mockReservations;
        when(mockReservations.clientADesReservations(1)).thenReturn(true);

        spyClients.supprimerClient("Jean", "Dupont");
    }
}
