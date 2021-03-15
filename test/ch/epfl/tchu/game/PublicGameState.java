package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Fichier créé à 13:26 le 15/03/2021
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public final class PublicGameState {
    private final int ticketCount;
    private final PublicCardState cardState;
    private final PlayerId currentPlayerId;
    private final Map<PlayerId, PublicPlayerState> playerState;
    private final PlayerId lastPlayer;

    /** Constructeur de la partie publique de l'état d'une partie.
     * @param ticketCount Nombre de tickets dans la pioche de tickets.
     * @param cardState La partie publique des mains des joueurs.
     * @param currentPlayerId L'identification du joueur en cours.
     * @param playerState La partie publique de l'état des joueurs.
     * @param lastPlayer Le dernier joueur.
     */
    public PublicGameState(int ticketCount, PublicCardState cardState,
                           PlayerId currentPlayerId, Map<PlayerId, PublicPlayerState> playerState, PlayerId lastPlayer) {
        Preconditions.checkArgument(cardState.deckSize() >= 0);
        Preconditions.checkArgument(playerState.size() == 2);
        Objects.requireNonNull(currentPlayerId);
        this.ticketCount = ticketCount;
        this.cardState = cardState;
        this.currentPlayerId = currentPlayerId;
        this.playerState = playerState;
        this.lastPlayer = lastPlayer;

    }

    /**
     * @return Renvoie le nombre de tickets dans la pioche de tickets.
     */
    public int ticketCount() {
        return ticketCount;
    }

    /**
     * @return Renvoie vrai si il y a plus d'un ticket dans la pioche des tickets.
     */
    public boolean canDrawTickets() {
        return ticketCount != 0;
    }

    /**
     * @return Renvoie la partie publique de l'état des cartes.
     */
    public PublicCardState cardState() {
        return cardState;
    }

    /**
     * @return Renvoie vrai si il y a au moins 5 cartes réparties entre la défausse et la pioche.
     */
    public boolean canDrawCards() {
        return (cardState.totalSize() >= 5);
    }

    /**
     * @return Renvoie l'identité du joueur actuel.
     */
    public PlayerId currentPlayerId() {
        return currentPlayerId;
    }

    /**
     * @param playerId L'identité du joueur.
     * @return Renvoie la partie publique de l'état du joueur passé en argument.
     */
    public PublicPlayerState playerState(PlayerId playerId) {
        return playerState.get(playerId);
    }

    /**
     * @return Renvoie la partie publique de l'état du joueur en cours.
     */
    public PublicPlayerState currentPlayerState() {
        return playerState.get(currentPlayerId);
    }

    /**
     * @return Renvoie une liste contenant toutes les routes déjà prises par l'un des joueurs (toutes les routse non disponibles)
     */
    public List<Route> claimedRoute() {
        List<Route> union = new ArrayList<>();
        for (PublicPlayerState p : playerState.values()) {
            union.addAll(p.routes());
        }
        return union;
    }

    /**
     * @return Renvoie l'identité publique du dernier joueur, sera null si elle n'a pas encore été calculée (le dernier tour n'a pas commencé).
     */
    public PlayerId lastPlayer() {
        return lastPlayer;
    }



}
