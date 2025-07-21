package tp.collections;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Before;
import org.junit.Test;
import tp.TpExeception;
import tp.bdd.Connexion;
import tp.objets.Chambre;
import tp.objets.Client;
import tp.objets.Reservation;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReservationsTest {
    private MongoDatabase mockDatabase;
    private Connexion mockConnexion;
    private MongoCollection<Document> mockCollection;
    private Chambres mockChambres;
    private Clients mockClients;
    private Reservations reservations;

    @Before
    public void setUp() throws Exception {
        mockConnexion = mock(Connexion.class);
        mockDatabase = mock(MongoDatabase.class);
        mockCollection = mock(MongoCollection.class);
        mockChambres = mock(Chambres.class);
        mockClients = mock(Clients.class);

        when(mockConnexion.getDatabase()).thenReturn(mockDatabase);
        when(mockDatabase.getCollection("Reservations")).thenReturn(mockCollection);

        reservations = new Reservations(mockConnexion, mockChambres, mockClients);
    }

    @Test
    public void testGetNextId() {
        FindIterable<Document> mockFind = mock(FindIterable.class);
        MongoCursor<Document> mockCursor = mock(MongoCursor.class);

        when(mockCollection.find()).thenReturn(mockFind);
        when(mockFind.iterator()).thenReturn(mockCursor);

        when(mockCursor.hasNext()).thenReturn(true, true, false);
        when(mockCursor.next())
                .thenReturn(new Document("idReservation", 1))
                .thenReturn(new Document("idReservation", 5));

        assertEquals(6, reservations.getNextId());
    }

    @Test
    public void testAjouterReservation() {
        Reservation mockReservation = mock(Reservation.class);
        when(mockReservation.toDocument()).thenReturn(new Document("idReservation", 1));

        reservations.ajouterReservation(mockReservation);

        verify(mockCollection, times(1)).insertOne(any(Document.class));
    }

    @Test
    public void testGetReservationsFuturesPourChambre() {
        int idChambre = 1;
        String today = LocalDate.now().toString();

        FindIterable<Document> mockFind = mock(FindIterable.class);
        MongoCursor<Document> mockCursor = mock(MongoCursor.class);

        when(mockCollection.find((Bson) any())).thenReturn(mockFind);
        when(mockFind.iterator()).thenReturn(mockCursor);

        when(mockCursor.hasNext()).thenReturn(true, false);
        when(mockCursor.next()).thenReturn(new Document("idReservation", 1).append("idChambre", idChambre).append("dateFin", today));

        List<Reservation> result = reservations.getReservationsFuturesPourChambre(idChambre);

        assertEquals(1, result.size());
    }

    @Test
    public void testGetReservationsPourChambreEntre() throws TpExeception {
        int idChambre = 1;

        FindIterable<Document> mockFind = mock(FindIterable.class);
        MongoCursor<Document> mockCursor = mock(MongoCursor.class);

        when(mockCollection.find((Bson) any())).thenReturn(mockFind);
        when(mockFind.iterator()).thenReturn(mockCursor);

        when(mockCursor.hasNext()).thenReturn(true, false);
        when(mockCursor.next()).thenReturn(new Document("idReservation", 1).append("idChambre", idChambre));

        List<Reservation> result = reservations.getReservationsPourChambreEntre(idChambre, "2025-01-01", "2025-12-31");

        assertEquals(1, result.size());
    }

    @Test
    public void testGetIdChambresReserveesEntre() {
        FindIterable<Document> mockFind = mock(FindIterable.class);
        MongoCursor<Document> mockCursor = mock(MongoCursor.class);

        when(mockCollection.find((Bson) any())).thenReturn(mockFind);
        when(mockFind.iterator()).thenReturn(mockCursor);

        when(mockCursor.hasNext()).thenReturn(true, false);
        when(mockCursor.next()).thenReturn(new Document("idChambre", 5));

        List<Integer> ids = reservations.getIdChambresReserveesEntre("2025-01-01", "2025-12-31");

        assertEquals(1, ids.size());
        assertEquals(5, (int) ids.get(0));
    }

    @Test
    public void testClientADesReservationsTrue() {
        FindIterable<Document> mockFind = mock(FindIterable.class);
        when(mockCollection.find((Bson) any())).thenReturn(mockFind);
        when(mockFind.first()).thenReturn(new Document("idClient", 1));

        assertTrue(reservations.clientADesReservations(1));
    }

    @Test
    public void testClientADesReservationsFalse() {
        FindIterable<Document> mockFind = mock(FindIterable.class);
        when(mockCollection.find((Bson) any())).thenReturn(mockFind);
        when(mockFind.first()).thenReturn(null);

        assertFalse(reservations.clientADesReservations(999));
    }

    @Test
    public void testClientAReservationEnCoursTrue() {
        FindIterable<Document> mockFind = mock(FindIterable.class);
        when(mockCollection.find((Bson) any())).thenReturn(mockFind);
        when(mockFind.first()).thenReturn(new Document("prenomClient", "John"));

        assertTrue(reservations.clientAReservationEnCours("John", "Doe"));
    }

    @Test
    public void testClientAReservationEnCoursFalse() {
        FindIterable<Document> mockFind = mock(FindIterable.class);
        when(mockCollection.find((Bson) any())).thenReturn(mockFind);
        when(mockFind.first()).thenReturn(null);

        assertFalse(reservations.clientAReservationEnCours("Jane", "Doe"));
    }

    @Test
    public void testGetChambresLibres() {
        Chambre c1 = new Chambre(1, "A", "Queen", 100);
        Chambre c2 = new Chambre(2, "B", "King", 200);

        when(mockChambres.getToutesChambres()).thenReturn(Arrays.asList(c1, c2));
        Reservations spy = spy(reservations);
        doReturn(Collections.singletonList(1)).when(spy).getIdChambresReserveesEntre(anyString(), anyString());

        List<Chambre> libres = spy.getChambresLibres("2025-01-01", "2025-01-31");

        assertEquals(1, libres.size());
        assertEquals(2, libres.get(0).getIdChambre());
    }
}
