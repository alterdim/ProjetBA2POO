package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Créé à 13:53 le 22.02.2021
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 *
 * Classe trip. Caractérise deux stations qui sont reliés, utilisé pour la classe Ticket;
 */
public final class Trip {

    private final Station from;
    private final Station to;
    private final int points;

    /** Constructeur de Trip, un voyage qui part d'une station vers une autre et rapporte des points une fois complété.
     * @param from Station de départ.
     * @param to Station d'arrivée.
     * @param points Nombre de points accordés.
     */
    public Trip(Station from, Station to, int points) {
        Preconditions.checkArgument(points>0);
        this.from= Objects.requireNonNull(from);
        this.to=Objects.requireNonNull(to);
        this.points=points;
    }

    /**
     * @param from Stations de départ.
     * @param to Stations d'arrivée.
     * @param points Points accordés à chaque voyage.
     * @return Renvoie une liste de tous les voyages directs entre deux listes de stations.
     */
    public static List<Trip> all(List<Station> from, List<Station> to, int points){
        Preconditions.checkArgument(!from.isEmpty());
        Preconditions.checkArgument(!to.isEmpty());
        Preconditions.checkArgument(points>0);
        List<Trip> trips = new ArrayList<>();
        for (Station stationFrom: from) {
            for (Station stationTo : to) {
                trips.add(new Trip(stationFrom, stationTo, points));
            }
        }

        return trips;
    }

    /**
     * @return Renvoie la station de départ d'un voyage.
     */
    public Station from(){
        return this.from;
    }

    /**
     * @return Renvoie la station d'arrivée d'un voyage.
     */
    public Station to(){
        return this.to;
    }

    /**
     * @return Renvoie le nombre de points d'un voyage.
     */
    public int points(){
        return this.points;
    }

    /**
     * @param stationConnectivity La connectivité d'un joueur.
     * @return Renvoie le nombre de points calculés d'un voyage (le nombre de points si rempli, l'opposé sinon).
     */
    public int points(StationConnectivity stationConnectivity){
        return stationConnectivity.connected(from, to) ? this.points : -this.points;
    }

}
