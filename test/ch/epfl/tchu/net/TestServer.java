package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;

/**
 * Créé le 19.04.2021 à 13:51
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public final class TestServer {
    public static void main(String[] args) throws IOException {
        System.out.println("Starting server!");
        try (ServerSocket serverSocket = new ServerSocket(5108);
             Socket socket = serverSocket.accept()) {
            Player playerProxy = new RemotePlayerProxy(socket);
            var playerNames = Map.of(PLAYER_1, "Ada",
                    PLAYER_2, "Charles");
            playerProxy.initPlayers(PLAYER_1, playerNames);


//            System.out.println("Claimed Route "+ playerProxy.claimedRoute().id());

            SortedBag.Builder s = new SortedBag.Builder();
            s.add(ChMap.tickets().get(0));
            s.add(ChMap.tickets().get(1));
            s.add(ChMap.tickets().get(2));
            s.add(ChMap.tickets().get(3));
            s.add(ChMap.tickets().get(4));
            playerProxy.setInitialTicketChoice(s.build());

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
            map.put(PLAYER_1, new PublicPlayerState(0, 0, List.of()));
            map.put(PLAYER_2, new PublicPlayerState(0, 0, List.of()));

            var p = new PublicGameState(0, publicC, PLAYER_1, map, null);

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

//            System.out.println("Initial tickets choice"+ playerProxy.chooseInitialTickets());

            playerProxy.updateState(p, playerState);
        }
        System.out.println("Server done!");
    }
}
