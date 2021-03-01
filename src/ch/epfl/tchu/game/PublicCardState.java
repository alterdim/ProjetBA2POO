package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.Collections;
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
        Preconditions.checkArgument(faceUpCards.size() == Constants.FACE_UP_CARD_SLOTS.size());
        Preconditions.checkArgument(deckSize >= 0 && discardSize >= 0);
        this.deckSize = deckSize;
        this.discardSize = discardSize;
        this.faceUpCards = faceUpCards;

    }

    public int totalSize() {
        return discardSize + deckSize + faceUpCards.size();
    }

    public List<Card> faceUpCards() {
        return Collections.unmodifiableList(faceUpCards);
    }

    public Card faceUpCard(int slot) {
        return faceUpCards.get(Objects.checkIndex(slot, 5));
    }

    public int deckSize() {
        return deckSize;
    }

    public boolean isDeckEmpty() {
        return deckSize == 0;
    }

    public int discardSize() {
        return discardSize;
    }
}
