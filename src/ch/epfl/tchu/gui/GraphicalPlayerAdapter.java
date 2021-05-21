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
 *
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

    /**
     * Constructeur vide
     */
    public GraphicalPlayerAdapter() {
    }

    /**
     * est appelée au début de la partie pour communiquer au joueur son nom et la liste de tous les joueurs (le siens inclus)
     * @param ownId sa propre identité
     * @param playerNames noms des différents joueurs, le sien inclus
     */
    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        runLater(() -> graphicalPlayer = new GraphicalPlayer(ownId, playerNames));
    }

    /**
     * est appelée chaque fois qu'une information doit être communiquée au joueur au cours de la partie (sous forme de chaine de caractères)
     * @param info information a communiquer
     */
    @Override
    public void receiveInfo(String info) {
        runLater(() -> graphicalPlayer.receiveInfo(info));
    }

    /**
     * Informe le joueur de la nouvelle composante publique lors d'un changement d'état
     * @param newState nouvel état
     * @param ownState son propre état
     */
    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {
        runLater(() -> graphicalPlayer.setState(newState, ownState));
    }

    /**
     * communiquer au joueur les cinq billets qui lui ont été distribués au début de la partie
     * @param tickets SortedBag de 5 tickets
     */
    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
        runLater(() -> graphicalPlayer.chooseTickets(tickets, ticketQueue::add));
    }

    /**
     * demande au joueur quels billets qu'on lui a distribué initialement(au début de la partie) il garde
     */
    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        return take(ticketQueue);
    }

    /**
     * Quel action le joueur décide de faire durant son tour
     * @return Type d'action effectuée durant le tour
     */
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

    /**
     * est appelée lorsque le joueur a décidé de tirer des billets supplémentaires en cours de partie, afin de lui communiquer les billets tirés et de savoir lesquels il garde
     * @param options les billets tirés
     * @return les billets garder
     */
    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        runLater(() -> graphicalPlayer.chooseTickets(options, ticketQueue::add));
        return take(ticketQueue);
    }

    /**
     * Lorsque DRAW_CARDS est choisi, détermine depuis quel emplacement les cartes seront prises : cartes visible ou la pioche
     * @return si carte visible entre 0 et 4 inclus, ou si pioche Constants.DECK_SLOT (c-à-d -1)
     */
    @Override
    public int drawSlot() {
        if (intQueue.isEmpty()) {
            runLater(() -> graphicalPlayer.drawCard(slot -> put(intQueue, slot)));
        }
        return take(intQueue);
    }

    /**
     * est appelée lorsque le joueur a décidé de (tenter de) s'emparer d'une route, afin de savoir de quelle route il s'agit
     */
    @Override
    public Route claimedRoute() {
        return take(routeQueue);
    }

    /**
     * est appelée lorsque le joueur a décidé de (tenter de) s'emparer d'une route, afin de savoir quelle(s) carte(s) il désire initialement utiliser pour cela
     * @return carte(s) que le joueur désir initialement utiliser
     */
    @Override
    public SortedBag<Card> initialClaimCards() {
        return take(cardQueue);
    }

    /**
     * est appelée lorsque le joueur a décidé de tenter de s'emparer d'un tunnel et que des cartes additionnelles sont nécessaires, afin de savoir quelle(s) carte(s) il désire utiliser pour cela
     * @param options carte(s) additionnelles disponibles
     * @return carte(s) additionnelles choisies par le joueur si non vide, sinon (multiensemble vide) cela signifie que le joueur ne désire pas (ou ne peut pas) choisir l'une de ces possibilités
     */
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
