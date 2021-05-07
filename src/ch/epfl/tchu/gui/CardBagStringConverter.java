package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import javafx.util.StringConverter;

/**
 *
 * Transform un SortedBag de cartes en String
 * Créé le 07.05.2021 à 14:17
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public final class CardBagStringConverter extends StringConverter<SortedBag<Card>> {


    public CardBagStringConverter() {
    }

    @Override
    public String toString(SortedBag<Card> cardBag) {
        return Info.generateCardString(cardBag);
    }

    /** NE M'UTILISEZ PAS PAR PITIE
     * @param string non
     * @throws UnsupportedOperationException car ne doit pas être utilisé
     * @return stop
     */
    @Override
    public SortedBag<Card> fromString(String string) {
        throw new UnsupportedOperationException();
    }

}
