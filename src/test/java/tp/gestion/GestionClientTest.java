package tp.gestion;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import tp.TpExeception;
import tp.bdd.Connexion;
import tp.collections.Clients;
import tp.collections.Reservations;
import tp.objets.Client;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GestionClientTest {

    @Mock
    private Clients mockClients;
    @Mock
    private Reservations mockReservations;
    @Mock
    private Connexion mockConnexion;
    @Mock
    private MongoDatabase mockDatabase;
    @Mock
    private MongoCollection<Document> mockCollection;

    @InjectMocks
    private GestionClient gestionClient;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialiser tous les @Mock et @InjectMocks

        when(mockConnexion.getDatabase()).thenReturn(mockDatabase);
        when(mockDatabase.getCollection("Clients")).thenReturn(mockCollection);

        // gestionClient est injecté avec mockConnexion automatiquement par @InjectMocks
    }

    @Test
    public void testAjouterClient_OK() throws TpExeception {
        String nom = "Dupont";
        String prenom = "Jean";
        int age = 30;

        when(mockClients.existe(nom)).thenReturn(false);

        gestionClient.ajouterClient(nom, prenom, age);

        verify(mockClients, times(1)).ajouterClient(any(Client.class));
    }

//    @Test(expected = TpExeception.class)
//    public void testAjouterClient_ExisteDeja() throws TpExeception {
//        String nom = "Dupont";
//        String prenom = "Jean";
//        int age = 30;
//
//        when(mockClients.existe(nom)).thenReturn(true);
//
//        gestionClient.ajouterClient(nom, prenom, age);
//    }

    @Test
    public void testAfficherClients_OK() throws TpExeception {
        String nom = "Dupont";
        String prenom = "Jean";

        // Attention à l’ordre : Client(id, prenom, nom, age)
        Client client = new Client(1, prenom, nom, 30); // inversé ici
        when(mockClients.GetClientByNomPrenom(nom, prenom)).thenReturn(client);

        Client result = gestionClient.afficherClients(nom, prenom);

        assertNotNull(result);
        assertEquals(nom, result.getNom());
        assertEquals(prenom, result.getPrenom());
    }

    @Test(expected = TpExeception.class)
    public void testAfficherClients_NonTrouve() throws TpExeception {
        when(mockClients.GetClientByNomPrenom(anyString(), anyString())).thenReturn(null);

        gestionClient.afficherClients("Inconnu", "Personne");
    }

    @Test
    public void testSupprimerClient_OK() throws TpExeception {
        String nom = "Dupont";
        String prenom = "Jean";

        Client client = new Client(1, nom, prenom, 30);
        when(mockClients.GetClientByNomPrenom(prenom, nom)).thenReturn(client);

        gestionClient.supprimerClient(prenom, nom);

        verify(mockClients, times(1)).supprimerClient(prenom, nom);
    }

    @Test(expected = TpExeception.class)
    public void testSupprimerClient_NonTrouve() throws TpExeception {
        when(mockClients.GetClientByNomPrenom(anyString(), anyString())).thenReturn(null);

        gestionClient.supprimerClient("Jean", "Inconnu");
    }

    @Test
    public void testSupprimerClientAvecReservation() throws TpExeception {
        // Arrange
        String prenom = "Jean";
        String nom = "Dupont";
        Client mockClient = new Client(1, prenom, nom, 30);

        when(mockClients.GetClientByNomPrenom(prenom, nom)).thenReturn(mockClient);
        when(mockReservations.clientADesReservations(mockClient.getIdClient())).thenReturn(true);

        try {
            gestionClient.supprimerClient(prenom, nom);
            fail("Une TpExeception aurait dû être lancée");
        } catch (TpExeception e) {
            assertEquals("Impossible de supprimer : le client a des réservations existantes.", e.getMessage());
        }
    }
}
