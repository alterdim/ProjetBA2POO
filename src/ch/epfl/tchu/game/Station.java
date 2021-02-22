package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

/**
 * Created by cemuelle at 13:40 on 22.02.2021
 * celien.muller@epfl.ch
 */

public class Station {
    private final int id;
    private final String name;

    public Station(int id, String name) {
        Preconditions.checkArgument(id >= 0);
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public int id() {
        return id;
    }

    public String name() {
        return name;
    }

}
