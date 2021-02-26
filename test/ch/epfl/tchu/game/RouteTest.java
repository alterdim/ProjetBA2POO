package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Fichier créé à 15:41 le 25/02/2021
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
class RouteTest {

    @Test
    void FailOnSameStation(){
        Station station1 = new Station(1, "station1");
        assertThrows(IllegalArgumentException.class, ()-> new Route("Route", station1, station1, 3, Route.Level.OVERGROUND, null));
    }

    @Test
    void FailWhenLengthUnderLimit(){
        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");
        assertThrows(IllegalArgumentException.class, ()-> new Route("Route", station1, station2, Constants.MIN_ROUTE_LENGTH-1, Route.Level.OVERGROUND, null));
    }
    @Test
    void FailWhenLengthUpperLimit(){
        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");
        assertThrows(IllegalArgumentException.class, ()-> new Route("Route", station1, station2, Constants.MAX_ROUTE_LENGTH+1, Route.Level.OVERGROUND, null));
    }

    @Test
    void FailWhenNullId(){
        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");
        assertThrows(NullPointerException.class, ()-> new Route(null, station1, station2, 3, Route.Level.OVERGROUND, null));
    }

    @Test
    void FailWhenNullStation1(){
        Station station2 = new Station(2, "station2");
        assertThrows(NullPointerException.class, ()-> new Route("Route", null, station2, 3, Route.Level.OVERGROUND, null));
    }

    @Test
    void FailWhenNullStation2(){
        Station station1 = new Station(1, "station1");
        assertThrows(NullPointerException.class, ()-> new Route("Route", station1, null, 3, Route.Level.OVERGROUND, null));
    }

    @Test
    void FailWhenNullLevel(){
        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");
        assertThrows(NullPointerException.class, ()-> new Route("Route", station1, station2, 3,null, null));
    }



    @Test
    void WorkOnTrivialId() {
        String str="Route";

        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");
        Route route = new Route(str, station1, station2, 3, Route.Level.OVERGROUND, null);

        assertEquals(str, route.id());
    }


    //Vient de StationTest
    private static final String alphabet = "abcdefghijklmnopqrstuvwxyz";
    private static String randomName(Random rng, int length) {
        var sb = new StringBuilder();
        for (int i = 0; i < length; i++)
            sb.append(alphabet.charAt(rng.nextInt(alphabet.length())));
        return sb.toString();
    }

    @Test
    void WorkOnRandomId() {

        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var name = randomName(rng, 1 + rng.nextInt(10));
            Route route = new Route(name, station1, station2, 3, Route.Level.OVERGROUND, null);
            assertEquals(name, route.id());
        }
    }


    @Test
    void WorkOnStation1() {
        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");
        Route route = new Route("Route", station1, station2, 3, Route.Level.OVERGROUND, null);

        assertEquals(station1, route.station1());
    }

    @Test
    void WorkOnStation2() {
        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");
        Route route = new Route("Route", station1, station2, 3, Route.Level.OVERGROUND, null);

        assertEquals(station2, route.station2());
    }

    @Test
    void WorkOnTrivialLength() {
        int len=3;
        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");
        Route route = new Route("Route", station1, station2, len, Route.Level.OVERGROUND, null);

        assertEquals(len, route.length());
    }

    @Test
    void WorkOnAllLevel() {
        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");

        for (var lev : Route.Level.values()) {
            Route route = new Route("Route", station1, station2, 3,lev, null);
            assertEquals(lev, route.level());
        }
    }

    @Test
    void WorkOnNullColor() {
        Color col = null;
        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");
        Route route = new Route("Route", station1, station2, 3, Route.Level.OVERGROUND, col);

        assertEquals(col, route.color());
    }

    @Test
    void WorkOnAllColor() {
        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");
        for (var color : Color.values()) {
            Route route = new Route("Route", station1, station2, 3, Route.Level.OVERGROUND, color);
            assertEquals(color, route.color());
        }
    }

    @Test
    void WorkOnTrivialStations() {
        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");
        Route route = new Route("Route", station1, station2, 3, Route.Level.OVERGROUND, null);

        assertEquals(List.of(station1, station2), route.stations());
    }

    @Test
    void WorkOnStation1Opposite() {
        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");
        Route route = new Route("Route", station1, station2, 3, Route.Level.OVERGROUND, null);

        assertEquals(station2, route.stationOpposite(station1));
    }
    @Test
    void WorkOnStation2Opposite() {
        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");
        Route route = new Route("Route", station1, station2, 3, Route.Level.OVERGROUND, null);

        assertEquals(station1, route.stationOpposite(station2));
    }

    //TODO possibleClaimCards

    @Test
    void possibleClaimCards() {
        //TODO
        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");
        Route testRoute = new Route("Route", station1, station2, 3, Route.Level.OVERGROUND, null);
        System.out.println(testRoute.possibleClaimCards().toString());
    }

    @Test
    void additionalClaimCardsCountTest() {
        SortedBag.Builder builderB = new SortedBag.Builder();
        SortedBag<Card> bag1 = builderB.add(7, Card.BLUE).build();
        builderB = new SortedBag.Builder();
        SortedBag<Card> bag2 = builderB.add(3, Card.BLUE).build();
        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");
        Route testRoute = new Route("Route", station1, station2, 3, Route.Level.UNDERGROUND, null);
        assertEquals(testRoute.additionalClaimCardsCount(bag1, bag2), 3);

    }
    //TODO claimCardsCount Work

    @Test
    void FailOnOverGroundadditionalClaimCardsCount(){
        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");
        var route = new Route("Route", station1, station2, 3, Route.Level.OVERGROUND, null);
        assertThrows(IllegalArgumentException.class, ()-> route.additionalClaimCardsCount(SortedBag.of(3,Card.BLUE), SortedBag.of(3, Card.LOCOMOTIVE)));
    }

    @Test
    void FailOnNotGoogDrawnCardNumberadditionalClaimCardsCount(){
        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");
        var route = new Route("Route", station1, station2, 3, Route.Level.UNDERGROUND, null);
        assertThrows(IllegalArgumentException.class, ()-> route.additionalClaimCardsCount(SortedBag.of(3,Card.BLUE), SortedBag.of(4, Card.LOCOMOTIVE)));
    }

    @Test
    void  workOnClaimPoints(){
        //TODO ajouter test
    }
}