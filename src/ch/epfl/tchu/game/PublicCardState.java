package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.List;
import java.util.Objects;

/**
 * Fichier créé à 15:28 le 01/03/2021
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 *
 */
public class PublicCardState {
    private final int deckSize;
    private final int discardSize;
    private final List<Card> faceUpCards;

    /**
     * Crée un publicCardState, objet qui rassemble les informations publiques du plateau de jeu.
     * @param faceUpCards les cartes à côté de la pioche qui sont face ouverte. Leur nombre est constant.
     * @param deckSize la taille de la pioche
     * @param discardSize la taille de la défausse
     * @throws IllegalArgumentException si la taille de faceUpCards n'est pas égale au nombre de cartes
     * révélées ou si la taille du deck/de la défausse est négative.
     */
    public PublicCardState(List<Card> faceUpCards, int deckSize, int discardSize) {
        Preconditions.checkArgument(faceUpCards.size() == Constants.FACE_UP_CARDS_COUNT);
        Preconditions.checkArgument(deckSize >= 0 && discardSize >= 0);
        this.faceUpCards = List.copyOf(faceUpCards);
        this.deckSize = deckSize;
        this.discardSize = discardSize;
    }

    /**
     * @return la liste des cartes visibles.
     */
    public List<Card> faceUpCards() {
        return faceUpCards;
    }

    /**
     * @param slot l'index des cartes visibles à consulter.
     * @throws IndexOutOfBoundsException si l'index est invalide.
     * @return la carte à l'index donné.
     */
    public Card faceUpCard(int slot) {
        return faceUpCards.get(Objects.checkIndex(slot, Constants.FACE_UP_CARDS_COUNT));
    }

    /**
     * @return la taile du deck.
     */
    public int deckSize() {
        return deckSize;
    }

    /**
     * @return true si le deck est vide (0 cartes). Renvoie false sinon.
     */
    public boolean isDeckEmpty() {
        return deckSize == 0;
    }

    /**
     * @return la taille de la pile de défausse.
     */
    public int discardsSize() {
        return discardSize;
    }
}
