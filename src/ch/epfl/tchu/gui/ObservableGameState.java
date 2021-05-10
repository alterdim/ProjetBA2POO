package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

import static ch.epfl.tchu.game.Constants.FACE_UP_CARD_SLOTS;

/**
 * Représente l' état observable d' une partie de tCHu.
 * Créé le 26.04.2021 à 15:24
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public class ObservableGameState {
    private final PlayerId player;
    //Groupe 1, état publique de la partie
    private final IntegerProperty leftTicketsPercentage;
    private final IntegerProperty leftCardsPercentage;
    private final List<ObjectProperty<Card>> faceUpCards;
    private final Map<Route, ObjectProperty<PlayerId>> routesOwned;
    //Groupe 2, état publique des joueurs
    private final Map<PlayerId, IntegerProperty> ticketCount;
    private final Map<PlayerId, IntegerProperty> cardCount;
    private final Map<PlayerId, IntegerProperty> carCount;
    private final Map<PlayerId, IntegerProperty> claimPoints;
    //Groupe 3, état privé du joueur player
    private final ObservableList<Ticket> tickets;
    private final Map<Card, IntegerProperty> cards;
    private final Map<Route, BooleanProperty> routes;

    private PublicGameState currentPublicGameState;
    private PlayerState currentPlayerState;

    private  Map<Route, Route> doubleRoute;

    /**
     * Constructeur
     *
     * @param player L' identité du joueur qui correspond à l' interface graphique
     */
    public ObservableGameState(PlayerId player) {
        this.player = player;
        //groupe 1
        this.faceUpCards = createFaceUpCards();
        this.leftTicketsPercentage = new SimpleIntegerProperty(0);
        this.leftCardsPercentage = new SimpleIntegerProperty(0);
        this.routesOwned = createRoutesOwned();

        //groupe 2
        this.ticketCount = createMap();
        this.cardCount = createMap();
        this.carCount = createMap();
        this.claimPoints = createMap();

        //groupe 3
        this.tickets = createTickets();
        this.cards = createCards();
        this.routes = createRoutes();

        this.doubleRoute = getAllDoubleRoute();
    }

    //groupe 1
    private static List<ObjectProperty<Card>> createFaceUpCards() {
        List<ObjectProperty<Card>> faceUpCardsList = new ArrayList<>();
        for (int slot : FACE_UP_CARD_SLOTS) {
            faceUpCardsList.add(new SimpleObjectProperty<>(null));
        }
        return faceUpCardsList;
    }

    private static Map<Route, ObjectProperty<PlayerId>> createRoutesOwned() {
        Map<Route, ObjectProperty<PlayerId>> map = new HashMap<>();
        for (Route route : ChMap.routes()) {
            map.put(route, new SimpleObjectProperty<>(null));
        }
        return map;
    }

    //Groupe 2
    private static Map<PlayerId, IntegerProperty> createMap() {
        Map<PlayerId, IntegerProperty> map = new HashMap<>();
        for (PlayerId playerId : PlayerId.ALL) {
            map.put(playerId, new SimpleIntegerProperty(0));
        }
        return map;
    }

    //Groupe 3
    private static ObservableList<Ticket> createTickets() {
        return FXCollections.observableArrayList();
    }

    private static Map<Card, IntegerProperty> createCards() {
        Map<Card, IntegerProperty> map = new HashMap<>();
        for (Card card : Card.ALL) {
            map.put(card, new SimpleIntegerProperty(0));
        }
        return map;
    }

    private static Map<Route, BooleanProperty> createRoutes() {
        Map<Route, BooleanProperty> map = new HashMap<>();
        for (Route route : ChMap.routes()) {
            map.put(route, new SimpleBooleanProperty(false));
        }
        return map;
    }

    /**
     * Met à jour la totalité des propriétés
     *
     * @param newGameState   la partie publique du jeu
     * @param newPlayerState l 'état complet du joueur auquel elle correspond
     */
    public void setState(PublicGameState newGameState, PlayerState newPlayerState) {
        this.currentPublicGameState = newGameState;
        this.currentPlayerState = newPlayerState;
        //Groupe 1, état publique de la partie
        updateFaceUpCards(newGameState.cardState());
        updateLeftCardsPercentage(newGameState.cardState());
        updateLeftTicketsPercentage(newGameState);
        updateRoutesOwned(newGameState);

        //Groupe 2, état publique des joueurs
        updateTicketCount(newGameState);
        updateCardCount(newGameState);
        updateCarCount(newGameState);
        updateClaimPoints(newGameState);

        //Groupe 3, état privé du joueur
        updateTickets(newPlayerState);
        updateCards(newPlayerState);
        updateRoutes(newGameState, newPlayerState);
    }

    //groupe 1
    private void updateLeftCardsPercentage(PublicCardState cardState) {
        double percentage = ((double)cardState.deckSize() / (double) Constants.TOTAL_CARDS_COUNT) * 100;
        if (leftCardsPercentage.get() != percentage) {
            leftCardsPercentage.set((int)percentage);
        }
    }

    private void updateLeftTicketsPercentage(PublicGameState gameState) {
        double percentage = ((double)gameState.ticketsCount() / (double)ChMap.tickets().size()) * 100;
        if (leftTicketsPercentage.get() != percentage) {
            leftTicketsPercentage.set((int)percentage);
        }
    }

    private void updateFaceUpCards(PublicCardState cardState) {
        for (int slot : FACE_UP_CARD_SLOTS) {
            Card newCard = cardState.faceUpCard(slot);
            if (faceUpCards.get(slot).get() != newCard) {
                faceUpCards.get(slot).set(newCard);
            }
        }
    }

    private void updateRoutesOwned(PublicGameState gameState) {
        for (PlayerId playerId : PlayerId.ALL) {
            for (Route route : gameState.playerState(playerId).routes()) {
                if (routesOwned.get(route).get() != playerId) {
                    routesOwned.get(route).set(playerId);
                }
            }
        }
    }

    private void updateTicketCount(PublicGameState gameState) {
        for (PlayerId playerId : PlayerId.ALL) {
            if (ticketCount.get(playerId).get() != gameState.playerState(playerId).ticketCount()) {
                ticketCount.get(playerId).set(gameState.playerState(playerId).ticketCount());
            }
        }
    }

    private void updateCardCount(PublicGameState gameState) {
        for (PlayerId playerId : PlayerId.ALL) {
            if (cardCount.get(playerId).get() != gameState.playerState(playerId).cardCount()) {
                cardCount.get(playerId).set(gameState.playerState(playerId).cardCount());
            }
        }
    }

    private void updateCarCount(PublicGameState gameState) {
        for (PlayerId playerId : PlayerId.ALL) {
            if (carCount.get(playerId).get() != gameState.playerState(playerId).carCount()) {
                carCount.get(playerId).set(gameState.playerState(playerId).carCount());
            }
        }
    }

    private void updateClaimPoints(PublicGameState gameState) {
        for (PlayerId playerId : PlayerId.ALL) {
            if (claimPoints.get(playerId).get() != gameState.playerState(playerId).claimPoints()) {
                claimPoints.get(playerId).set(gameState.playerState(playerId).claimPoints());
            }
        }
    }

    //Groupe 3
    private void updateTickets(PlayerState playerState) {
        if (!tickets.equals(playerState.tickets().toList())) {
            tickets.setAll(playerState.tickets().toList());
        }
    }

    private void updateCards(PlayerState playerState) {
        for (Card card : playerState.cards()) {
            if (cards.get(card).get() != playerState.cards().countOf(card)) {
                cards.get(card).set(playerState.cards().countOf(card));
            }
        }
    }

    private void updateRoutes(PublicGameState gameState, PlayerState playerState) {
        for (Route route : routes.keySet()) {
            boolean isRouteClaimable = (gameState.currentPlayerId().equals(player) && !gameState.claimedRoutes().contains(route) && checkClaimDoubleRoute(route) && playerState.canClaimRoute(route));
            if (routes.get(route).get() != isRouteClaimable) {
                routes.get(route).set(isRouteClaimable);
            }
        }
    }

    /**
     * Vérifier que la route ne fait pas partie de la liste des routes double, ou le cas échéant si la route double associée n'a pas été price
     *
     * @param route Route à contrôler
     * @return Boolean vrai si la route n appartient à personne et, dans le cas d une route double, sa voisine non plus.
     */
    private boolean checkClaimDoubleRoute(Route route) {
        return !doubleRoute.containsKey(route) || routesOwned.get(doubleRoute.get(route)).get() == null;
    }

    /**
     * Récupère toutes les routes doubles
     *
     * @return Un map avec lid dune route et la deuxième route
     */
    private Map<Route, Route> getAllDoubleRoute() {
        Map<Route, Route> doubleRoute = new HashMap<>();
        for (Route route1 : ChMap.routes()) {
            for (Route route2 : ChMap.routes()) {
                //l’ordre des gares sera toujours le même, car comme dit à la §3.2 de l’étape 2, ce qui rend inutile la deuxième partie du contrôle
                if (!route1.equals(route2) && (route1.station1().equals(route2.station1()) && route1.station2().equals(route2.station2()) || (route1.station2().equals(route2.station1()) && route1.station1().equals(route2.station2())))) {
                    doubleRoute.put(route1, route2);
                }
            }
        }
        return doubleRoute;
    }

    //groupe 1

    /**
     * Carte face visible
     *
     * @param slot emplacement
     * @return carte
     */
    public ReadOnlyObjectProperty<Card> faceUpCard(int slot) {
        return faceUpCards.get(slot);
    }

    /**
     * le pourcentage de cartes restant dans la pioche
     *
     * @return le pourcentage de cartes restant dans la pioche
     */
    public ReadOnlyIntegerProperty leftCardsPercentage() {
        return leftCardsPercentage;
    }

    /**
     * @return le pourcentage de tickets restant dans la pioche,
     */
    public ReadOnlyIntegerProperty leftTicketsPercentage() {
        return leftTicketsPercentage;
    }

    /**
     * Retourne le propriétaire de la route donnée
     *
     * @param route route
     * @return propriétaire de la route
     */
    public ReadOnlyObjectProperty<PlayerId> routeOwner(Route route) {
        return routesOwned.get(route);
    }

    //Groupe 2

    /**
     * le nombre de cartes que le joueur a en main
     *
     * @param playerId identité du joueur
     * @return nombre de cartes
     */
    public ReadOnlyIntegerProperty cardCount(PlayerId playerId) {
        return cardCount.get(playerId);
    }

    /**
     * le nombre de billets que le joueur a en main
     *
     * @param playerId identité du joueur
     * @return nombre de billets
     */
    public ReadOnlyIntegerProperty ticketCount(PlayerId playerId) {
        return ticketCount.get(playerId);
    }

    /**
     * le nombre de wagons dont le joueur dispose
     *
     * @param playerId identité du joueur
     * @return nombre de wagons restant
     */
    public ReadOnlyIntegerProperty carCount(PlayerId playerId) {
        return carCount.get(playerId);
    }

    /**
     * le nombre de points de construction obtenu par le joueur
     *
     * @param playerId identité du joueur
     * @return nombre de points de construction
     */
    public ReadOnlyIntegerProperty claimedPoints(PlayerId playerId) {
        return claimPoints.get(playerId);
    }

    //Groupe 3

    /**
     * Liste de billets appartenant au joueur
     *
     * @return Liste de billet
     */
    public ObservableList<Ticket> tickets() {
        return tickets;
    }

    /**
     * Nombre de cartes dans la main du joueur correspondant à la carte donnée
     *
     * @param card Carte
     * @return Nombre d' occurrence de la carte
     */
    public ReadOnlyIntegerProperty cardsCountOf(Card card) {
        return cards.get(card);
    }

    /**
     * Contrôle si le joueur peut prendre possession de la route donnée
     *
     * @param route Route a vérifier
     * @return vrai si le joueur peut prendre la route
     */
    public ReadOnlyBooleanProperty canClaimRoute(Route route) {
        return routes.get(route);
    }


    //Méthode de PublicGameState et de PlayerState

    /**
     * Appelle à la méthode canDrawTickets de l 'état courant de PublicGameState
     *
     * @return vrai si le joueur peut tirer des tickets
     */
    public boolean canDrawTickets() {
        return currentPublicGameState.canDrawTickets();
    }

    /**
     * Appelle à la méthode canDrawCards de l 'état courant de PublicGameState
     *
     * @return vrai si le joueur peut tirer des cartes
     */
    public boolean canDrawCards() {
        return currentPublicGameState.canDrawCards();
    }

    /**
     * Appelle à la méthode possibleClaimCards de l 'état courant de PlayerState
     *
     * @param route Route
     * @return les cartes que le joueur peut utiliser pour s' emparer de la route
     */
    public List<SortedBag<Card>> possibleClaimCards(Route route) {
        return currentPlayerState.possibleClaimCards(route);
    }
}
