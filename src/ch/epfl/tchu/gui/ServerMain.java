package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.net.RemotePlayerProxy;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import static ch.epfl.tchu.game.PlayerId.*;
import static ch.epfl.tchu.gui.StringsFr.*;


/**
 * Contient le programme principal du serveur tCHu. .
 *
 * Créé le 10.05.2021 à 16:20
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public class ServerMain extends Application {

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
        Platform.setImplicitExit(false);
        List<String> parameters = getParameters().getRaw();
        Map<PlayerId, String> playersNameMap = new EnumMap<>(PlayerId.class);
        playersNameMap.put(PLAYER_1, "Ada");
        playersNameMap.put(PLAYER_2, "Charles");
        switch (parameters.size()) {
            case 2:
                playersNameMap.put(PLAYER_1, parameters.get(0));
                playersNameMap.put(PLAYER_2, parameters.get(1));
                break;
            case 1:
                playersNameMap.put(PLAYER_1, parameters.get(0));
                break;
        }


        Stage mainWindow = new Stage();
        //Titre du stage
        mainWindow.setTitle(GAME_SERVER_NAME);
        //Création d'un pane
        TilePane pane = new TilePane();

        //Création de l'inputDialog
        TextInputDialog playerNameInputDialog = new TextInputDialog();
        playerNameInputDialog.setTitle(NAME_CHOICE_TITLE);
        playerNameInputDialog.setHeaderText(CHOOSE_NAME_HEADER);
        playerNameInputDialog.setContentText(CHOOSE_NAME_CONTENT);
        playerNameInputDialog.setGraphic(null);


        Label pseudoPlayer = new Label(playersNameMap.get(PLAYER_1));
        Button pseudoButton = new Button(CHOOSE);
        pseudoButton.setOnAction(e -> {
            Optional<String> result = playerNameInputDialog.showAndWait();

            if (result.isPresent()) {
                String username = playerNameInputDialog.getEditor().getText();
                if (username.length() > 0) {
                    playersNameMap.put(PLAYER_1, username);
                    pseudoPlayer.setText(username);
                }
            }
        });
        pane.getChildren().add(pseudoButton);
        pane.getChildren().add(pseudoPlayer);
        Scene scene = new Scene(pane, 500, 300);

        Button startButton = new Button(START);
        startButton.setOnAction(e -> {
            mainWindow.hide();
            startGame(playersNameMap);
        });
        pane.getChildren().add(startButton);

        mainWindow.setScene(scene);
        mainWindow.show();

        mainWindow.setOnCloseRequest(windowsEvent -> System.exit(0));
    }

    private void startGame(Map<PlayerId, String> playersNameMap){
        new Thread(()->{
            try (ServerSocket serverSocket = new ServerSocket(5108);
                 Socket socket = serverSocket.accept()) {
                Map<PlayerId, Player> playersMap = new EnumMap<>(PlayerId.class);
                Player p1 = new GraphicalPlayerAdapter();
                playersMap.put(PLAYER_1, p1);
                RemotePlayerProxy p2 = new RemotePlayerProxy(socket);
                playersMap.put(PLAYER_2, p2);
                playersNameMap.put(PLAYER_2, p2.chooseUsername());

                Game.play(playersMap, playersNameMap, SortedBag.of(ChMap.tickets()), new Random());
            } catch (IOException e) {
                throw new Error(e);
            }
        }).start();
    }
}
