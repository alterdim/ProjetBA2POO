package ch.epfl.tchu.game;

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
        this.id = id;
        this.station1 = station1;
        this.station2 = station2;
        this.length = length;
        this.level = level;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public Station getStation1() {
        return station1;
    }

    public Station getStation2() {
        return station2;
    }

    public int getLength() {
        return length;
    }

    public Level getLevel() {
        return level;
    }

    public Color getColor() {
        return color;
    }
}
