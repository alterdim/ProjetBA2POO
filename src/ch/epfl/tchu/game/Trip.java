package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created at 13:53 on 22.02.2021
 *
 *  * @author Louis Gerard (296782)
 *  * @author Célien Muller (310777)
 */
public final class Trip {

    private final Station from;
    private final Station to;
    private final int points;

    public Trip(Station from, Station to, int points) {
        Preconditions.checkArgument(points>0);
        this.from= Objects.requireNonNull(from);
        this.to=Objects.requireNonNull(to);
        this.points=points;
    }

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

    public Station from(){
        return this.from;
    }

    public Station to(){
        return this.to;
    }

    public int points(){
        return this.points;
    }

    public int points(StationConnectivity stationConnectivity){
        return stationConnectivity.connected(from, to) ? this.points : -this.points;
    }

}
