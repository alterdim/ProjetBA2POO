package ch.epfl.tchu.game;

import java.util.List;

/**
 * Créé à 13:24 le 22.02.2021
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 *
 * Enum qui contient les couleurs.
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

    /**
     * Liste des couleurs.
     */
    public static final List<Color> ALL = List.of(Color.values());
    /**
     * Nombre de couleurs différentes.
     */
    public static final int COUNT = ALL.size();
}
