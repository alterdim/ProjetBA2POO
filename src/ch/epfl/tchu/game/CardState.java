package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static ch.epfl.tchu.game.Constants.FACE_UP_CARDS_COUNT;
import static ch.epfl.tchu.game.Constants.FACE_UP_CARD_SLOTS;

/**
 *
 * Crée un CardState, objet qui rassemble les informations privées du plateau de jeu.
 * Créé le 01.03.2021 à 16:10
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public final class CardState extends PublicCardState{
    private final Deck<Card> drawCards;
    private final SortedBag<Card> discards;

    private CardState(List<Card> faceUpCards ,Deck<Card> deck, SortedBag<Card> discards){
        super(faceUpCards, deck.size(), discards.size());
        this.drawCards=deck;
        this.discards=discards;
    }

    /**
     * méthode de construction publique
     * @param deck Un Deck de cartes
     * @return un CardState
     * @throws IllegalArgumentException si le deck ne contient pas n éléments
     */
    public static CardState of(Deck<Card> deck){
        Preconditions.checkArgument(deck.size()>=FACE_UP_CARDS_COUNT);

        return new CardState(deck.topCards(FACE_UP_CARDS_COUNT).toList(), deck.withoutTopCards(FACE_UP_CARDS_COUNT), new SortedBag.Builder<Card>().build());
    }

    /**
     *  Retourne un cardState avec la carte donnée dans l'index supprimée de la liste et remplacée par la première carte de la pioche
     * @param slot index d'un carte visible
     * @return retourne une CardState
     * @throws IndexOutOfBoundsException si le paramètre n'est pas l'index d'une carte visible
     * @throws IllegalArgumentException si la pioche est vide
     */
    public CardState withDrawnFaceUpCard(int slot){
        Objects.checkIndex(slot, Constants.FACE_UP_CARDS_COUNT);
//        if (!FACE_UP_CARD_SLOTS.contains(slot)) throw new IndexOutOfBoundsException();
        Preconditions.checkArgument(!drawCards.isEmpty());

        List<Card> faceUpCards = new ArrayList<>(faceUpCards());
        faceUpCards.set(slot, drawCards.topCard());  //Replace element in position slot
        return new CardState(faceUpCards ,drawCards.withoutTopCard(), discards);
    }

    /**
     *
     * @return Retourne la carte se trouvant au sommet de la pioche
     * @throws IllegalArgumentException  si la pioche est vide
     */
    public Card topDeckCard(){
        Preconditions.checkArgument(!drawCards.isEmpty());
        return drawCards.topCard();
    }

    /**
     *
     * @return retourne un ensemble de carte en enlevant la première carte de la pioche
     * @throws IllegalArgumentException si la pioche est vide
     */
    public CardState withoutTopDeckCard(){
        Preconditions.checkArgument(!drawCards.isEmpty());
        return new CardState(faceUpCards(), drawCards.withoutTopCard(), discards);
    }

    /**
     * Remet dans la pioche les cartes de la défausse mélangées
     * @param rng instance de Random
     * @return Retourne un ensemble de carte avec les cartes de la défausses mélangées et remise dans la pioche
     * @throws IllegalArgumentException si la pioche n'est pas vide
     */
    public CardState withDeckRecreatedFromDiscards(Random rng){
        Preconditions.checkArgument(drawCards.isEmpty());
        return new CardState(faceUpCards(), Deck.of(discards, rng), new SortedBag.Builder<Card>().build());
    }

    /**
     * Ajoute les cartes en paramètres à la défausse
     * @param additionalDiscards ensemble de cartes à mettre a la défausse
     * @return retourne un ensemble de cartes
     */
    public CardState withMoreDiscardedCards(SortedBag<Card> additionalDiscards){
        SortedBag.Builder<Card> discardList = new SortedBag.Builder<>();
        discardList.add(discards);
        discardList.add(additionalDiscards);

        return new CardState(faceUpCards(), drawCards, discardList.build());
    }
}
