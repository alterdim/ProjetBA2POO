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
    public static Trail longest(List<Route> routes) {
        List<Trail> trails = new ArrayList<>();
        for (Route r : routes) {
            trails.add(new Trail(List.of(r)));
        }
        while (!trails.isEmpty()) {
            List<Route> commonRoutes = new ArrayList<>();
            for (Trail t : trails) {
                //TODO
            }
        }


    }
}
