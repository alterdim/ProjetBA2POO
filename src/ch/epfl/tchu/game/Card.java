package ch.epfl.tchu.game;

import java.util.Arrays;
import java.util.List;

/**
 * Créé à 13:36 le 22.02.2021
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 *
 * Enum qui contient les différentes cartes (les voitures de différentes couleurs ainsi que la locomotive)
 * A chaque carte est associée un couleur.
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

    /**
     * Constructeur de l'enum
     * @param color Couleur de l'enum
     */
    Card(Color color){
        this.color=color;
    }

    /**
     * Liste des cartes (couleurs de voitures et locomotive)
     */
    public static final List<Card> ALL = List.of(Card.values());
    /**
     * Nombre de types de cartes
     */
    public static final int COUNT = ALL.size();
    /**
     * Array qui ne contient que les couleurs de voitures (donc pas la locomotive)
     */
    public static final List<Card> CARS = Arrays.asList(BLACK,VIOLET,BLUE,GREEN,YELLOW,ORANGE,RED,WHITE);

    /**
     * @param color Couleur de la carte à renvoyer
     * @return Renvoie la carte de la couleur appropriée
     */
    public static Card of(Color color) {
        for (Card c : Card.values()) {
            if (color.equals(c.color)) {
                return c;
            }
        }
        return null;
    }


    /**
     * @return Renvoie la couleur associé à la voiture donnée en argument, renvoie null pour la locomotive.
     */
    public Color color(){
        return CARS.contains(this) ? this.color : null;
    }
}
