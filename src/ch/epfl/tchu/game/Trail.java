package ch.epfl.tchu.game;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Fichier créé à 17:32 le 25/02/2021
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public final class Trail {
    private final List<Route> routes;

    private Trail(List<Route> routes) {
        this.routes = routes;
    }

    public Station station1() {
        return routes.get(0).station1();
    }

    public Station station2() {
        return routes.get(0).station2();
    }

    public static Trail longest(List<Route> routes) {
        //TODO
        return null;

    }
}
