package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.net.RemotePlayerProxy;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
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


        //Titre du stage
        primaryStage.setTitle(GAME_SERVER_NAME);
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
            Optional<String> result =playerNameInputDialog.showAndWait();

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

        Button startButton = new Button(START);
        startButton.setOnAction(e -> {
            startGame(playersNameMap);
        });
        pane.getChildren().add(startButton);

        Scene scene = new Scene(pane, 500, 300);

        primaryStage.setScene(scene);

        primaryStage.show();

    }

    private void startGame(Map<PlayerId, String> playersNameMap){
        new Thread(()->{
            try (ServerSocket serverSocket = new ServerSocket(5108);
                 Socket socket = serverSocket.accept()) {
                Map<PlayerId, Player> playersMap = new EnumMap<>(PlayerId.class);
                Player p1 = new GraphicalPlayerAdapter();
                playersMap.put(PLAYER_1, p1);
                Player p2 = new RemotePlayerProxy(socket);
                playersMap.put(PlayerId.PLAYER_2, p2);

                Game.play(playersMap, playersNameMap, SortedBag.of(ChMap.tickets()), new Random());
            } catch (IOException e) {
                throw new Error(e);
            }
        }).start();
    }
}
