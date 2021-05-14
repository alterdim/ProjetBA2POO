package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import javafx.util.StringConverter;

/**
 *
 * Transform un SortedBag de cartes en String
 *
 * Créé le 07.05.2021 à 14:17
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public final class CardBagStringConverter extends StringConverter<SortedBag<Card>> {


    /**
     * Constructeur (vide)
     */
    public CardBagStringConverter() {
    }

    /**
     * Convertit le sortedBag de cartes en texte
     * @param cardBag SortedBag de cartes
     * @return texte en fr du contenu du sortedBag
     */
    @Override
    public String toString(SortedBag<Card> cardBag) {
        return Info.generateCardString(cardBag);
    }

    /** A ne pas utiliser
     * @param string Texte à convertir
     * @throws UnsupportedOperationException La classe n'implémente pas cette méthode et renvoie une erreur
     * @return SortedBag de cartes
     */
    @Override
    public SortedBag<Card> fromString(String string) {
        throw new UnsupportedOperationException();
    }

}
