package ch.epfl.tchu.game;

import java.util.Arrays;
import java.util.List;

/**
 * Créé à 13:36 le 22.02.2021
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public enum Card {
    BLACK(Color.BLACK),
    VIOLET(Color.VIOLET),
    BLUE(Color.BLUE),
    GREEN(Color.GREEN),
    YELLOW(Color.YELLOW),
    ORANGE(Color.ORANGE),
    RED(Color.RED),
    WHITE(Color.WHITE),
    LOCOMOTIVE(null);

    private final Color color;

    Card(Color color){
        this.color=color;
    }

    public static final List<Card> ALL = List.of(Card.values());
    public static final int COUNT = ALL.size();
    public static final List<Card> CARS = Arrays.asList(BLACK,VIOLET,BLUE,GREEN,YELLOW,ORANGE,RED,WHITE);

    /**
     * @param color Couleur de la carte à renvoyer
     * @return Renvoie la carte de la couleur appropriée
     */
    public static Card of(Color color){
        return Card.valueOf(color.toString());
    }

    /**
     * @return Renvoie la couleur associé à la voiture donnée en argument, renvoie null pour la locomotive.
     */
    public Color color(){
        return CARS.contains(this) ? this.color : null;
    }
}
