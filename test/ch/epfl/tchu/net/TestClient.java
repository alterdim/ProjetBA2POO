package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;

/**
 * Créé le 19.04.2021 à 13:51
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public final class TestClient {
    public static void main(String[] args) {
        System.out.println("Starting client!");
        TestPlayer testPlayer = new TestPlayer();
        RemotePlayerClient playerClient =
                new RemotePlayerClient(testPlayer,
                        "localhost",
                        5108);
        playerClient.run();
        testPlayer.claimedRoute();
        System.out.println("Client done!");
    }

    private final static class TestPlayer implements Player {

        private final Deque<SortedBag<Ticket>> allTicketsSeen = new ArrayDeque<>();


        @Override
        public void initPlayers(PlayerId ownId,
                                Map<PlayerId, String> names) {
            System.out.printf("ownId: %s\n", ownId);
            System.out.printf("playerNames: %s\n", names);
        }

        @Override
        public void receiveInfo(String info) {
            System.out.println("receiveInfo "+info);
        }

        @Override
        public void updateState(PublicGameState newState, PlayerState ownState) {

        }

        @Override
        public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
            allTicketsSeen.addLast(tickets);
            System.out.println("InitialTickets "+tickets);
        }

        @Override
        public SortedBag<Ticket> chooseInitialTickets() {
            System.out.println("Tickets choisi"+allTicketsSeen.peekFirst());
            return allTicketsSeen.peekFirst();
        }

        @Override
        public TurnKind nextTurn() {
            return null;
        }

        @Override
        public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
            return null;
        }

        @Override
        public int drawSlot() {
            return 0;
        }

        @Override
        public Route claimedRoute() {
            var route = ChMap.routes().get(0);
            System.out.println("Claimed route "+route.id());
            return route;
        }

        @Override
        public SortedBag<Card> initialClaimCards() {
            return null;
        }

        @Override
        public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
            return null;
        }
    }
}