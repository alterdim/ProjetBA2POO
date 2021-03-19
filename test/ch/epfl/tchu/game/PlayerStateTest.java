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
    void workWithAddedTickets(){
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
    void workWithAddedCard(){
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
    void workWithAddedCards(){
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

    @Test
    void workCanClaimRoute(){
        var map = new TestMap();
        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");
        Route route = new Route("rte", station1, station2, 3, Route.Level.OVERGROUND, Color.BLACK);


        var cards = new SortedBag.Builder<Card>()
                .add(Card.BLACK)
                .add(Card.BLACK)
                .add(Card.BLACK)
                .build();

        var tickets = new SortedBag.Builder<Ticket>()
                .add(map.LAU_BER)
                .add(map.BER_NEIGHBORS)
                .add(map.LAU_STG)
                .add(map.FR_NEIGHBORS)
                .build();

        var playerState = new  PlayerState(tickets, cards, List.of());
        assertTrue(playerState.canClaimRoute(route));
    }

    @Test
    void workOvergroundPossibleClaimCards(){
        var map = new TestMap();
        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");
        Route route = new Route("rte", station1, station2, 3, Route.Level.OVERGROUND, Color.BLACK);


        var cards = new SortedBag.Builder<Card>()
                .add(Card.BLACK)
                .add(Card.BLACK)
                .add(Card.BLACK)
                .build();

        var tickets = new SortedBag.Builder<Ticket>()
                .add(map.LAU_BER)
                .add(map.BER_NEIGHBORS)
                .add(map.LAU_STG)
                .add(map.FR_NEIGHBORS)
                .build();
        var playerState = new  PlayerState(tickets, cards, List.of());

        assertEquals(List.of(cards) ,playerState.possibleClaimCards(route));
    }

    @Test
    void workUndergroundPossibleClaimCards(){
        var map = new TestMap();
        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");
        Route route = new Route("rte", station1, station2, 3, Route.Level.UNDERGROUND, Color.BLACK);


        var cards = new SortedBag.Builder<Card>()
                .add(Card.BLACK)
                .add(Card.BLACK)
                .add(Card.BLACK)
                .add(Card.LOCOMOTIVE)
                .add(Card.LOCOMOTIVE)
                .add(Card.LOCOMOTIVE)
                .build();

        var tickets = new SortedBag.Builder<Ticket>()
                .add(map.LAU_BER)
                .add(map.BER_NEIGHBORS)
                .add(map.LAU_STG)
                .add(map.FR_NEIGHBORS)
                .build();
        var playerState = new  PlayerState(tickets, cards, List.of());



        assertEquals(List.of(new SortedBag.Builder<Card>()
                .add(Card.BLACK)
                .add(Card.BLACK)
                .add(Card.BLACK)
                .build(), new SortedBag.Builder<Card>()
                .add(Card.BLACK)
                .add(Card.BLACK)
                .add(Card.LOCOMOTIVE)
                .build(),new SortedBag.Builder<Card>()
                .add(Card.BLACK)
                .add(Card.LOCOMOTIVE)
                .add(Card.LOCOMOTIVE)
                .build(),new SortedBag.Builder<Card>()
                .add(Card.LOCOMOTIVE)
                .add(Card.LOCOMOTIVE)
                .add(Card.LOCOMOTIVE)
                .build()) ,playerState.possibleClaimCards(route));
    }

    @Test
    void failNumberPossibleAdditionalCards(){
        var map = new TestMap();
        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");
        Route route = new Route("rte", station1, station2, 3, Route.Level.OVERGROUND, Color.BLACK);


        var cards = new SortedBag.Builder<Card>()
                .add(Card.BLACK)
                .add(Card.BLACK)
                .add(Card.BLACK)
                .build();

        var tickets = new SortedBag.Builder<Ticket>()
                .add(map.LAU_BER)
                .add(map.BER_NEIGHBORS)
                .add(map.LAU_STG)
                .add(map.FR_NEIGHBORS)
                .build();

        var playerState = new  PlayerState(tickets, cards, List.of());


        var iniCards = new SortedBag.Builder<Card>()
                .add(Card.BLUE)
                .add(Card.YELLOW)
                .add(Card.YELLOW)
                .build();

        var drawnCards = new SortedBag.Builder<Card>()
                .add(Card.BLUE)
                .add(Card.GREEN)
                .add(Card.ORANGE)
                .build();

        assertThrows(IllegalArgumentException.class, ()->
                playerState.possibleAdditionalCards(0, iniCards, drawnCards)
        );
        assertThrows(IllegalArgumentException.class, ()->
                playerState.possibleAdditionalCards(4, iniCards, drawnCards)
        );
    }

    @Test
    void failIniPossibleAdditionalCards(){
        var map = new TestMap();
        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");
        Route route = new Route("rte", station1, station2, 3, Route.Level.OVERGROUND, Color.BLACK);


        var cards = new SortedBag.Builder<Card>()
                .add(Card.BLACK)
                .add(Card.BLACK)
                .add(Card.BLACK)
                .build();

        var tickets = new SortedBag.Builder<Ticket>()
                .add(map.LAU_BER)
                .add(map.BER_NEIGHBORS)
                .add(map.LAU_STG)
                .add(map.FR_NEIGHBORS)
                .build();

        var playerState = new  PlayerState(tickets, cards, List.of());


        var iniCards = new SortedBag.Builder<Card>()
                .add(Card.BLUE)
                .add(Card.YELLOW)
                .add(Card.GREEN)
                .build();

        var drawnCards = new SortedBag.Builder<Card>()
                .add(Card.BLUE)
                .add(Card.GREEN)
                .add(Card.ORANGE)
                .build();

        assertThrows(IllegalArgumentException.class, ()->
                playerState.possibleAdditionalCards(1, new SortedBag.Builder<Card>().build(), drawnCards)
        );
        assertThrows(IllegalArgumentException.class, ()->
                playerState.possibleAdditionalCards(3, iniCards, drawnCards)
        );
    }

    @Test
    void failCardNumPossibleAdditionalCards(){
        var map = new TestMap();
        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");
        Route route = new Route("rte", station1, station2, 3, Route.Level.OVERGROUND, Color.BLACK);


        var cards = new SortedBag.Builder<Card>()
                .add(Card.BLACK)
                .add(Card.BLACK)
                .add(Card.BLACK)
                .build();

        var tickets = new SortedBag.Builder<Ticket>()
                .add(map.LAU_BER)
                .add(map.BER_NEIGHBORS)
                .add(map.LAU_STG)
                .add(map.FR_NEIGHBORS)
                .build();

        var playerState = new  PlayerState(tickets, cards, List.of());


        var iniCards = new SortedBag.Builder<Card>()
                .add(Card.BLUE)
                .add(Card.YELLOW)
                .add(Card.YELLOW)
                .build();

        var drawnCards = new SortedBag.Builder<Card>()
                .add(Card.BLUE)
                .add(Card.GREEN)
                .add(Card.ORANGE)
                .add(Card.ORANGE)
                .build();

        assertThrows(IllegalArgumentException.class, ()->
                playerState.possibleAdditionalCards(1, iniCards, drawnCards)
        );
    }

    @Test
    void workPossibleAdditionalCards(){
        var map = new TestMap();
        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");
        Route route = new Route("rte", station1, station2, 3, Route.Level.OVERGROUND, Color.BLUE);


        var cardsplayer = new SortedBag.Builder<Card>()
                //TODO si un seul élément dans la main du joueur erreur si besoin de 1 nouvel carte
                .add(Card.ORANGE)
                .build();

        var tickets = new SortedBag.Builder<Ticket>()
                .add(map.LAU_BER)
                .add(map.BER_NEIGHBORS)
                .add(map.LAU_STG)
                .add(map.FR_NEIGHBORS)
                .build();

        var playerState = new  PlayerState(tickets, cardsplayer, List.of());


        var iniCards = new SortedBag.Builder<Card>()
                .add(Card.ORANGE)
                .build();

        var drawnCards = new SortedBag.Builder<Card>()
                .add(Card.ORANGE)
                .add(Card.GREEN)
                .add(Card.LOCOMOTIVE)
                .build();

//        System.out.println(playerState.possibleAdditionalCards(iniCards.size(), iniCards, drawnCards));
    }


    @Test
    void workPossibleAdditionalCards2(){
        var map = new TestMap();
        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");
        Route route = new Route("rte", station1, station2, 3, Route.Level.OVERGROUND, Color.BLUE);


        var cardsplayer = new SortedBag.Builder<Card>()
                .add(Card.ORANGE)
                .add(Card.ORANGE)
                .build();

        var tickets = new SortedBag.Builder<Ticket>()
                .add(map.LAU_BER)
                .add(map.BER_NEIGHBORS)
                .add(map.LAU_STG)
                .add(map.FR_NEIGHBORS)
                .build();

        var playerState = new  PlayerState(tickets, cardsplayer, List.of());


        var iniCards = new SortedBag.Builder<Card>()
                .add(Card.ORANGE)
                .build();

        var drawnCards = new SortedBag.Builder<Card>()
                .add(Card.ORANGE)
                .add(Card.GREEN)
                .add(Card.LOCOMOTIVE)
                .build();

//        System.out.println(playerState.possibleAdditionalCards(iniCards.size(), iniCards, drawnCards));
    }

    @Test
    void workPossibleAdditionalCards3(){
        var map = new TestMap();
        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");
        Route route = new Route("rte", station1, station2, 2, Route.Level.UNDERGROUND, null);


        var cardsplayer = new SortedBag.Builder<Card>()
                .add(Card.ORANGE)
                .add(Card.ORANGE)
                .add(Card.RED)
                .add(Card.LOCOMOTIVE)
                .build();

        var tickets = new SortedBag.Builder<Ticket>()
                .add(map.LAU_BER)
                .add(map.DE_IT)
                .add(map.LAU_STG)
                .add(map.FR_NEIGHBORS)
                .build();

        var playerState = new  PlayerState(tickets, cardsplayer, List.of());


        var iniCards = new SortedBag.Builder<Card>()
                .add(Card.ORANGE)
                .add(Card.LOCOMOTIVE)
                .build();

        var drawnCards = new SortedBag.Builder<Card>()
                .add(Card.YELLOW)
                .add(Card.BLUE)
                .add(Card.LOCOMOTIVE)
                .build();

//        System.out.println(playerState.possibleAdditionalCards(iniCards.size(), iniCards, drawnCards));
    }



    @Test
    void workWithClaimedRoute(){
        var map = new TestMap();
        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");
        Route route = new Route("rte", station1, station2, 3, Route.Level.OVERGROUND, Color.ORANGE);


        var cardsplayer = new SortedBag.Builder<Card>()
                .add(Card.ORANGE)
                .add(Card.ORANGE)
                .add(Card.ORANGE)
                .add(Card.LOCOMOTIVE)
                .build();

        var tickets = new SortedBag.Builder<Ticket>()
                .add(map.LAU_BER)
                .add(map.BER_NEIGHBORS)
                .add(map.LAU_STG)
                .add(map.FR_NEIGHBORS)
                .build();

        var cardsUsed = new SortedBag.Builder<Card>()
                .add(Card.ORANGE)
                .add(Card.ORANGE)
                .add(Card.ORANGE)
                .build();

        var playerState = new  PlayerState(tickets, cardsplayer, List.of());

        playerState = playerState.withClaimedRoute(route, cardsUsed);

        assertEquals(new SortedBag.Builder<Card>().add(Card.LOCOMOTIVE).build(), playerState.cards());
        assertEquals(List.of(route), playerState.routes());
    }

    @Test
    void workticketPoints() {
        var map = new TestMap();
        Route route1 = new Route("rte1", map.LAU, map.BER, 3, Route.Level.OVERGROUND, Color.BLUE);
        Route route2 = new Route("rte2", map.LAU, map.STG, 3, Route.Level.OVERGROUND, Color.BLUE);

        var cardsplayer = new SortedBag.Builder<Card>()
                .add(Card.ORANGE)
                .add(Card.ORANGE)
                .add(Card.ORANGE)
                .add(Card.LOCOMOTIVE)
                .build();

        var tickets = new SortedBag.Builder<Ticket>()
                .add(map.LAU_BER)//2
                .add(map.BER_NEIGHBORS)//6, 11, 8, 5
                .add(map.LAU_STG)//13
                .add(map.FR_NEIGHBORS)//5, 14, 11
                .build();

        var cardsUsed = new SortedBag.Builder<Card>()
                .add(Card.ORANGE)
                .add(Card.ORANGE)
                .add(Card.ORANGE)
                .build();

        var playerState = new PlayerState(tickets, cardsplayer, List.of());

        //-2;-5;-13;-5
        assertEquals(-25, playerState.ticketPoints());

        var playerState2 = new PlayerState(tickets, cardsplayer, List.of(route1));
        //+2-5-13-5
        assertEquals(-21, playerState2.ticketPoints());
        var playerState3 = new PlayerState(tickets, cardsplayer, List.of(route1, route2));
        //+2-5+13-5
        assertEquals(5, playerState3.ticketPoints());

    }






    //TODO end of tests

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
        public final Ticket DE_IT = new Ticket(DE1, IT1, 4);

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
