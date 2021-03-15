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
//            System.out.println(info.getsLongestTrailBonus(longesttrail));
            String testString = "\n" + name +" reçoit un bonus de 10 points pour le plus long trajet" + " (" + station1.name() + StringsFr.EN_DASH_SEPARATOR + station2.name() + ").\n";
//            System.out.println(testString);
            assertEquals(testString,  info.getsLongestTrailBonus(longesttrail));
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
//        System.out.println(str);
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

    //Test given
    @Test
    void infoCardNameWorks() {
        var actualK1 = Info.cardName(Card.BLACK, 1);
        var expectedK1 = "noire";
        assertEquals(expectedK1, actualK1);
        var actualK9 = Info.cardName(Card.BLACK, 9);
        var expectedK9 = "noires";
        assertEquals(expectedK9, actualK9);

        var actualB1 = Info.cardName(Card.BLUE, 1);
        var expectedB1 = "bleue";
        assertEquals(expectedB1, actualB1);
        var actualB9 = Info.cardName(Card.BLUE, 9);
        var expectedB9 = "bleues";
        assertEquals(expectedB9, actualB9);

        var actualG1 = Info.cardName(Card.GREEN, 1);
        var expectedG1 = "verte";
        assertEquals(expectedG1, actualG1);
        var actualG9 = Info.cardName(Card.GREEN, 9);
        var expectedG9 = "vertes";
        assertEquals(expectedG9, actualG9);

        var actualO1 = Info.cardName(Card.ORANGE, 1);
        var expectedO1 = "orange";
        assertEquals(expectedO1, actualO1);
        var actualO9 = Info.cardName(Card.ORANGE, 9);
        var expectedO9 = "oranges";
        assertEquals(expectedO9, actualO9);

        var actualR1 = Info.cardName(Card.RED, 1);
        var expectedR1 = "rouge";
        assertEquals(expectedR1, actualR1);
        var actualR9 = Info.cardName(Card.RED, 9);
        var expectedR9 = "rouges";
        assertEquals(expectedR9, actualR9);

        var actualV1 = Info.cardName(Card.VIOLET, 1);
        var expectedV1 = "violette";
        assertEquals(expectedV1, actualV1);
        var actualV9 = Info.cardName(Card.VIOLET, 9);
        var expectedV9 = "violettes";
        assertEquals(expectedV9, actualV9);

        var actualW1 = Info.cardName(Card.WHITE, 1);
        var expectedW1 = "blanche";
        assertEquals(expectedW1, actualW1);
        var actualW9 = Info.cardName(Card.WHITE, 9);
        var expectedW9 = "blanches";
        assertEquals(expectedW9, actualW9);

        var actualY1 = Info.cardName(Card.YELLOW, 1);
        var expectedY1 = "jaune";
        assertEquals(expectedY1, actualY1);
        var actualY9 = Info.cardName(Card.YELLOW, 9);
        var expectedY9 = "jaunes";
        assertEquals(expectedY9, actualY9);

        var actualL1 = Info.cardName(Card.LOCOMOTIVE, 1);
        var expectedL1 = "locomotive";
        assertEquals(expectedL1, actualL1);
        var actualL9 = Info.cardName(Card.LOCOMOTIVE, 9);
        var expectedL9 = "locomotives";
        assertEquals(expectedL9, actualL9);
    }

    @Test
    void infoDrawWorks() {
        var actual = Info.draw(List.of("Ada", "Ada"), 17);
        var expected = "\nAda et Ada sont ex æqo avec 17 points !\n";
        assertEquals(expected, actual);
    }

    @Test
    void infoWillPlayFirstWorks() {
        var info = new Info("Niklaus");
        var actual = info.willPlayFirst();
        var expected = "Niklaus jouera en premier.\n\n";
        assertEquals(expected, actual);
    }

    @Test
    void infoKeptTicketsWorks() {
        var info = new Info("Edsger");

        var actual1 = info.keptTickets(1);
        var expected1 = "Edsger a gardé 1 billet.\n";
        assertEquals(expected1, actual1);

        var actual5 = info.keptTickets(5);
        var expected5 = "Edsger a gardé 5 billets.\n";
        assertEquals(expected5, actual5);
    }

    @Test
    void infoCanPlayWorks() {
        var info = new Info("Charles");

        var actual = info.canPlay();
        var expected = "\nC'est à Charles de jouer.\n";
        assertEquals(expected, actual);
    }

    @Test
    void infoDrewTicketsWorks() {
        var info = new Info("Linus");

        var actual1 = info.drewTickets(1);
        var expected1 = "Linus a tiré 1 billet...\n";
        assertEquals(expected1, actual1);

        var actual5 = info.drewTickets(5);
        var expected5 = "Linus a tiré 5 billets...\n";
        assertEquals(expected5, actual5);
    }

    @Test
    void infoDrewBlindCardWorks() {
        var info = new Info("Alan");

        var actual = info.drewBlindCard();
        var expected = "Alan a tiré une carte de la pioche.\n";
        assertEquals(expected, actual);
    }

    @Test
    void infoDrewVisibleCardWorks() {
        var info = new Info("John");

        var actual = info.drewVisibleCard(Card.GREEN);
        var expected = "John a tiré une carte verte visible.\n";
        assertEquals(expected, actual);
    }

    @Test
    void infoClaimedRouteWorks() {
        var info = new Info("Brian");

        var s1 = new Station(0, "Neuchâtel");
        var s2 = new Station(1, "Lausanne");

        var route1 = new Route("1", s1, s2, 1, Route.Level.OVERGROUND, Color.ORANGE);
        var actual1 = info.claimedRoute(route1, SortedBag.of(Card.ORANGE));
        var expected1 = "Brian a pris possession de la route Neuchâtel – Lausanne au moyen de 1 orange.\n";
        assertEquals(expected1, actual1);

        var route2 = new Route("1", s1, s2, 2, Route.Level.OVERGROUND, null);
        var actual2 = info.claimedRoute(route2, SortedBag.of(2, Card.RED));
        var expected2 = "Brian a pris possession de la route Neuchâtel – Lausanne au moyen de 2 rouges.\n";
        assertEquals(expected2, actual2);

        var route3 = new Route("1", s1, s2, 4, Route.Level.UNDERGROUND, null);
        var actual3 = info.claimedRoute(route3, SortedBag.of(4, Card.BLUE, 2, Card.LOCOMOTIVE));
        var expected3 = "Brian a pris possession de la route Neuchâtel – Lausanne au moyen de 4 bleues et 2 locomotives.\n";
        assertEquals(expected3, actual3);
    }

    @Test
    void infoAttemptsTunnelClaimWorks() {
        var info = new Info("Grace");

        var s1 = new Station(0, "Wassen");
        var s2 = new Station(1, "Coire");

        var route1 = new Route("1", s1, s2, 1, Route.Level.UNDERGROUND, Color.ORANGE);
        var actual1 = info.attemptsTunnelClaim(route1, SortedBag.of(Card.ORANGE));
        var expected1 = "Grace tente de s'emparer du tunnel Wassen – Coire au moyen de 1 orange !\n";
        assertEquals(expected1, actual1);

        var route2 = new Route("1", s1, s2, 2, Route.Level.UNDERGROUND, null);
        var actual2 = info.attemptsTunnelClaim(route2, SortedBag.of(2, Card.RED));
        var expected2 = "Grace tente de s'emparer du tunnel Wassen – Coire au moyen de 2 rouges !\n";
        assertEquals(expected2, actual2);

        var route3 = new Route("1", s1, s2, 4, Route.Level.UNDERGROUND, null);
        var actual3 = info.attemptsTunnelClaim(route3, SortedBag.of(4, Card.BLUE, 2, Card.LOCOMOTIVE));
        var expected3 = "Grace tente de s'emparer du tunnel Wassen – Coire au moyen de 4 bleues et 2 locomotives !\n";
        assertEquals(expected3, actual3);
    }

    @Test
    void infoDrewAdditionalCardsWorks() {
        var info = new Info("Margaret");

        var actual1 = info.drewAdditionalCards(SortedBag.of(3, Card.ORANGE), 0);
        var expected1 = "Les cartes supplémentaires sont 3 oranges. Elles n'impliquent aucun coût additionnel.\n";
        assertEquals(expected1, actual1);

        var actual2 = info.drewAdditionalCards(SortedBag.of(1, Card.WHITE, 2, Card.RED), 1);
        var expected2 = "Les cartes supplémentaires sont 2 rouges et 1 blanche. Elles impliquent un coût additionnel de 1 carte.\n";
        assertEquals(expected2, actual2);

        var actual3 = info.drewAdditionalCards(SortedBag.of(1, Card.YELLOW, 2, Card.GREEN), 2);
        var expected3 = "Les cartes supplémentaires sont 2 vertes et 1 jaune. Elles impliquent un coût additionnel de 2 cartes.\n";
        assertEquals(expected3, actual3);

        var actual4 = info.drewAdditionalCards(SortedBag.of(1, Card.VIOLET, 2, Card.LOCOMOTIVE), 3);
        var expected4 = "Les cartes supplémentaires sont 1 violette et 2 locomotives. Elles impliquent un coût additionnel de 3 cartes.\n";
        assertEquals(expected4, actual4);
    }

    @Test
    void infoDidNotClaimRouteWorks() {
        var info = new Info("Guido");
        var s1 = new Station(0, "Zernez");
        var s2 = new Station(1, "Klosters");

        var route = new Route("1", s1, s2, 4, Route.Level.UNDERGROUND, Color.ORANGE);
        var actual = info.didNotClaimRoute(route);
        var expected = "Guido n'a pas pu (ou voulu) s'emparer de la route Zernez – Klosters.\n";
        assertEquals(expected, actual);
    }

    @Test
    void infoLastTurnBeginsWorks() {
        var info = new Info("Martin");

        var actual1 = info.lastTurnBegins(0);
        var expected1 = "\nMartin n'a plus que 0 wagons, le dernier tour commence !\n";
        assertEquals(expected1, actual1);

        var actual2 = info.lastTurnBegins(1);
        var expected2 = "\nMartin n'a plus que 1 wagon, le dernier tour commence !\n";
        assertEquals(expected2, actual2);

        var actual3 = info.lastTurnBegins(2);
        var expected3 = "\nMartin n'a plus que 2 wagons, le dernier tour commence !\n";
        assertEquals(expected3, actual3);
    }

    @Test
    void infoGetsLongestTrailBonusWorks() {
        var info = new Info("Larry");

        var s1 = new Station(0, "Montreux");
        var s2 = new Station(1, "Montreux");

        var route = new Route("1", s1, s2, 1, Route.Level.UNDERGROUND, Color.ORANGE);
        var trail = Trail.longest(List.of(route));

        var actual = info.getsLongestTrailBonus(trail);
        var expected = "\nLarry reçoit un bonus de 10 points pour le plus long trajet (Montreux – Montreux).\n";
        assertEquals(expected, actual);
    }

    @Test
    void infoWonWorks() {
        var info = new Info("Bjarne");

        var actual1 = info.won(2, 1);
        var expected1 = "\nBjarne remporte la victoire avec 2 points, contre 1 point !\n";
        assertEquals(expected1, actual1);

        var actual2 = info.won(3, 2);
        var expected2 = "\nBjarne remporte la victoire avec 3 points, contre 2 points !\n";
        assertEquals(expected2, actual2);
    }
}