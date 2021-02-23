package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *  Une route relie deux stations et possède plusieurs caractéristiques.
 *  Fichier créé à 14:28 le 23.02.2021
 *
 *  @author Louis Gerard (296782)
 *  @author Célien Muller (310777)
 *
 */

public final class Route {
    private final String id;
    private final Station station1;
    private final Station station2;
    private final int length;
    private final Level level;
    private final Color color;
    public enum Level {
        OVERGROUND,
        UNDERGROUND
    }

    public Route(String id, Station station1, Station station2, int length, Level level, Color color) {
        Preconditions.checkArgument(!station1.equals(station2));
        Preconditions.checkArgument(!(length > Constants.MAX_ROUTE_LENGTH));
        Preconditions.checkArgument(!(length < Constants.MIN_ROUTE_LENGTH));
        Objects.requireNonNull(level);
        Objects.requireNonNull(station1);
        Objects.requireNonNull(station2);
        Objects.requireNonNull(id);

        this.id = id;
        this.station1 = station1;
        this.station2 = station2;
        this.length = length;
        this.level = level;
        this.color = color;
    }

    public String id() {
        return id;
    }

    public Station station1() {
        return station1;
    }

    public Station station2() {
        return station2;
    }

    public int length() {
        return length;
    }

    public Level level() {
        return level;
    }

    public Color color() {
        return color;
    }

    public List<Station> stations() {
        List<Station> stationList = new ArrayList<>();
        stationList.add(station1);
        stationList.add(station2);
        return stationList;
    }

    public Station stationOpposite(Station station) {
        boolean isStation1 = station.equals(station1);
        boolean isStation2 = station.equals(station2);
        Preconditions.checkArgument(isStation1 || isStation2);
        if (isStation1) {
            return station2;
        }
        else {
            return station1;
        }
    }
}
