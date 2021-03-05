package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Créé le 05.03.2021 à 13:01
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
class PublicCardStateTest {
    @Test
    void failOnPublicLessCardsCardState(){
        List<Card> cards = new ArrayList<>();
        cards.add(Card.LOCOMOTIVE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        cards.add(Card.WHITE);
//        var publicCardState = new PublicCardState(cards, 0,0);
        assertThrows(IllegalArgumentException.class,  ()-> new PublicCardState(cards, 0,0));
    }

    @Test
    void failOnPublicMoreCardsCardState(){
        List<Card> cards = new ArrayList<>();
        cards.add(Card.LOCOMOTIVE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        cards.add(Card.WHITE);
        cards.add(Card.GREEN);
        cards.add(Card.YELLOW);
//        var publicCardState = new PublicCardState(cards, 0,0);
        assertThrows(IllegalArgumentException.class,  ()-> new PublicCardState(cards, 0,0));
    }

    @Test
    void failOnPublicNegativeDiscardSizeCardState(){
        List<Card> cards = new ArrayList<>();
        cards.add(Card.LOCOMOTIVE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        cards.add(Card.WHITE);
        cards.add(Card.GREEN);
//        var publicCardState = new PublicCardState(cards, 0,0);
        assertThrows(IllegalArgumentException.class,  ()-> new PublicCardState(cards, 0,-1));
    }

    @Test
    void failOnPublicNegativeDeckSizeCardState(){
        List<Card> cards = new ArrayList<>();
        cards.add(Card.LOCOMOTIVE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        cards.add(Card.WHITE);
        cards.add(Card.GREEN);
//        var publicCardState = new PublicCardState(cards, 0,0);
        assertThrows(IllegalArgumentException.class,  ()-> new PublicCardState(cards, -1,0));
    }
    @Test
    void failOnPublicNegativeDeckAndDiscardSizeCardState(){
        List<Card> cards = new ArrayList<>();
        cards.add(Card.LOCOMOTIVE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        cards.add(Card.WHITE);
        cards.add(Card.GREEN);
//        var publicCardState = new PublicCardState(cards, 0,0);
        assertThrows(IllegalArgumentException.class,  ()-> new PublicCardState(cards, -1,-1));
    }



    @Test
    void workOnTrivialTotalSize(){
        List<Card> cards = new ArrayList<>();
        cards.add(Card.LOCOMOTIVE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        cards.add(Card.WHITE);
        cards.add(Card.GREEN);
        int x = 0;
        int y= 0;
        int totalSize= cards.size()+x+y;

        var publicC = new PublicCardState(cards, x,y);
        assertEquals(totalSize, publicC.totalSize());
    }

    @Test
    void workOnTrivialfaceUpCards(){
        List<Card> cards = new ArrayList<>();
        cards.add(Card.LOCOMOTIVE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        cards.add(Card.WHITE);
        cards.add(Card.GREEN);
        int x = 0;
        int y= 0;

        var publicC = new PublicCardState(cards, x,y);
        assertEquals(cards, publicC.faceUpCards());
    }

    @Test
    void workOnTrivialfaceUpCard(){
        List<Card> cards = new ArrayList<>();
        cards.add(Card.LOCOMOTIVE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        cards.add(Card.WHITE);
        cards.add(Card.GREEN);
        int x = 0;
        int y= 0;

        var publicC = new PublicCardState(cards, x,y);
        for (int i =0; i<cards.size();i++){
            assertEquals(cards.get(i), publicC.faceUpCard(i));
        }
    }

    @Test
    void FailOnInvalidSlotfaceUpCard(){
        List<Card> cards = new ArrayList<>();
        cards.add(Card.LOCOMOTIVE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        cards.add(Card.WHITE);
        cards.add(Card.GREEN);
        int x = 0;
        int y= 0;

        var publicC = new PublicCardState(cards, x,y);

        assertThrows(IndexOutOfBoundsException.class, ()-> publicC.faceUpCard(-1));
        assertThrows(IndexOutOfBoundsException.class, ()-> publicC.faceUpCard(5));
    }

    @Test
    void workOnTrivialdeckSize() {
        List<Card> cards = new ArrayList<>();
        cards.add(Card.LOCOMOTIVE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        cards.add(Card.WHITE);
        cards.add(Card.GREEN);

        int x=Integer.MAX_VALUE;
        int y = 0;

        var publicC = new PublicCardState(cards, x, y);
        assertEquals(x, publicC.deckSize());
    }

    @Test
    void workOnTrivialDiscardsSize() {
        List<Card> cards = new ArrayList<>();
        cards.add(Card.LOCOMOTIVE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        cards.add(Card.WHITE);
        cards.add(Card.GREEN);

        int x=0;
        int y = 5;

        var publicC = new PublicCardState(cards, x, y);
        assertEquals(y, publicC.discardsSize());
    }

    @Test
    void workEmptyDeck(){
        List<Card> cards = new ArrayList<>();
        cards.add(Card.LOCOMOTIVE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        cards.add(Card.WHITE);
        cards.add(Card.GREEN);
        int x=0;
        int y = 0;

        var publicC = new PublicCardState(cards, x, y);

        assertTrue(publicC.isDeckEmpty());
    }
    @Test
    void workNonEmptyDeck(){
        List<Card> cards = new ArrayList<>();
        cards.add(Card.LOCOMOTIVE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        cards.add(Card.WHITE);
        cards.add(Card.GREEN);
        int x=3;
        int y = 0;

        var publicC = new PublicCardState(cards, x, y);

        assertFalse(publicC.isDeckEmpty());
    }


}