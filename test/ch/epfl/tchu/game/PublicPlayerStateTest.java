package ch.epfl.tchu.game;

import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Créé le 08.03.2021 à 13:17
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
class PublicPlayerStateTest {
    @Test
    void FailOnNegativeTicketNumber(){
        Station A = new Station(1, "A");
        Station B = new Station(2, "B");
        Station C = new Station(3, "C");
        Station D = new Station(4, "D");
        Station E = new Station(5, "E");

        List<Route> routes = List.of(
                new Route("AB", A, B, 3, Route.Level.OVERGROUND, Color.BLACK),
                new Route("AC", A, C, 4, Route.Level.OVERGROUND, Color.BLACK),
                new Route("AD", A, D, 2, Route.Level.OVERGROUND, Color.BLACK),
                new Route("AE", A, E, 5, Route.Level.OVERGROUND, Color.BLACK),
                new Route("EB", E, B, 5, Route.Level.OVERGROUND, Color.BLACK),
                new Route("CD", C, D, 3, Route.Level.OVERGROUND, Color.BLACK)
        );

        assertThrows(IllegalArgumentException.class, ()-> new PublicPlayerState(-1, 10, routes));
    }

    @Test
    void FailOnNegativeCardNumber(){
        Station A = new Station(1, "A");
        Station B = new Station(2, "B");
        Station C = new Station(3, "C");
        Station D = new Station(4, "D");
        Station E = new Station(5, "E");

        List<Route> routes = List.of(
                new Route("AB", A, B, 3, Route.Level.OVERGROUND, Color.BLACK),
                new Route("AC", A, C, 4, Route.Level.OVERGROUND, Color.BLACK),
                new Route("AD", A, D, 2, Route.Level.OVERGROUND, Color.BLACK),
                new Route("AE", A, E, 5, Route.Level.OVERGROUND, Color.BLACK),
                new Route("EB", E, B, 5, Route.Level.OVERGROUND, Color.BLACK),
                new Route("CD", C, D, 3, Route.Level.OVERGROUND, Color.BLACK)
        );

        assertThrows(IllegalArgumentException.class, ()-> new PublicPlayerState(10, -1, routes));
    }

    @Test
    void WorkOnTicketCount(){
        Station A = new Station(1, "A");
        Station B = new Station(2, "B");
        Station C = new Station(3, "C");
        Station D = new Station(4, "D");
        Station E = new Station(5, "E");

        List<Route> routes = List.of(
                new Route("AB", A, B, 3, Route.Level.OVERGROUND, Color.BLACK),
                new Route("AC", A, C, 4, Route.Level.OVERGROUND, Color.BLACK),
                new Route("AD", A, D, 2, Route.Level.OVERGROUND, Color.BLACK),
                new Route("AE", A, E, 5, Route.Level.OVERGROUND, Color.BLACK),
                new Route("EB", E, B, 5, Route.Level.OVERGROUND, Color.BLACK),
                new Route("CD", C, D, 3, Route.Level.OVERGROUND, Color.BLACK)
        );

        var rng =TestRandomizer.newRandom();

        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            var x = rng.nextInt(i+1);
            var publicPlayerState=new PublicPlayerState(x, 10, routes);
            assertEquals(x, publicPlayerState.ticketCount());
        }
    }

    @Test
    void WorkOnroutes(){
        Station A = new Station(1, "A");
        Station B = new Station(2, "B");
        Station C = new Station(3, "C");
        Station D = new Station(4, "D");
        Station E = new Station(5, "E");

        List<Route> routes = List.of(
                new Route("AB", A, B, 3, Route.Level.OVERGROUND, Color.BLACK),
                new Route("AC", A, C, 4, Route.Level.OVERGROUND, Color.BLACK),
                new Route("AD", A, D, 2, Route.Level.OVERGROUND, Color.BLACK),
                new Route("AE", A, E, 5, Route.Level.OVERGROUND, Color.BLACK),
                new Route("EB", E, B, 5, Route.Level.OVERGROUND, Color.BLACK),
                new Route("CD", C, D, 3, Route.Level.OVERGROUND, Color.BLACK)
        );

        var playerState = new PublicPlayerState(10, 10, routes);
        assertEquals(routes,playerState.routes());
    }



}