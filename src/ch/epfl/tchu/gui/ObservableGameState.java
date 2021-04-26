package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.*;
import javafx.beans.property.*;

import java.util.*;

import static ch.epfl.tchu.game.Constants.FACE_UP_CARD_SLOTS;

/**
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
    private final Map<Route, ObjectProperty<PlayerId>> routesOwner;

    //Groupe 2, état publique des joueurs
    private final Map<PlayerId, IntegerProperty> ticketCount;
    private final Map<PlayerId, IntegerProperty> cardCount;
    private final Map<PlayerId, IntegerProperty> carCount;
    private final Map<PlayerId, IntegerProperty> claimPoints;

    //Groupe 3, état privé du joueur player
    private final ListProperty<Ticket> tickets;//TODO vérifier
    private final Map<Card, IntegerProperty> cards;
    private final Map<Route, BooleanProperty> routes;

    public ObservableGameState(PlayerId player) {
        this.player = player;
        //groupe 1
        this.faceUpCards = createFaceUpCards();
        this.leftTicketsPercentage = new SimpleIntegerProperty(0);
        this.leftCardsPercentage = new SimpleIntegerProperty(0);
        this.routesOwner = createRoutesOwner();

        //groupe 2
        this.ticketCount = createMap();
        this.cardCount = createMap();
        this.carCount = createMap();
        this.claimPoints = createMap();

        //groupe 3
        this.tickets = createTickets();
        this.cards = createCards();
        this.routes = createRoutes();
    }

    //groupe 1
    private static List<ObjectProperty<Card>> createFaceUpCards() {
        List<ObjectProperty<Card>> faceUpCardsList = new ArrayList<>();
        for (int slot : FACE_UP_CARD_SLOTS) {
            faceUpCardsList.set(slot, new SimpleObjectProperty<>(null));
        }
        return faceUpCardsList;
    }

    private static Map<Route, ObjectProperty<PlayerId>> createRoutesOwner() {
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
    private static ListProperty<Ticket> createTickets() {
        return new SimpleListProperty<>(null);
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

    public void setState(PublicGameState newGameState, PlayerState newPlayerState) {
        //Groupe 1, état publique de la partie
        updateFaceUpCards(newGameState.cardState());
        updateLeftCardsPercentage(newGameState.cardState());
        updateLeftTicketsPercentage(newGameState);
        updateRoutesOwner(newGameState);

        //Groupe 2, état publique des joueurs
        updateTicketCount(newGameState);
        updateCardCount(newGameState);
        updateCarCount(newGameState);
        updateClaimPoints(newGameState);

        //Groupe 3, état privé du joueur
        updateTickets(newPlayerState);
        updateCards(newPlayerState);
    }

    //groupe 1
    private void updateLeftCardsPercentage(PublicCardState cardState) {
        int percentage = (cardState.deckSize() / cardState.totalSize()) * 100;
        if (leftCardsPercentage.get() != percentage) {
            leftCardsPercentage.set(percentage);
        }
    }

    private void updateLeftTicketsPercentage(PublicGameState gameState) {
        int percentage = (gameState.ticketsCount() / ChMap.tickets().size()) * 100;
        if (leftTicketsPercentage.get() != percentage) {
            leftTicketsPercentage.set(percentage);
        }
    }

    private void updateFaceUpCards(PublicCardState cardState) {
        for (int slot : FACE_UP_CARD_SLOTS) {
            Card newCard = cardState.faceUpCard(slot);
            if (!faceUpCards.get(slot).get().equals(newCard)) {
                faceUpCards.get(slot).set(newCard);
            }
        }
    }

    private void updateRoutesOwner(PublicGameState gameState) {
        for (PlayerId playerId : PlayerId.ALL) {
            for (Route route : gameState.playerState(playerId).routes()) {
                if (!routesOwner.get(route).get().equals(playerId)) {
                    routesOwner.get(route).set(playerId);
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
            tickets.get().setAll(playerState.tickets().toList());
//        tickets.addAll(playerState.tickets().toList());
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
        for (Route route : routesOwner.keySet()) {
            if (gameState.currentPlayerId().equals(player) && !gameState.claimedRoutes().contains(route) && playerState.canClaimRoute(route)) {
//                if (route.)//TODO vérifier si pas une route double
                    routes.get(route).set(true);
            } else {
                routes.get(route).set(false);
            }
        }
    }

    private boolean isRouteDoubled(){
        Map<Route, Route> doubleRoute = new HashMap<>();
        for (Route route1 : ChMap.routes()) {
            for (Route route2 : ChMap.routes()) {
                if ((route1.station1().equals(route2.station1()) && route1.station2().equals(route2.station2())) || (route1.station2().equals(route2.station1()) && route1.station1().equals(route2.station2()))){
//                    if (doubleRoute.get(route1.station1()).equals(route2.station1()) && doubleRoute.get(route1.station2()).equals(route2.station2()))
                    doubleRoute.put(route1, route2);
                    //TODO is each value in double?
                }
            }
        }
        return false;
    }

    //groupe 1
    public ReadOnlyObjectProperty<Card> faceUpCard(int slot) {
        return faceUpCards.get(slot);
    }

    public ReadOnlyIntegerProperty leftCardsPercentage() {
        return leftCardsPercentage;
    }

    public ReadOnlyIntegerProperty leftTicketsPercentage() {
        return leftTicketsPercentage;
    }

    public ReadOnlyObjectProperty<PlayerId> routeOwner(Route route) {
        return routesOwner.get(route);
    }


    //Groupe 2
    public ReadOnlyIntegerProperty cardCount(PlayerId playerId) {
        return cardCount.get(playerId);
    }

    public ReadOnlyIntegerProperty ticketCount(PlayerId playerId) {
        return ticketCount.get(playerId);
    }

    public ReadOnlyIntegerProperty carCount(PlayerId playerId) {
        return carCount.get(playerId);
    }

    public ReadOnlyIntegerProperty claimedPoints(PlayerId playerId) {
        return claimPoints.get(playerId);
    }

    //Groupe 3
    public ReadOnlyListProperty<Ticket> tickets() {
        return tickets;
    }

    public ReadOnlyIntegerProperty cardsCountOf(Card card) {
        return cards.get(card);
    }

}
