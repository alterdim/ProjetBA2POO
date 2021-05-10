package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.gui.GraphicalPlayerAdapter;
import javafx.application.Application;
import javafx.stage.Stage;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * Créé le 10.05.2021 à 16:20
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public class ServerMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        List<String> parameters = getParameters().getRaw();
        Map<PlayerId, String> playersNameMap = new EnumMap<>(PlayerId.class);
        playersNameMap.put(PlayerId.PLAYER_1, "Ada");
        playersNameMap.put(PlayerId.PLAYER_2, "Charles");
        switch (parameters.size()) {
            case 2:
                playersNameMap.put(PlayerId.PLAYER_1, parameters.get(0));
                playersNameMap.put(PlayerId.PLAYER_2, parameters.get(1));
                break;
            case 1:
                playersNameMap.put(PlayerId.PLAYER_1, parameters.get(0));
                break;
        }
        try (ServerSocket serverSocket = new ServerSocket(5108);
             Socket socket = serverSocket.accept()) {
            Map<PlayerId, Player> playersMap = new EnumMap<>(PlayerId.class);
            Player p1 = new GraphicalPlayerAdapter();
            playersMap.put(PlayerId.PLAYER_1, p1);
            Player p2 = new RemotePlayerProxy(socket);
            playersMap.put(PlayerId.PLAYER_2, p2);

            Game.play(playersMap, playersNameMap, SortedBag.of(ChMap.tickets()), new Random());
        }
    }
}
