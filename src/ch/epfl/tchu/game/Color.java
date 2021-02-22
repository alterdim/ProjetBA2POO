package ch.epfl.tchu.game;

import java.util.List;

/**
 * Created at 13:24 on 22.02.2021
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
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
