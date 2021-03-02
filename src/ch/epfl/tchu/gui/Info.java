package ch.epfl.tchu.gui;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.game.Card;

import java.util.List;

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
        String colorString;
        switch (card) {
            case BLACK:
                colorString = "noire";
                break;
            case VIOLET:
                colorString = "violette";
                break;
            case BLUE:
                colorString = "bleue";
                break;
            case GREEN:
                colorString = "verte";
                break;
            case YELLOW:
                colorString = "jaune";
                break;
            case ORANGE:
                colorString = "orange";
                break;
            case RED:
                colorString = "rouge";
                break;
            case WHITE:
                colorString = "blanche";
                break;
            case LOCOMOTIVE:
                colorString = "locomotive";
                break;
            default:
                colorString = "somethingIsWrong !";
                throw new IllegalArgumentException();
        }
        if (count > 1) {
            colorString+= "s";
        }
        return colorString;
    }

    public static String draw(List<String> playerNames, int points) {
        return "a";
    }

}
