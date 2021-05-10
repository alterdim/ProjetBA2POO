package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;

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
//        testPlayer.claimedRoute();


        List<Card> cards = new ArrayList<>();
        cards.add(Card.LOCOMOTIVE);
        cards.add(Card.BLUE);
        cards.add(Card.BLACK);
        cards.add(Card.WHITE);
        cards.add(Card.GREEN);
        int x = 0;
        int y = 0;

        var publicC = new PublicCardState(cards, x, y);

        Map<PlayerId, PublicPlayerState> map = new TreeMap<>();
        map.put(PLAYER_1, new PublicPlayerState(0, 0, List.of(ChMap.routes().get(0))));
        map.put(PLAYER_2, new PublicPlayerState(0, 0, List.of(ChMap.routes().get(1))));

        new PublicGameState(0, publicC, PLAYER_1, map, null);

        Station station1 = new Station(1, "station1");
        Station station2 = new Station(2, "station2");
        Route route = new Route("rte", station1, station2, 2, Route.Level.UNDERGROUND, null);


        var cardsplayer = new SortedBag.Builder<Card>()
                .add(Card.ORANGE)
                .add(Card.ORANGE)
                .add(Card.RED)
                .add(Card.LOCOMOTIVE)
                .build();

        var tickets = new SortedBag.Builder<Ticket>()
                .add(ChMap.tickets().get(1))
                .add(ChMap.tickets().get(2))
                .add(ChMap.tickets().get(3))
                .add(ChMap.tickets().get(4))
                .add(ChMap.tickets().get(5))
                .build();

        var playerState = new  PlayerState(tickets, cardsplayer, List.of());
        System.out.println("Client done!");
    }

    private final static class TestPlayer implements Player {

        private final Deque<SortedBag<Ticket>> allTicketsSeen = new ArrayDeque<>();


        @Override
        public void initPlayers(PlayerId ownId,
                                Map<PlayerId, String> names) {
//            System.out.printf("ownId: %s\n", ownId);
//            System.out.printf("playerNames: %s\n", names);
        }

        @Override
        public void receiveInfo(String info) {
            System.out.println("receiveInfo "+info);
        }

        @Override
        public void updateState(PublicGameState newState, PlayerState ownState) {
            System.out.println(newState.cardState().faceUpCard(2));
        }

        @Override
        public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
            allTicketsSeen.addLast(tickets);
//            System.out.println("InitialTickets "+tickets);
        }

        @Override
        public SortedBag<Ticket> chooseInitialTickets() {
//            System.out.println("Tickets choisi"+allTicketsSeen.peekFirst());
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
//            System.out.println("Claimed route "+route.id());
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