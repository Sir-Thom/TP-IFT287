package tp.gestion;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import tp.TpExeception;
import tp.collections.Chambres;
import tp.collections.Clients;
import tp.collections.Reservations;
import tp.objets.Chambre;
import tp.objets.Client;
import tp.objets.Reservation;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GestionReservationTest {

    private Reservations mockReservations;
    private Clients mockClients;
    private Chambres mockChambres;
    private GestionReservation gestionReservation;

    @Before
    public void setUp() {
        mockReservations = mock(Reservations.class);
        mockClients = mock(Clients.class);
        mockChambres = mock(Chambres.class);
        gestionReservation = new GestionReservation(mockReservations, mockClients, mockChambres);
    }

    @Test
    public void testReserver_OK() throws Exception {
        String prenom = "Jean";
        String nom = "Dupont";
        String nomChambre = "Suite";
        String dateDebut = LocalDate.now().plusDays(1).toString();
        String dateFin = LocalDate.now().plusDays(3).toString();

        Client client = new Client();
        client.setNom(nom);
        client.setPrenom(prenom);

        Chambre chambre = new Chambre();
        chambre.setIdChambre(10);
        chambre.setNomChambre(nomChambre);

        when(mockClients.GetClientByNomPrenom(nom, prenom)).thenReturn(client);
        when(mockChambres.getChambreByNom(nomChambre)).thenReturn(chambre);
        when(mockReservations.getReservationsPourChambreEntre(chambre.getIdChambre(), dateDebut, dateFin))
                .thenReturn(Collections.emptyList());
        when(mockReservations.getNextId()).thenReturn(42);

        gestionReservation.reserver(prenom, nom, nomChambre, dateDebut, dateFin);

        // Vérifie que la réservation a bien été ajoutée
        verify(mockReservations, times(1)).ajouterReservation(Mockito.argThat(reservation ->
                reservation.getPrenomClient().equals(prenom) &&
                        reservation.getNomClient().equals(nom) &&
                        reservation.getIdChambre() == chambre.getIdChambre() &&
                        reservation.getDateDebut().equals(dateDebut) &&
                        reservation.getDateFin().equals(dateFin) &&
                        reservation.getIdReservation() == 42
        ));
    }

    @Test(expected = TpExeception.class)
    public void testReserver_parametresNull() throws Exception {
        gestionReservation.reserver(null, "Nom", "Chambre", "2025-01-01", "2025-01-02");
    }

    @Test(expected = TpExeception.class)
    public void testReserver_dateDebutInvalide() throws Exception {
        gestionReservation.reserver("Jean", "Dupont", "Suite", "2025-13-01", "2025-01-02");
    }

    @Test(expected = TpExeception.class)
    public void testReserver_dateFinAvantDebut() throws Exception {
        gestionReservation.reserver("Jean", "Dupont", "Suite", "2025-01-03", "2025-01-02");
    }

    @Test(expected = TpExeception.class)
    public void testReserver_dateDebutPasse() throws Exception {
        String hier = LocalDate.now().minusDays(1).toString();
        String demain = LocalDate.now().plusDays(1).toString();

        gestionReservation.reserver("Jean", "Dupont", "Suite", hier, demain);
    }

    @Test(expected = TpExeception.class)
    public void testReserver_clientNonTrouve() throws Exception {
        when(mockClients.GetClientByNomPrenom(anyString(), anyString())).thenReturn(null);

        gestionReservation.reserver("Jean", "Dupont", "Suite",
                LocalDate.now().plusDays(1).toString(),
                LocalDate.now().plusDays(3).toString());
    }

    @Test(expected = TpExeception.class)
    public void testReserver_chambreNonTrouvee() throws Exception {
        Client client = new Client();
        when(mockClients.GetClientByNomPrenom(anyString(), anyString())).thenReturn(client);
        when(mockChambres.getChambreByNom(anyString())).thenReturn(null);

        gestionReservation.reserver("Jean", "Dupont", "Suite",
                LocalDate.now().plusDays(1).toString(),
                LocalDate.now().plusDays(3).toString());
    }

    @Test(expected = TpExeception.class)
    public void testReserver_conflitReservation() throws Exception {
        Client client = new Client();
        client.setNom("Dupont");
        client.setPrenom("Jean");

        Chambre chambre = new Chambre();
        chambre.setIdChambre(10);
        chambre.setNomChambre("Suite");

        when(mockClients.GetClientByNomPrenom("Dupont", "Jean")).thenReturn(client);
        when(mockChambres.getChambreByNom("Suite")).thenReturn(chambre);
        when(mockReservations.getReservationsPourChambreEntre(eq(chambre.getIdChambre()), anyString(), anyString()))
                .thenReturn(Collections.singletonList(new Reservation()));


        gestionReservation.reserver("Jean", "Dupont", "Suite",
                LocalDate.now().plusDays(1).toString(),
                LocalDate.now().plusDays(3).toString());
    }
}
