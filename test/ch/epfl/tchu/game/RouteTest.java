package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Fichier créé à 15:41 le 25/02/2021
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
class RouteTest {

    @Test
    void id() {
    }

    @Test
    void station1() {
    }

    @Test
    void station2() {
    }

    @Test
    void length() {
    }

    @Test
    void level() {
    }

    @Test
    void color() {
    }

    @Test
    void stations() {
    }

    @Test
    void stationOpposite() {
    }

    @Test
    void possibleClaimCards() {
        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");
        Route testRoute = new Route("Route", station1, station2, 3, Route.Level.OVERGROUND, null);
        System.out.println(testRoute.possibleClaimCards().toString());
    }
}