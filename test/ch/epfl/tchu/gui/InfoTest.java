package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Color;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Station;
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
        //TODO
        var rng = TestRandomizer.newRandom();
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var name = randomName(rng, 1 + rng.nextInt(10));
            var number = rng.nextInt(15);
            var cards = new SortedBag.Builder<Card>()
                    .add(Card.BLACK)
                    .add(Card.BLUE)
                    .add(Card.RED)
                    .add(Card.LOCOMOTIVE)
                    .build();
            assertEquals(name+" a gardé "+ number +" billet"+StringsFr.plural(number) +".\n", new Info(name).keptTickets(number));
        }
    }

    @Test
    void drewAdditionalCards() {
        var rng = TestRandomizer.newRandom();
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
        }
    }

    @Test
    void didNotClaimRoute() {
    }

    @Test
    void lastTurnBegins() {
    }

    @Test
    void getsLongestTrailBonus() {
    }

    @Test
    void won() {
    }

    @Test
    void generateCardString() {
        //TODO
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