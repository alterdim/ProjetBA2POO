package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static javafx.application.Platform.runLater;

/**
 *
 * Adapte une instance de GraphicalPlayer en une valeur de type Player
 * Créé le 10.05.2021 à 14:49
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public class GraphicalPlayerAdapter implements Player {
    private GraphicalPlayer graphicalPlayer;

    private BlockingQueue<SortedBag<Ticket>> ticketQueue = new ArrayBlockingQueue<>(1);
    private BlockingQueue<Integer> intQueue = new ArrayBlockingQueue<>(1);
    private BlockingQueue<TurnKind> turnKindQueue = new ArrayBlockingQueue<>(1);
    private BlockingQueue<Route> routeQueue= new ArrayBlockingQueue<>(1);
    private BlockingQueue<SortedBag<Card>> cardQueue = new ArrayBlockingQueue<>(1);

    public GraphicalPlayerAdapter() {
    }

    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        runLater(() -> graphicalPlayer = new GraphicalPlayer(ownId, playerNames));
    }

    @Override
    public void receiveInfo(String info) {
        runLater(() -> graphicalPlayer.receiveInfo(info));
    }

    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {
        runLater(() -> graphicalPlayer.setState(newState, ownState));
    }

    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
        runLater(() -> graphicalPlayer.chooseTickets(tickets, handler -> ticketQueue.add(tickets)));
    }

    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        return (SortedBag<Ticket>) take(ticketQueue);
    }

    @Override
    public TurnKind nextTurn() {
        runLater(() -> graphicalPlayer.startTurn(() -> turnKindQueue.add(TurnKind.DRAW_TICKETS),
                (slot) -> {
                    turnKindQueue.add(TurnKind.DRAW_CARDS);
                    intQueue.add(slot);
                },
                ((route, cards) -> {
                    turnKindQueue.add(TurnKind.CLAIM_ROUTE);
                    routeQueue.add(route);
                    cardQueue.add(cards);
                })));
        return (TurnKind) take(turnKindQueue);
    }

    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        setInitialTicketChoice(options);
        return chooseInitialTickets();
    }

    @Override
    public int drawSlot() {
        if (intQueue.isEmpty()) {
            runLater(() -> graphicalPlayer.drawCard(new ActionHandlers.DrawCardHandler() {
                @Override
                public void onDrawCard(int slot) {
                    put(intQueue, slot);
                }
            }));
        }
        return (int) take(intQueue);
    }

    @Override
    public Route claimedRoute() {
        return (Route) take(routeQueue);
    }

    @Override
    public SortedBag<Card> initialClaimCards() {
        return (SortedBag<Card>) take(cardQueue);
    }

    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        runLater(() -> graphicalPlayer.chooseAdditionalCards(options, cards -> put(cardQueue, cards)));
        return (SortedBag<Card>) take(cardQueue);
    }

    private Object take(BlockingQueue queue) {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            throw new Error();
        }
    }

    private void put(BlockingQueue queue, Object item) {
        try {
            queue.put(item);
        } catch (InterruptedException e) {
            throw new Error();
        }
    }
}
