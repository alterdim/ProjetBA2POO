package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

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


            System.out.println("Claimed Route "+ playerProxy.claimedRoute().id());

            SortedBag.Builder s = new SortedBag.Builder();
            s.add(ChMap.tickets().get(0));
            s.add(ChMap.tickets().get(1));
            s.add(ChMap.tickets().get(2));
            s.add(ChMap.tickets().get(3));
            s.add(ChMap.tickets().get(4));
            playerProxy.setInitialTicketChoice(s.build());

            System.out.println("Initial tickets choice"+ playerProxy.chooseInitialTickets());
        }
        System.out.println("Server done!");
    }
}
