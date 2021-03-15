package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

/**
 * Fichier créé à 14:16 le 15/03/2021
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public final class GameState extends PublicGameState {

    private final Map<PlayerId, PlayerState> playerStateMap;
    private final Deck<Ticket> ticketDeck;



    /**
     * Constructeur de l'état d'une partie.
     *
     * @param ticketDeck      La pioche de tickets.
     * @param cardState       La partie publique des mains des joueurs.
     * @param currentPlayerId L'identification du joueur en cours.
     * @param playerState     La partie publique de l'état des joueurs.
     * @param lastPlayer      Le dernier joueur.
     */
    private GameState(Deck<Ticket> ticketDeck, PublicCardState cardState, PlayerId currentPlayerId,
                      Map<PlayerId, PublicPlayerState> playerState, PlayerId lastPlayer) {
        super(ticketDeck.size(), cardState, currentPlayerId, playerState, lastPlayer);
        playerStateMap = new EnumMap<>(PlayerId.class);
        this.ticketDeck = ticketDeck;

    }

    public static GameState initial(SortedBag<Ticket> tickets, Random rng) {
        PlayerId firstPlayer = PlayerId.PLAYER_2;
        if (rng.nextBoolean()) {
            firstPlayer = PlayerId.PLAYER_1;
        }
        Deck<Ticket> ticketDeck = Deck.of(tickets, rng);
        Deck<Card> cardDeck = Deck.of(Constants.ALL_CARDS, rng);
        SortedBag<Card> initialCardsPlayer1 = cardDeck.topCards(4);
        cardDeck = cardDeck.withoutTopCards(4);
        SortedBag<Card> initialCardsPlayer2 = cardDeck.topCards(4);
        cardDeck = cardDeck.withoutTopCards(4);
        Map<PlayerId, PlayerState> playerStateMap = new EnumMap<>(PlayerId.class);
        playerStateMap.put(PlayerId.PLAYER_1, PlayerState.initial(initialCardsPlayer1));
        playerStateMap.put(PlayerId.PLAYER_2, PlayerState.initial(initialCardsPlayer2));
        CardState cardState = CardState.of(cardDeck);
        //TODO
        return null;
    }

    private PublicPlayerState makePublic(PlayerState playerState) {
        return new PublicPlayerState(playerState.ticketCount(), playerState.cardCount(), playerState.routes());
    }

}
