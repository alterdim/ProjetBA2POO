package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Random;

/**
 * Créé le 01.03.2021 à 17:45
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
class CardStateTest {

    @Test
    void withDrawnFaceUpCard(){
        SortedBag.Builder<Card> list = new SortedBag.Builder<>();
        list.add(Card.RED);
        list.add(Card.GREEN);
        list.add(Card.ORANGE);
        list.add(Card.VIOLET);
        list.add(Card.YELLOW);
        list.add(Card.BLUE);
        list.add(Card.BLACK);
        list.add(Card.WHITE);
        list.add(Card.LOCOMOTIVE);
        var ca = Deck.of(list.build(), new Random());
        CardState cardState = CardState.of(ca);
        cardState.withDrawnFaceUpCard(2);
    }

    @Test
    void withDrawnFaceUpCard2(){
        SortedBag.Builder<Card> list = new SortedBag.Builder<>();
        list.add(Card.RED);
        list.add(Card.GREEN);
        list.add(Card.ORANGE);
        list.add(Card.VIOLET);
        list.add(Card.YELLOW);
        var ca = Deck.of(list.build(), new Random());
        CardState cardState = CardState.of(ca);
//        cardState.withDrawnFaceUpCard(2);
    }

    @Test
    void withDeckRecreatedFromDiscards(){
        SortedBag.Builder<Card> list = new SortedBag.Builder<>();
        list.add(Card.RED);
        list.add(Card.GREEN);
        list.add(Card.ORANGE);
        list.add(Card.VIOLET);
        list.add(Card.YELLOW);
        list.add(Card.BLUE);
        /*list.add(Card.BLACK);
        list.add(Card.WHITE);
        list.add(Card.LOCOMOTIVE);*/
        var ca = Deck.of(list.build(), new Random());
        CardState cardState = CardState.of(ca);
//        cardState.withDeckRecreatedFromDiscards(new Random());
    }
//    private final SortedBag<Card> discards;
    @Test
    void test2(){
//        discards
//        Collections.shuffle(discards.toList());
//        System.out.println(discards.toList());
//        return new CardState(Deck.of(discards, rng), null);
    }
}