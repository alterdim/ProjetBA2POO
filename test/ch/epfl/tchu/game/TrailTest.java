package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Créé le 28.02.2021 à 11:49
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
class TrailTest {
    @Test
    void WorkOnTrivialLongest(){
        Station st1 = new Station(1, "ST1");
        Station st2 = new Station(2, "ST2");
        Station st3 = new Station(3, "ST3");
        Station st4 = new Station(4, "ST4");
        Station st5 = new Station(5, "ST5");
        Station st6 = new Station(6, "ST6");

        List<Route> routes = List.of(new Route("ST1_ST2", st1, st2, 2, Route.Level.OVERGROUND, Color.VIOLET) ,
                new Route("ST2_ST4", st2, st4, 2, Route.Level.OVERGROUND, Color.VIOLET),
                new Route("ST4_ST5", st4, st5, 2, Route.Level.OVERGROUND, Color.VIOLET)
        );
        assertEquals("ST1 - ST2 - ST4 - ST5 (6)" ,Trail.longest(routes).toString());
    }

    private static final Station NEU = new Station(19, "Neuchâtel");
    private static final Station YVE = new Station(31, "Yverdon");
    private static final Station SOL = new Station(26, "Soleure");
    private static final Station BER = new Station(3, "Berne");
    private static final Station FRI = new Station(9, "Fribourg");
    private static final Station LUC = new Station(16, "Lucerne");



    /*@Test
    void WorkOnExampleLongest(){
        List<Route> routes = List.of(
                new Route("NEU_SOL_1", NEU, SOL, 4, Route.Level.OVERGROUND, Color.GREEN),
                new Route("NEU_YVE_1", NEU, YVE, 2, Route.Level.OVERGROUND, Color.BLACK),
                new Route("BER_NEU_1", BER, NEU, 2, Route.Level.OVERGROUND, Color.RED),
                new Route("BER_SOL_1", BER, SOL, 2, Route.Level.OVERGROUND, Color.BLACK),
                new Route("BER_LUC_1", BER, LUC, 4, Route.Level.OVERGROUND, null),
                new Route("BER_LUC_2", BER, LUC, 4, Route.Level.OVERGROUND, null),
                new Route("BER_FRI_1", BER, FRI, 1, Route.Level.OVERGROUND, Color.ORANGE),
                new Route("BER_FRI_2", BER, FRI, 1, Route.Level.OVERGROUND, Color.YELLOW)

        );


        assertEquals("Lucerne - Fribourg" ,Trail.longest(routes).toString());
    }*/

    @Test
    void WorkOnExampleLongest(){
        List<Route> routes = List.of(
                new Route("A", NEU, YVE, 2, Route.Level.OVERGROUND, Color.BLACK),
                new Route("B", NEU, SOL, 4, Route.Level.OVERGROUND, Color.GREEN),
                new Route("C", BER, NEU, 2, Route.Level.OVERGROUND, Color.RED),
                new Route("D", BER, SOL, 2, Route.Level.OVERGROUND, Color.BLACK),
                new Route("E", BER, LUC, 4, Route.Level.OVERGROUND, null),
                new Route("F", BER, FRI, 1, Route.Level.OVERGROUND, Color.ORANGE)
        );


        assertEquals("Lucerne - Berne - Neuchâtel - Soleure - Berne - Fribourg (13)" ,Trail.longest(routes).toString());
    }

   @Test
    void WorkOnNullListLongest(){
        Station st1 = new Station(1, "ST1");
        Station st2 = new Station(2, "ST2");
        Station st3 = new Station(3, "ST3");
        Station st4 = new Station(4, "ST4");
        Station st5 = new Station(5, "ST5");
        Station st6 = new Station(6, "ST6");

        List<Route> routes = new ArrayList<>();
        assertEquals("" ,Trail.longest(routes).toString());
    }


}