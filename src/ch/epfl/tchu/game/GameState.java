package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.*;

/**
 * Fichier créé à 14:16 le 15/03/2021
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public final class GameState extends PublicGameState {

    private final Map<PlayerId, PlayerState> playerStateMap;
    private final Deck<Ticket> ticketDeck;
    private final CardState cardState;

    /**
     * Constructeur de l'état d'une partie.
     *
     * @param ticketDeck      La pioche de tickets.
     * @param cardState       La partie publique des mains des joueurs.
     * @param currentPlayerId L'identification du joueur en cours.
     * @param playerState     La partie publique de l'état des joueurs.
     * @param lastPlayer      Le dernier joueur.
     */
    private GameState(Deck<Ticket> ticketDeck, CardState cardState, PlayerId currentPlayerId,
                      Map<PlayerId, PlayerState> playerState, PlayerId lastPlayer) {
        super(ticketDeck.size(), cardState, currentPlayerId, Map.copyOf(playerState), lastPlayer);
        this.cardState = cardState;
        this.ticketDeck = ticketDeck;
        this.playerStateMap = playerState;
    }

    /**
     * @param tickets Les tickets à mettre en jeu.
     * @param rng Le générateur de nombre aléatoires
     * @return Retourne un gameState initial pour les débuts de partie. Le premier joueur est déjà choisi au hasard et
     * les cartes distribuées.
     */
    public static GameState initial(SortedBag<Ticket> tickets, Random rng) {
        PlayerId firstPlayer = PlayerId.PLAYER_2;
        if (rng.nextBoolean()) {
            firstPlayer = PlayerId.PLAYER_1;
        }
        Deck<Ticket> ticketDeck = Deck.of(tickets, rng);
        Deck<Card> cardDeck = Deck.of(Constants.ALL_CARDS, rng);
        SortedBag<Card> initialCardsPlayer1 = cardDeck.topCards(Constants.INITIAL_CARDS_COUNT);
        cardDeck = cardDeck.withoutTopCards(Constants.INITIAL_CARDS_COUNT);
        SortedBag<Card> initialCardsPlayer2 = cardDeck.topCards(Constants.INITIAL_CARDS_COUNT);
        cardDeck = cardDeck.withoutTopCards(Constants.INITIAL_CARDS_COUNT);
        Map<PlayerId, PlayerState> playerStateMap = new EnumMap<>(PlayerId.class);
        playerStateMap.put(PlayerId.PLAYER_1, PlayerState.initial(initialCardsPlayer1));
        playerStateMap.put(PlayerId.PLAYER_2, PlayerState.initial(initialCardsPlayer2));
        CardState cardState = CardState.of(cardDeck);

        return new GameState(ticketDeck, cardState, firstPlayer, playerStateMap, null);
    }

    /**
     * @param playerId L'identité du joueur.
     * @return Retourne la TOTALITÉ du playerState et pas seulement la partie publique !!
     */
    @Override
    public PlayerState playerState(PlayerId playerId) {
        return playerStateMap.get(playerId);
    }

    /**
     * @return Retourne la TOTALITÉ du PlayerState du joueur en cours, pas seulement la partie publique !!
     */
    @Override
    public PlayerState currentPlayerState() {
        return playerState(currentPlayerId());
    }

    /**
     * @throws IllegalArgumentException si count n'est pas compris entre zéro et la taille du deck de tickets.
     * @param count le nombre de tickets à "regarder".
     * @return Renvoie un sortedBag contenant les count tickets du haut de la pioche.
     */
    public SortedBag<Ticket> topTickets(int count) {
        Preconditions.checkArgument(count >= 0 && count <= ticketDeck.size());
        return ticketDeck.topCards(count);
    }

    /**
     * @throws IllegalArgumentException si count n'est pas compris entre zéro et la taille du deck de tickets.
     * @param count Le nombre de tickets à enlever de la pioche de tickets.
     * @return Retourne un gameState identique sans les count tickets du dessus.
     */
    public GameState withoutTopTickets(int count) {
        Preconditions.checkArgument(count >= 0 && count <= ticketDeck.size());
        return new GameState(ticketDeck.withoutTopCards(count), cardState, currentPlayerId(), playerStateMap, lastPlayer());
    }

    /**
     * @throws IllegalArgumentException si la pioche est vide.
     * @return Renvoie la carte "au dessus" de la pioche.
     */
    public Card topCard() {
        Preconditions.checkArgument(!cardState.isDeckEmpty());
        return cardState.topDeckCard();
    }

    /**
     * @throws IllegalArgumentException si la pioche est vide.
     * @return Un gameState identique sans la carte du dessus de la pioche.
     */
    public GameState withoutTopCard() {
        Preconditions.checkArgument(!cardState.isDeckEmpty());
        return new GameState(ticketDeck, cardState.withoutTopDeckCard(), currentPlayerId(), playerStateMap, lastPlayer());
    }

    /**
     * @param discardedCards le nombre de cartes à ajouter à la défausse
     * @return Un nouveau GameState identique avec le SortedBag ajouté à la défausse.
     */
    public GameState withMoreDiscardedCards(SortedBag<Card> discardedCards) {
        return new GameState(ticketDeck, cardState.withMoreDiscardedCards(discardedCards), currentPlayerId(), playerStateMap, lastPlayer());
    }

    /**
     * @param rng Le générateur de nombres aléatoires.
     * @return Un gameState identique (this) si
     */
    public GameState withCardsDeckRecreatedIfNeeded(Random rng) {
        if (!cardState.isDeckEmpty()) {
            return this;
        }
        return new GameState(ticketDeck, cardState.withDeckRecreatedFromDiscards(rng), currentPlayerId(), playerStateMap, lastPlayer());
    }

    /**
     * @param playerId Le joueur auquel ajouter des tickets.
     * @param chosenTickets Les tickets à ajouter.
     * @return Un gameState identique mais dont le joueur choisi a reçu de nouveaux tickets.
     * @throws IllegalArgumentException si le joueur possède déjà au moins un billet
     */
    public GameState withInitiallyChosenTickets(PlayerId playerId, SortedBag<Ticket> chosenTickets) {
        Preconditions.checkArgument(playerState(playerId).ticketCount()==0);
        Map<PlayerId, PlayerState> newMap = new TreeMap<>(playerStateMap);
        newMap.put(playerId, playerState(playerId).withAddedTickets(chosenTickets));
        return new GameState(ticketDeck, cardState, currentPlayerId(), newMap, lastPlayer());
    }

    /**
     * @throws  IllegalArgumentException si les tickets choisis n'appartiennent pas au deck.
     * @param drawnTickets Les tickets piochés.
     * @param chosenTickets Les tickets choisis par le joueur.
     * @return Retourne un gameState identique avec les tickets choisis ajoutés à la mains et les piochés retirés de la pioche.
     */
    public GameState withChosenAdditionalTickets(SortedBag<Ticket> drawnTickets, SortedBag<Ticket> chosenTickets) {
        Preconditions.checkArgument(drawnTickets.contains(chosenTickets));
        Deck<Ticket> newDeck = ticketDeck.withoutTopCards(drawnTickets.size());
        Map<PlayerId, PlayerState> newMap = new TreeMap<>(playerStateMap);
        newMap.put(currentPlayerId(), playerState(currentPlayerId()).withAddedTickets(chosenTickets));
        return new GameState(newDeck, cardState, currentPlayerId(), newMap, lastPlayer());
    }

    /**
     * @throws IllegalArgumentException s'il n'est pas possible de piocher des cartes actuellement (voir canDrawCards())
     * @param slot L'emplacement de la carte ouverte choisie par le joueur.
     * @return Retourne un GameState identique où la carte du slot a été remplacée par la carte du haut de la pioche et ajoutée à la main du joueur.
     */
    public GameState withDrawnFaceUpCard(int slot) {
        Preconditions.checkArgument(canDrawCards());

        Card c = cardState().faceUpCard(slot);
        CardState newCardState=cardState.withDrawnFaceUpCard(slot);
        Map<PlayerId, PlayerState> newMap = new TreeMap<>(playerStateMap);
        newMap.put(currentPlayerId(), currentPlayerState().withAddedCard(c));

        return new GameState(ticketDeck, newCardState,currentPlayerId(), newMap, lastPlayer());
    }

    /**
     * @throws IllegalArgumentException s'il n'est pas possible de piocher des cartes actuellement (voir canDrawCards())
     * @return Retourne un gameState identique où la carte du haut de la pioche a été piochée par le joueur courant.
     */
    public GameState withBlindlyDrawnCard() {
        Preconditions.checkArgument(canDrawCards());
        Card drawnCard = cardState.topDeckCard();
        CardState newCardState = cardState.withoutTopDeckCard();
        Map<PlayerId, PlayerState> newMap = new TreeMap<>(playerStateMap);
        newMap.put(currentPlayerId(), playerState(currentPlayerId()).withAddedCard(drawnCard));
        return new GameState(ticketDeck, newCardState, currentPlayerId(), newMap, lastPlayer());

    }

    /**
     * @param route La route capturée.
     * @param cards Les cartes utilisées pour capturer la route.
     * @return Un GameState identique où le joueur courant à utilisé cards pour s'emparer de route.
     */
    public GameState withClaimedRoute(Route route, SortedBag<Card> cards) {
        Map<PlayerId, PlayerState> newMap = new TreeMap<>(playerStateMap);
        newMap.put(currentPlayerId(), playerState(currentPlayerId()).withClaimedRoute(route, cards));
        return new GameState(ticketDeck, cardState.withMoreDiscardedCards(cards), currentPlayerId(), newMap, lastPlayer());
    }

    /**
     * @return Renvoie vrai si le dernier joueur n'a pas encore été désigné et si le nombre de wagons dans la main du joueur courant est inférieur ou égal à 2.
     */
    public boolean lastTurnBegins() {
        if (lastPlayer() != null) {
            return false;
        }
        return (currentPlayerState().carCount() <= 2);
    }

    /**
     * @return Retourne un gameState identique où le prochain joueur devient le joueur actuel, et le joueur actuel devient le dernier joueur
     * si le dernier tour est enclenché (voir lastTurnBegins)
     */
    public GameState forNextTurn() {
        if (lastTurnBegins()) {
            return new GameState(ticketDeck, cardState, currentPlayerId().next(), playerStateMap, currentPlayerId());
        }
        return new GameState(ticketDeck, cardState, currentPlayerId().next(), playerStateMap, lastPlayer());
    }
}
