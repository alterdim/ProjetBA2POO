package ch.epfl.tchu.gui;

import ch.epfl.tchu.net.RemotePlayerClient;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;

/**
 * Contient le programme principal du client tCHu.
 *
 * Créé le 10.05.2021 à 15:29
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public class ClientMain extends Application {
    /**
     * Démarre l' application graphique
     * @param args Paramètres de l' application
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Démarre le client
     * @param primaryStage Scène principale de l' interface (ignoré dans le cas présent)
     */
    @Override
    public void start(Stage primaryStage) {
        List<String> parameters = getParameters().getRaw();
        String address = "localhost";
        int port = 5108;
        switch (parameters.size()) {
            case 2:
                address = parameters.get(0);
                port = Integer.parseInt(parameters.get(1));
                break;
            case 1:
                address = parameters.get(0);
                break;
        }

        RemotePlayerClient remoteClient = new RemotePlayerClient(new GraphicalPlayerAdapter(), address, port);
        new Thread(remoteClient::run).start();
    }
}
