package ch.epfl.tchu.game;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Fichier créé à 17:32 le 25/02/2021
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public final class Trail {
    private final List<Route> routes;
    private final Station stationFrom;
    private final Station stationTo;

    private final int length;



    private Trail(Station stationFrom, Station stationTo, List<Route> routes, int length) {
        this.stationFrom=stationFrom;
        this.stationTo=stationTo;
        this.routes=routes;
        this.length=length;
    }

    public static Trail longest(List<Route> routes){
        if (routes.isEmpty()) return new Trail(null, null, null, 0);


        List<Trail> cs = new ArrayList<>();

        for (Route route:routes){
            cs.add(new Trail(route.station1(), route.station2(), List.of(route), route.length()));
            cs.add(new Trail(route.station2(), route.station1(), List.of(route), route.length()));
        }

        while (!cs.isEmpty()){
            List<Trail> cs2 = new ArrayList<>();
            for(Trail c:cs){

                List<Route> rs= new ArrayList<>(routes);

                rs.removeAll(c.routes);
                rs.removeIf(r -> !r.stations().contains(c.stationTo));

                for (Route r : rs){
                    List<Route> routeList = new ArrayList<>(c.routes);
                    routeList.add(r);
                    cs2.add(new Trail(c.stationFrom, r.stationOpposite(c.stationTo), routeList, c.length+r.length()));
                }

            }
            if(cs2.isEmpty()){
                return cs.get(0);
            }
            cs=cs2;
        }
        return null;
    }


    public Station station1() {
        return ((length()!=0) ? stationFrom: null);
    }

    public Station station2() {
        return ((length()!=0) ? stationTo: null);
    }

    public int length(){
        return length;
    }

    @Override
    public String toString() {
        //TODO
        return stationFrom.name()+" - "+stationTo.name();
    }
}