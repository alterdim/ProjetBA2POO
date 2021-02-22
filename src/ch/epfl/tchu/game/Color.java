package ch.epfl.tchu.game;

import java.util.List;

/**
 * Created by cemuelle at 13:24 on 22.02.2021
 * celien.muller@epfl.ch
 */
public enum Color {
    BLACK,
    VIOLET,
    BLUE,
    GREEN,
    YELLOW,
    ORANGE,
    RED,
    WHITE;

    public static final List<Color> ALL = List.of(Color.values());
    public static final int COUNT = ALL.size();
}
