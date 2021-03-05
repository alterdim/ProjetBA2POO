package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import javax.swing.table.DefaultTableModel;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Créé le 01.03.2021 à 15:24
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
class DeckTest {
    @Test
    void workOnTrivialTopCard(){
        SortedBag.Builder<String> cardList = new SortedBag.Builder<>();
        cardList.add("A");
        cardList.add("A");
        cardList.add("B");
        cardList.add("C");
        cardList.add("D");
        cardList.add("A");
        Deck<String> deck= Deck.of(cardList.build(), new Random());
        System.out.println(deck.withoutTopCards(5).topCard());
    }

}