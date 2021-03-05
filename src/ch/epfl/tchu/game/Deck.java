package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Tas de cartes
 *
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

    /**
     * Calcul un tas de cartes
     * @param <C> élément générique
     * @param cards liste des cartes
     * @param rng instance de Random
     * @return Un Deck
     */
    public static <C extends Comparable<C>> Deck of(SortedBag<C> cards, Random rng){
        List<C> cardsList = cards.toList();
        Collections.shuffle(cardsList, rng);
        return new Deck<>(cardsList);
    }

    /**
     * Taille du tas
     * @return taille du tas
     */
    public int size(){
        return cards.size();
    }

    /**
     * @return true si il y a des éléments dans le tas
     */
    public boolean isEmpty(){
        return cards.isEmpty();
    }

    /**
     *
     * @return Premier élément du tas
     * @throws IllegalArgumentException si le tas est vide
     */
    public C topCard(){
        Preconditions.checkArgument(!cards.isEmpty());
        return cards.get(0);
    }

    /**
     * @return Un nouveau tas sans le premier élément
     * @throws IllegalArgumentException Si le tas est vide
     */
    public Deck<C> withoutTopCard() {
        Preconditions.checkArgument(!isEmpty());
        return new Deck<>(cards.subList(1, size()));
    }

    /**
     *
     * @param count nombre d'éléments a sélectionner
     * @return  retourner les count premiers éléments du tas
     * @throws IllegalArgumentException si le nombre d'élément a sélectionner n'est pas compris dans le tas
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
     * @param count  nombre d'éléments a enlever
     * @return Un tas sans les count premiers éléments
     * @throws IllegalArgumentException si le nombre d'élément a supprimer n'est pas compris dans le tas
     */
    public Deck<C> withoutTopCards(int count){
        Preconditions.checkArgument(count>=0 && count<=size());
        return new Deck<>(cards.subList(count, size()));
    }
}
