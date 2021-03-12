package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Fichier créé à 18:29 le 04/03/2021
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
class InfoTest {

    @Test
    void workInfo(){
        new Info("Test");
    }

    @Test
    void cardName() {
        assertEquals("noire", Info.cardName(Card.BLACK, 1));
        assertEquals("noires", Info.cardName(Card.BLACK, 2));
        assertEquals("noires", Info.cardName(Card.BLACK, 0));
        assertEquals("noires", Info.cardName(Card.BLACK, 10));
    }

    private static final String alphabet = "abcdefghijklmnopqrstuvwxyz";
    private static String randomName(Random rng, int length) {
        var sb = new StringBuilder();
        for (int i = 0; i < length; i++)
            sb.append(alphabet.charAt(rng.nextInt(alphabet.length())));
        return sb.toString();
    }

    @Test
    void draw() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var name1 = randomName(rng, 1 + rng.nextInt(10));
            var name2 = randomName(rng, 1 + rng.nextInt(15));
            var points = rng.nextInt(15);

            assertEquals("\n"+name1+" et "+name2+" sont ex æqo avec "+points+" points !\n", Info.draw(List.of(name1, name2), points));
        }

    }

    @Test
    void willPlayFirst() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var name = randomName(rng, 1 + rng.nextInt(10));

            assertEquals(name+" jouera en premier.\n\n", new Info(name).willPlayFirst());
        }
    }

    @Test
    void keptTickets() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var name = randomName(rng, 1 + rng.nextInt(10));
            var number = rng.nextInt(15);
            assertEquals(name+" a gardé "+ number +" billet"+StringsFr.plural(number) +".\n", new Info(name).keptTickets(number));
        }
    }

    @Test
    void canPlay() {
    }

    @Test
    void drewTickets() {
    }

    @Test
    void drewBlindCard() {
    }

    @Test
    void drewVisibleCard() {
    }

    @Test
    void claimedRoute() {
        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");
        Route route = new Route("route", station1, station2, 3, Route.Level.OVERGROUND, Color.BLACK);
        Info info = new Info("monsieurCool");
        SortedBag.Builder<Card> builder = new SortedBag.Builder<>();
        builder.add(2, Card.ORANGE);
        builder.add(3, Card.LOCOMOTIVE);
//        System.out.println(info.claimedRoute(route, builder.build()));
    }

    @Test
    void attemptsTunnelClaim() {
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var name = randomName(rng, 1 + rng.nextInt(10));
            var number = rng.nextInt(15);
            Station station1 = new Station(1, "station1");
            Station station2 = new Station(2, "station2");
            Route route = new Route("route", station1, station2, 3, Route.Level.OVERGROUND, Color.BLACK);
            var cards = new SortedBag.Builder<Card>()
                    .add(Card.BLACK)
                    .add(Card.BLUE)
                    .add(Card.RED)
                    .add(Card.LOCOMOTIVE);
            Info info = new Info("monsieurCool");
            SortedBag.Builder<Card> builder = new SortedBag.Builder<>();
            builder.add(2, Card.ORANGE);
            builder.add(1, Card.YELLOW);
            builder.add(1, Card.BLACK);
            builder.add(1, Card.WHITE);
            builder.add(3, Card.LOCOMOTIVE);
//        System.out.println(info.attemptsTunnelClaim(route, builder.build()));
//            assertEquals(name+" tente de s'emparer du tunnel "+ station1.name() +StringsFr.EN_DASH_SEPARATOR+station2.name()+" au moyen de "+  +"!\n", new Info(name).keptTickets(number));
        }
    }

    @Test
    void drewAdditionalCards() {
        /*var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var name = randomName(rng, 1 + rng.nextInt(10));
            var number = rng.nextInt(15);
            var cards = new SortedBag.Builder<Card>()
                    .add(Card.BLACK)
                    .add(Card.BLUE)
                    .add(Card.RED)
                    .add(Card.LOCOMOTIVE)
                    .build();
            assertEquals("Les cartes supplémentaires sont "+cards.toString()+". ", new Info(name).drewAdditionalCards(cards, 0));
        }*/
        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");
        Route route = new Route("route", station1, station2, 3, Route.Level.OVERGROUND, Color.BLACK);


        Info info = new Info("monsieurCool");
        SortedBag.Builder<Card> builder = new SortedBag.Builder<>();
        builder.add(2, Card.ORANGE);
        builder.add(1, Card.YELLOW);
        builder.add(1, Card.BLACK);
        builder.add(1, Card.WHITE);
        builder.add(3, Card.LOCOMOTIVE);
        /*System.out.print(info.drewAdditionalCards(builder.build(), 3));
        System.out.print(info.drewAdditionalCards(builder.build(), 0));
        System.out.print(info.drewAdditionalCards(builder.build(), 1));*/
    }

    @Test
    void didNotClaimRoute() {
    }

    @Test
    void lastTurnBegins() {
    }

    @Test
    void getsLongestTrailBonus() {
        var rng = TestRandomizer.newRandom();
//        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {

            Station station1 = new Station(1, "station1");
            Station station2 = new Station(2, "station2");
            Route route = new Route("route", station1, station2, 3, Route.Level.OVERGROUND, Color.BLACK);

            var longesttrail = Trail.longest(List.of(route));


            var name = randomName(rng, 1 + rng.nextInt(10));
            var info = new Info(name);
            System.out.println(info.getsLongestTrailBonus(longesttrail));
            assertEquals(name +" reçoit un bonus de 10 points pour le plus long trajet (station1 – station2).\n", info.getsLongestTrailBonus(longesttrail));
//            assertEquals(name, new Info(name).getsLongestTrailBonus(longesttrail));
//            assertEquals(name+" reçoit un bonus de 10 points pour le plus long trajet ().\n" , new Info(name).getsLongestTrailBonus(longesttrail));
//            assertEquals(name+" reçoit un bonus de 10 points pour le plus long trajet ("+longesttrail.station1()+StringsFr.EN_DASH_SEPARATOR+longesttrail.station2()+").\n" , new Info(name).getsLongestTrailBonus(longesttrail));
//        }

    }

    @Test
    void test(){
        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");
        Route route = new Route("route", station1, station2, 3, Route.Level.OVERGROUND, Color.BLACK);

        var longesttrail = Trail.longest(List.of(route));
        var rng = TestRandomizer.newRandom();
        var name = randomName(rng, 1 + rng.nextInt(10));
        var info = new Info(name);

        String str = name +" reçoit un bonus de 10 points pour le plus long trajet (station1 – station2).\n";//name+" reçoit un bonus de 10 points pour le plus long trajet ("+longesttrail.station1()+StringsFr.EN_DASH_SEPARATOR+longesttrail.station2()+").\n"
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(name);
        stringBuilder.append(" reçoit un bonus de 10 points pour le plus long trajet (");
        stringBuilder.append(longesttrail.station1());
        stringBuilder.append(StringsFr.EN_DASH_SEPARATOR);
        stringBuilder.append(longesttrail.station2());
        stringBuilder.append(").\n");
        System.out.println(str);
//        assertEquals(str, "reçoit un bonus de 10 points pour le plus long trajet (station1 – station2).");
//        assertEquals(stringBuilder.toString(), info.getsLongestTrailBonus(longesttrail));
        assertEquals(" – ", StringsFr.EN_DASH_SEPARATOR);
    }

    @Test
    void won() {

    }

    @Test
    void printTest(){
        var info = new Info("playerName");
        SortedBag.Builder<Card> builder = new SortedBag.Builder<>();
        builder.add(2, Card.ORANGE);
        builder.add(1, Card.YELLOW);
        builder.add(1, Card.BLACK);
        builder.add(1, Card.WHITE);
        builder.add(3, Card.LOCOMOTIVE);

        var cardSortedBag = builder.build();

        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");
        Route route = new Route("route", station1, station2, 3, Route.Level.OVERGROUND, Color.BLACK);

        var longesttrail = Trail.longest(List.of(route));


//        System.out.print(info.willPlayFirst());
//        System.out.print(info.keptTickets(10));
//        System.out.print(info.canPlay());
//        System.out.print(info.drewTickets(10));
//        System.out.print(info.drewBlindCard());
//        System.out.print(info.drewVisibleCard(Card.BLACK));
//        System.out.print(info.claimedRoute(route, cardSortedBag));
//        System.out.print(info.attemptsTunnelClaim(route, cardSortedBag));
//        System.out.print(info.drewAdditionalCards(cardSortedBag, 5));
//        System.out.print(info.didNotClaimRoute(route));
//        System.out.print(info.lastTurnBegins(5));
//        System.out.print(info.getsLongestTrailBonus(longesttrail));
//        System.out.print(info.won(5,3));
    }

    @Test
    void generateCardString() {
        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");
        Route route = new Route("route", station1, station2, 3, Route.Level.OVERGROUND, Color.BLACK);
        SortedBag.Builder<Card> builder = new SortedBag.Builder<>();
        builder.add(2, Card.ORANGE);
        builder.add(2, Card.RED);
        builder.add(2, Card.WHITE);
        builder.add(3, Card.LOCOMOTIVE);
        Info info = new Info("madameCool");
//        System.out.println(info.attemptsTunnelClaim(route, builder.build()));
    }
}