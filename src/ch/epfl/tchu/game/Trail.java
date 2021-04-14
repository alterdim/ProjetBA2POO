package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Chemin (trajet avec plusieurs routes passant par des gares)
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
        this.routes=List.copyOf(routes);
        this.length=length;
    }

    /**
     * Calcul tous les trajets possible
     * @param routes Liste de routes appartenant au joueur
     * @return  Retourne un trajet parmi les plus longs disponibles
     */
    public static Trail longest(List<Route> routes){
        Trail longestTrail = new Trail(null, null, List.of(), 0);

        List<Trail> cs = new ArrayList<>();

        for (Route route:routes){
            if (longestTrail.length < route.length()) longestTrail=new Trail(route.station1(), route.station2(), List.of(route), route.length());
            cs.add(new Trail(route.station1(), route.station2(), List.of(route), route.length()));
            cs.add(new Trail(route.station2(), route.station1(), List.of(route), route.length()));
        }

        while (!cs.isEmpty()){
            List<Trail> cs2 = new ArrayList<>();
            for(Trail c:cs){
                List<Route> rs= new ArrayList<>(routes);

                //Retire toutes les routes déjà utilisée
                rs.removeAll(c.routes);
                //Retire les routes qui ne permettent pas de prolonger le chemin
                rs.removeIf(r -> !r.stations().contains(c.stationTo));

                for (Route r : rs){
                    //Ajoute la route r au chemin
                    List<Route> routeList = new ArrayList<>(c.routes);
                    routeList.add(r);

                    int length=c.length+r.length();
                    Trail trail = new Trail(c.stationFrom, r.stationOpposite(c.stationTo), routeList, length);
                    //Ajoute a cs2 le nouveau chemin
                    cs2.add(trail);
                    if (longestTrail.length< length) longestTrail=trail;
                }
            }
            cs=cs2;
        }
        return longestTrail;
    }

    /**
     * Récupère la station de départ
     * @return station de départ du trajet
     */
    public Station station1() {
        return ((length()!=0) ? stationFrom: null);
    }

    /**
     * Récupère la station d'arrivée
     * @return station d'arrivée du trajet
     */
    public Station station2() {
        return ((length()!=0) ? stationTo: null);
    }

    public int length(){
        return length;
    }

    /**
     *
     * @return Le nom des gares où le trajet passe (séparée par " - " ainsi que la longueur total du trajet dans le format "(xx)"
     */
    @Override
    public String toString() {
        if (station1()==null || station2()==null){
            return "";
        }

        List<Station> stations=new ArrayList<>();
        stations.add(stationFrom);
        for(Route r:routes){
            stations.add(r.stationOpposite(stations.get(stations.size()-1)));
        }
        stations.remove(stations.size()-1);
        StringBuilder str=new StringBuilder();
        for (Station station:stations){
            str.append(station.name());
            str.append(" - ");
        }

//        stations.add(stationTo);
        str.append(stationTo.name());

        str.append(String.format(" (%d)", length()));

        return str.toString();
    }
}