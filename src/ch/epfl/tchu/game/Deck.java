package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Créé le 01.03.2021 à 14:11
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public final class Deck<C extends Comparable<C>> {

    private final List<C> cards;

    private Deck(List<C> cards){
        this.cards=cards;

    }


    public static <C extends Comparable<C>> Deck<C> of(SortedBag<C> cards, Random rng){
        List<C> cardsList = cards.toList();
        Collections.shuffle(cardsList, rng);
        return new Deck(cardsList);
    }

    public int size(){
        return cards.size();
    }

    public boolean isEmpty(){
        return cards.isEmpty();
    }

    /**
     *
     * @return
     * @throws IllegalArgumentException
     */
    public C topCard(){
        Preconditions.checkArgument(!cards.isEmpty());
        return cards.get(0);
    }

    /**
     *
     * @return
     * @throws IllegalArgumentException
     */
    public Deck<C> withoutTopCard() {
        Preconditions.checkArgument(!isEmpty());
        return new Deck<>(cards.subList(1, size()-1));
    }

    /**
     *
     * @param count
     * @return
     * @throws IllegalArgumentException
     */
    public SortedBag<C> topCards(int count){
        Preconditions.checkArgument(count>=0 && count<=size());
        SortedBag.Builder<C> cardList = new SortedBag.Builder<>();
        for(C c:cards.subList(0, count)){
            cardList.add(c);
        }
        return cardList.build();
    }

    /**
     *
     * @param count
     * @return
     * @throws IllegalArgumentException
     */
    public Deck<C> withoutTopCards(int count){
        Preconditions.checkArgument(count>=0 && count<=size());
        return new Deck<>(cards.subList(count, size()-1));
    }
}
