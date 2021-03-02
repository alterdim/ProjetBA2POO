package ch.epfl.tchu.gui;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.game.Card;

import java.util.List;

import static ch.epfl.tchu.gui.StringsFr.*;

/**
 * Fichier créé à 14:34 le 02/03/2021
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public class Info {
    private final String playerName;

    public Info(String playerName) {
        this.playerName = playerName;

    }

    /**
     * @param card Une carte
     * @param count Le nombre de cartes. Doit être supérieur à 0.
     * @throws IllegalArgumentException si la carte demandée n'a pas de texte français associé (ne devrait jamais arriver),
     * ou si le nombre de cartes n'est pas strictement positif.
     * @return un string en français lié à la couleur et à la multiplicité d'une carte.
     */
    public static String cardName(Card card, int count) {
        Preconditions.checkArgument(count > 0);
        StringBuilder colorString=new StringBuilder();
        switch (card) {
            case BLACK:
                colorString.append(BLACK_CARD);
                break;
            case VIOLET:
                colorString.append(VIOLET_CARD);
                break;
            case BLUE:
                colorString.append(BLUE_CARD);
                break;
            case GREEN:
                colorString.append(GREEN_CARD);
                break;
            case YELLOW:
                colorString.append(YELLOW_CARD);
                break;
            case ORANGE:
                colorString.append(ORANGE_CARD);
                break;
            case RED:
                colorString.append(RED_CARD);
                break;
            case WHITE:
                colorString.append(WHITE_CARD);
                break;
            case LOCOMOTIVE:
                colorString.append(LOCOMOTIVE_CARD);
                break;
            default:
                colorString.append("somethingIsWrong !");
                throw new IllegalArgumentException();
        }
        colorString.append(plural(count));
        return colorString.toString();
    }

    public static String draw(List<String> playerNames, int points) {
        return "a";
    }

}
