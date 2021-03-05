package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Color;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Station;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Fichier créé à 18:29 le 04/03/2021
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
class InfoTest {

    @Test
    void cardName() {
    }

    @Test
    void draw() {
    }

    @Test
    void willPlayFirst() {
    }

    @Test
    void keptTickets() {
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
        System.out.println(info.claimedRoute(route, builder.build()));
    }

    @Test
    void attemptsTunnelClaim() {
    }

    @Test
    void drewAdditionalCards() {
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
        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");
        Route route = new Route("route", station1, station2, 3, Route.Level.OVERGROUND, Color.BLACK);
        SortedBag.Builder<Card> builder = new SortedBag.Builder<>();
        builder.add(2, Card.ORANGE);
        builder.add(2, Card.RED);
        builder.add(2, Card.WHITE);
        builder.add(3, Card.LOCOMOTIVE);
        Info info = new Info("madameCool");
        System.out.println(info.attemptsTunnelClaim(route, builder.build()));
    }
}