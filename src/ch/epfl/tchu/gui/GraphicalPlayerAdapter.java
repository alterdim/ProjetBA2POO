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

    private final BlockingQueue<SortedBag<Ticket>> ticketQueue = new ArrayBlockingQueue<>(1);
    private final BlockingQueue<Integer> intQueue = new ArrayBlockingQueue<>(1);
    private final BlockingQueue<TurnKind> turnKindQueue = new ArrayBlockingQueue<>(1);
    private final BlockingQueue<Route> routeQueue= new ArrayBlockingQueue<>(1);
    private final BlockingQueue<SortedBag<Card>> cardQueue = new ArrayBlockingQueue<>(1);

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
        runLater(() -> graphicalPlayer.chooseTickets(tickets, ticketQueue::add));
    }

    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        return take(ticketQueue);
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
        return take(turnKindQueue);
    }

    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        return take(ticketQueue);
    }

    @Override
    public int drawSlot() {
        if (intQueue.isEmpty()) {
            runLater(() -> graphicalPlayer.drawCard(slot -> put(intQueue, slot)));
        }
        return take(intQueue);
    }

    @Override
    public Route claimedRoute() {
        return take(routeQueue);
    }

    @Override
    public SortedBag<Card> initialClaimCards() {
        return take(cardQueue);
    }

    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        runLater(() -> graphicalPlayer.chooseAdditionalCards(options, cards -> put(cardQueue, cards)));
        return take(cardQueue);
    }

    private <T> T take(BlockingQueue<T> queue) {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            throw new Error();
        }
    }

    private <T> void put(BlockingQueue<T> queue, T item) {
        try {
            queue.put(item);
        } catch (InterruptedException e) {
            throw new Error();
        }
    }
}
