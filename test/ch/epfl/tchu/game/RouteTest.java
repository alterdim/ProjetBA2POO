package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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


    @Test
    void possibleClaimCards() {
        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");
        Route testRoute = new Route("Route", station1, station2, 3, Route.Level.OVERGROUND, null);
//        System.out.println(testRoute.possibleClaimCards().toString());
    }

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
    void workOnExamplePossibleClaimCards(){
        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");
        var route = new Route("Route", station1, station2, 2, Route.Level.UNDERGROUND, null);
//        System.out.println(route.possibleClaimCards().toString());
    }

    @Test
    void workOnAdditionalClaimsCard(){

    }

    @Test
    void workOnTrivialClaimPoints(){
        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");
        var route = new Route("Route", station1, station2, 2, Route.Level.UNDERGROUND, null);
        assertEquals(2, route.claimPoints());
    }
    @Test
    void workOnTrivial2ClaimPoints(){
        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");
        var route = new Route("Route", station1, station2, 6, Route.Level.UNDERGROUND, null);
        assertEquals(15, route.claimPoints());
    }
    @Test
    void workOnTrivial3ClaimPoints(){
        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");
        var route = new Route("Route", station1, station2, 1, Route.Level.UNDERGROUND, null);
        assertEquals(1, route.claimPoints());
    }

    private static final List<Color> COLORS =
            List.of(
                    Color.BLACK,
                    Color.VIOLET,
                    Color.BLUE,
                    Color.GREEN,
                    Color.YELLOW,
                    Color.ORANGE,
                    Color.RED,
                    Color.WHITE);
    private static final List<Card> CAR_CARDS =
            List.of(
                    Card.BLACK,
                    Card.VIOLET,
                    Card.BLUE,
                    Card.GREEN,
                    Card.YELLOW,
                    Card.ORANGE,
                    Card.RED,
                    Card.WHITE);

    @Test
    void routeConstructorFailsWhenBothStationsAreEqual() {
        var s = new Station(0, "Lausanne");
        assertThrows(IllegalArgumentException.class, () -> {
            new Route("id", s, s, 1, Route.Level.OVERGROUND, Color.BLACK);
        });
    }

    @Test
    void routeConstructorFailsWhenLengthIsInvalid() {
        var s1 = new Station(0, "Lausanne");
        var s2 = new Station(1, "EPFL");
        assertThrows(IllegalArgumentException.class, () -> {
            new Route("id", s1, s2, 0, Route.Level.OVERGROUND, Color.BLACK);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Route("id", s1, s2, 7, Route.Level.OVERGROUND, Color.BLACK);
        });
    }

    @Test
    void routeConstructorFailsWhenIdIsNull() {
        var s1 = new Station(0, "Lausanne");
        var s2 = new Station(1, "EPFL");
        assertThrows(NullPointerException.class, () -> {
            new Route(null, s1, s2, 1, Route.Level.OVERGROUND, Color.BLACK);
        });
    }

    @Test
    void routeConstructorFailsWhenOneStationIsNull() {
        var s = new Station(0, "EPFL");
        assertThrows(NullPointerException.class, () -> {
            new Route("id", null, s, 1, Route.Level.OVERGROUND, Color.BLACK);
        });
        assertThrows(NullPointerException.class, () -> {
            new Route("id", s, null, 1, Route.Level.OVERGROUND, Color.BLACK);
        });
    }

    @Test
    void routeConstructorFailsWhenLevelIsNull() {
        var s1 = new Station(0, "Lausanne");
        var s2 = new Station(1, "EPFL");
        assertThrows(NullPointerException.class, () -> {
            new Route("id", s1, s2, 1, null, Color.BLACK);
        });
    }

    @Test
    void routeIdReturnsId() {
        var s1 = new Station(0, "Lausanne");
        var s2 = new Station(1, "EPFL");
        var routes = new Route[100];
        for (int i = 0; i < routes.length; i++)
            routes[i] = new Route("id" + i, s1, s2, 1, Route.Level.OVERGROUND, Color.BLACK);
        for (int i = 0; i < routes.length; i++)
            assertEquals("id" + i, routes[i].id());
    }

    @Test
    void routeStation1And2ReturnStation1And2() {
        var rng = TestRandomizer.newRandom();
        var stations = new Station[100];
        for (int i = 0; i < stations.length; i++)
            stations[i] = new Station(i, "Station " + i);
        var routes = new Route[100];
        for (int i = 0; i < stations.length; i++) {
            var s1 = stations[i];
            var s2 = stations[(i + 1) % 100];
            var l = 1 + rng.nextInt(6);
            routes[i] = new Route("r" + i, s1, s2, l, Route.Level.OVERGROUND, Color.RED);
        }
        for (int i = 0; i < stations.length; i++) {
            var s1 = stations[i];
            var s2 = stations[(i + 1) % 100];
            var r = routes[i];
            assertEquals(s1, r.station1());
            assertEquals(s2, r.station2());
        }
    }

    @Test
    void routeLengthReturnsLength() {
        var s1 = new Station(0, "Lausanne");
        var s2 = new Station(1, "EPFL");
        var id = "id";
        var routes = new Route[6];
        for (var l = 1; l <= 6; l++)
            routes[l - 1] = new Route(id, s1, s2, l, Route.Level.OVERGROUND, Color.BLACK);
        for (var l = 1; l <= 6; l++)
            assertEquals(l, routes[l - 1].length());

    }

    @Test
    void routeLevelReturnsLevel() {
        var s1 = new Station(0, "Lausanne");
        var s2 = new Station(1, "EPFL");
        var id = "id";
        var ro = new Route(id, s1, s2, 1, Route.Level.OVERGROUND, Color.BLACK);
        var ru = new Route(id, s1, s2, 1, Route.Level.UNDERGROUND, Color.BLACK);
        assertEquals(Route.Level.OVERGROUND, ro.level());
        assertEquals(Route.Level.UNDERGROUND, ru.level());
    }

    @Test
    void routeColorReturnsColor() {
        var s1 = new Station(0, "Lausanne");
        var s2 = new Station(1, "EPFL");
        var id = "id";
        var routes = new Route[8];
        for (var c : COLORS)
            routes[c.ordinal()] = new Route(id, s1, s2, 1, Route.Level.OVERGROUND, c);
        for (var c : COLORS)
            assertEquals(c, routes[c.ordinal()].color());
        var r = new Route(id, s1, s2, 1, Route.Level.OVERGROUND, null);
        assertNull(r.color());
    }

    @Test
    void routeStationsReturnsStations() {
        var rng = TestRandomizer.newRandom();
        var stations = new Station[100];
        for (int i = 0; i < stations.length; i++)
            stations[i] = new Station(i, "Station " + i);
        var routes = new Route[100];
        for (int i = 0; i < stations.length; i++) {
            var s1 = stations[i];
            var s2 = stations[(i + 1) % 100];
            var l = 1 + rng.nextInt(6);
            routes[i] = new Route("r" + i, s1, s2, l, Route.Level.OVERGROUND, Color.RED);
        }
        for (int i = 0; i < stations.length; i++) {
            var s1 = stations[i];
            var s2 = stations[(i + 1) % 100];
            assertEquals(List.of(s1, s2), routes[i].stations());
        }
    }

    @Test
    void routeStationOppositeFailsWithInvalidStation() {
        var s1 = new Station(0, "Lausanne");
        var s2 = new Station(1, "EPFL");
        var s3 = new Station(1, "EPFL");
        var r = new Route("id", s1, s2, 1, Route.Level.OVERGROUND, Color.RED);
        assertThrows(IllegalArgumentException.class, () -> {
            r.stationOpposite(s3);
        });
    }

    @Test
    void routeStationOppositeReturnsOppositeStation() {
        var rng = TestRandomizer.newRandom();
        var stations = new Station[100];
        for (int i = 0; i < stations.length; i++)
            stations[i] = new Station(i, "Station " + i);
        var routes = new Route[100];
        for (int i = 0; i < stations.length; i++) {
            var s1 = stations[i];
            var s2 = stations[(i + 1) % 100];
            var l = 1 + rng.nextInt(6);
            routes[i] = new Route("r" + i, s1, s2, l, Route.Level.OVERGROUND, Color.RED);
        }
        for (int i = 0; i < stations.length; i++) {
            var s1 = stations[i];
            var s2 = stations[(i + 1) % 100];
            var r = routes[i];
            assertEquals(s1, r.stationOpposite(s2));
            assertEquals(s2, r.stationOpposite(s1));
        }
    }

    @Test
    void routePossibleClaimCardsWorksForOvergroundColoredRoute() {
        var s1 = new Station(0, "Lausanne");
        var s2 = new Station(1, "EPFL");
        var id = "id";
        for (var i = 0; i < COLORS.size(); i++) {
            var color = COLORS.get(i);
            var card = CAR_CARDS.get(i);
            for (var l = 1; l <= 6; l++) {
                var r = new Route(id, s1, s2, l, Route.Level.OVERGROUND, color);
                assertEquals(List.of(SortedBag.of(l, card)), r.possibleClaimCards());
            }
        }
    }

    @Test
    void routePossibleClaimCardsWorksOnOvergroundNeutralRoute() {
        var s1 = new Station(0, "Lausanne");
        var s2 = new Station(1, "EPFL");
        var id = "id";
        for (var l = 1; l <= 6; l++) {
            var r = new Route(id, s1, s2, l, Route.Level.OVERGROUND, null);
            var expected = List.of(
                    SortedBag.of(l, Card.BLACK),
                    SortedBag.of(l, Card.VIOLET),
                    SortedBag.of(l, Card.BLUE),
                    SortedBag.of(l, Card.GREEN),
                    SortedBag.of(l, Card.YELLOW),
                    SortedBag.of(l, Card.ORANGE),
                    SortedBag.of(l, Card.RED),
                    SortedBag.of(l, Card.WHITE));
            assertEquals(expected, r.possibleClaimCards());
        }
    }

    @Test
    void routePossibleClaimCardsWorksOnUndergroundColoredRoute() {
        var s1 = new Station(0, "Lausanne");
        var s2 = new Station(1, "EPFL");
        var id = "id";
        for (var i = 0; i < COLORS.size(); i++) {
            var color = COLORS.get(i);
            var card = CAR_CARDS.get(i);
            for (var l = 1; l <= 6; l++) {
                var r = new Route(id, s1, s2, l, Route.Level.UNDERGROUND, color);

                var expected = new ArrayList<SortedBag<Card>>();
                for (var locomotives = 0; locomotives <= l; locomotives++) {
                    var cars = l - locomotives;
                    expected.add(SortedBag.of(cars, card, locomotives, Card.LOCOMOTIVE));
                }
                assertEquals(expected, r.possibleClaimCards());
            }
        }
    }

    @Test
    void routePossibleClaimCardsWorksOnUndergroundNeutralRoute() {
        var s1 = new Station(0, "Lausanne");
        var s2 = new Station(1, "EPFL");
        var id = "id";
        for (var l = 1; l <= 6; l++) {
            var r = new Route(id, s1, s2, l, Route.Level.UNDERGROUND, null);

            var expected = new ArrayList<SortedBag<Card>>();
            for (var locomotives = 0; locomotives <= l; locomotives++) {
                var cars = l - locomotives;
                if (cars == 0)
                    expected.add(SortedBag.of(locomotives, Card.LOCOMOTIVE));
                else {
                    for (var card : CAR_CARDS)
                        expected.add(SortedBag.of(cars, card, locomotives, Card.LOCOMOTIVE));
                }
            }
            assertEquals(expected, r.possibleClaimCards());
        }
    }

    @Test
    void routeAdditionalClaimCardsCountWorksWithColoredCardsOnly() {
        var s1 = new Station(0, "Lausanne");
        var s2 = new Station(1, "EPFL");
        var id = "id";

        for (var l = 1; l <= 6; l++) {
            for (var color : COLORS) {
                var matchingCard = CAR_CARDS.get(color.ordinal());
                var nonMatchingCard = color == Color.BLACK
                        ? Card.WHITE
                        : Card.BLACK;
                var claimCards = SortedBag.of(l, matchingCard);
                var r = new Route(id, s1, s2, l, Route.Level.UNDERGROUND, color);
                for (var m = 0; m <= 3; m++) {
                    for (var locomotives = 0; locomotives <= m; locomotives++) {
                        var drawnB = new SortedBag.Builder<Card>();
                        drawnB.add(locomotives, Card.LOCOMOTIVE);
                        drawnB.add(m - locomotives, matchingCard);
                        drawnB.add(3 - m, nonMatchingCard);
                        var drawn = drawnB.build();
                        assertEquals(m, r.additionalClaimCardsCount(claimCards, drawn));
                    }
                }
            }
        }
    }

    @Test
    void routeAdditionalClaimCardsCountWorksWithLocomotivesOnly() {
        var s1 = new Station(0, "Lausanne");
        var s2 = new Station(1, "EPFL");
        var id = "id";

        for (var l = 1; l <= 6; l++) {
            for (var color : COLORS) {
                var matchingCard = CAR_CARDS.get(color.ordinal());
                var nonMatchingCard = color == Color.BLACK
                        ? Card.WHITE
                        : Card.BLACK;
                var claimCards = SortedBag.of(l, Card.LOCOMOTIVE);
                var r = new Route(id, s1, s2, l, Route.Level.UNDERGROUND, color);
                for (var m = 0; m <= 3; m++) {
                    for (var locomotives = 0; locomotives <= m; locomotives++) {
                        var drawnB = new SortedBag.Builder<Card>();
                        drawnB.add(locomotives, Card.LOCOMOTIVE);
                        drawnB.add(m - locomotives, matchingCard);
                        drawnB.add(3 - m, nonMatchingCard);
                        var drawn = drawnB.build();
                        assertEquals(locomotives, r.additionalClaimCardsCount(claimCards, drawn));
                    }
                }
            }
        }
    }

    @Test
    void routeAdditionalClaimCardsCountWorksWithMixedCards() {
        var s1 = new Station(0, "Lausanne");
        var s2 = new Station(1, "EPFL");
        var id = "id";

        for (var l = 2; l <= 6; l++) {
            for (var color : COLORS) {
                var matchingCard = CAR_CARDS.get(color.ordinal());
                var nonMatchingCard = color == Color.BLACK
                        ? Card.WHITE
                        : Card.BLACK;
                for (var claimLoc = 1; claimLoc < l; claimLoc++) {
                    var claimCards = SortedBag.of(
                            l - claimLoc, matchingCard,
                            claimLoc, Card.LOCOMOTIVE);
                    var r = new Route(id, s1, s2, l, Route.Level.UNDERGROUND, color);
                    for (var m = 0; m <= 3; m++) {
                        for (var locomotives = 0; locomotives <= m; locomotives++) {
                            var drawnB = new SortedBag.Builder<Card>();
                            drawnB.add(locomotives, Card.LOCOMOTIVE);
                            drawnB.add(m - locomotives, matchingCard);
                            drawnB.add(3 - m, nonMatchingCard);
                            var drawn = drawnB.build();
                            assertEquals(m, r.additionalClaimCardsCount(claimCards, drawn));
                        }
                    }
                }
            }
        }
    }

    @Test
    void routeClaimPointsReturnsClaimPoints() {
        var expectedClaimPoints =
                List.of(Integer.MIN_VALUE, 1, 2, 4, 7, 10, 15);
        var s1 = new Station(0, "Lausanne");
        var s2 = new Station(1, "EPFL");
        var id = "id";
        for (var l = 1; l <= 6; l++) {
            var r = new Route(id, s1, s2, l, Route.Level.OVERGROUND, Color.BLACK);
            assertEquals(expectedClaimPoints.get(l), r.claimPoints());
        }
    }
}