package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Créé le 08.03.2021 à 15:08
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public class PlayerStateTest {
    @Test
    void WorkwithAddedTickets(){
        var map = new TestMap();
        var cards = new SortedBag.Builder<Card>()
                .add(Card.BLACK)
                .add(Card.BLUE)
                .add(Card.RED)
                .add(Card.LOCOMOTIVE)

                .build();
        var playerState = PlayerState.initial(cards);
        var tickets = new SortedBag.Builder<Ticket>()
                .add(map.LAU_BER)
                .add(map.BER_NEIGHBORS)
                .add(map.LAU_STG)
                .add(map.FR_NEIGHBORS)
                .build();
        assertEquals(tickets, playerState.withAddedTickets(tickets).tickets());
    }

    @Test
    void WorkwithAddedCard(){
        var map = new TestMap();
        var cards = new SortedBag.Builder<Card>()
                .add(Card.BLACK)
                .add(Card.BLUE)
                .add(Card.RED)
                .add(Card.LOCOMOTIVE)

                .build();
        var playerState = PlayerState.initial(cards);

        var cards2 = new SortedBag.Builder<Card>()
                .add(cards)
                .add(Card.GREEN)
                .build();

        assertEquals(cards2, playerState.withAddedCard(Card.GREEN).cards());
    }

    @Test
    void WorkwithAddedCards(){
        var map = new TestMap();
        var cards = new SortedBag.Builder<Card>()
                .add(Card.BLACK)
                .add(Card.BLUE)
                .add(Card.RED)
                .add(Card.LOCOMOTIVE)

                .build();
        var playerState = PlayerState.initial(cards);

        var cards2 = new SortedBag.Builder<Card>()
                .add(Card.GREEN)
                .add(Card.WHITE)
                .add(Card.YELLOW)
                .build();

        var cards3 = new SortedBag.Builder<Card>()
                .add(cards)
                .add(cards2)
                .build();

        assertEquals(cards3, playerState.withAddedCards(cards2).cards());
    }

    private static final class TestMap {
        // Stations - cities
        public final Station BER = new Station(0, "Berne");
        public final Station LAU = new Station(1, "Lausanne");
        public final Station STG = new Station(2, "Saint-Gall");

        // Stations - countries
        public final Station DE1 = new Station(3, "Allemagne");
        public final Station DE2 = new Station(4, "Allemagne");
        public final Station DE3 = new Station(5, "Allemagne");
        public final Station AT1 = new Station(6, "Autriche");
        public final Station AT2 = new Station(7, "Autriche");
        public final Station IT1 = new Station(8, "Italie");
        public final Station IT2 = new Station(9, "Italie");
        public final Station IT3 = new Station(10, "Italie");
        public final Station FR1 = new Station(11, "France");
        public final Station FR2 = new Station(12, "France");

        // Countries
        public final List<Station> DE = List.of(DE1, DE2, DE3);
        public final List<Station> AT = List.of(AT1, AT2);
        public final List<Station> IT = List.of(IT1, IT2, IT3);
        public final List<Station> FR = List.of(FR1, FR2);

        public final Ticket LAU_STG = new Ticket(LAU, STG, 13);
        public final Ticket LAU_BER = new Ticket(LAU, BER, 2);
        public final Ticket BER_NEIGHBORS = ticketToNeighbors(List.of(BER), 6, 11, 8, 5);
        public final Ticket FR_NEIGHBORS = ticketToNeighbors(FR, 5, 14, 11, 0);

        private Ticket ticketToNeighbors(List<Station> from, int de, int at, int it, int fr) {
            var trips = new ArrayList<Trip>();
            if (de != 0) trips.addAll(Trip.all(from, DE, de));
            if (at != 0) trips.addAll(Trip.all(from, AT, at));
            if (it != 0) trips.addAll(Trip.all(from, IT, it));
            if (fr != 0) trips.addAll(Trip.all(from, FR, fr));
            return new Ticket(trips);
        }
    }
}
