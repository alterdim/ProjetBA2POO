package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

import java.util.*;

import static ch.epfl.tchu.game.PlayerId.*;

/**
 * Créé le 10.05.2021 à 16:12
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public final class Stage11Test extends Application {
    public static void main(String[] args) { launch(args); }
    private List<Player> spectators;

    @Override
    public void start(Stage primaryStage) {
        spectators=new ArrayList<>();
        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
        Map<PlayerId, String> names = new EnumMap<>(PlayerId.class);
        names.put(PLAYER_1, "Ada");
        names.put(PLAYER_2, "Bob");
        names.put(PLAYER_3, "Charles");

        Map<PlayerId, Player> players =
                Map.of(PLAYER_1, new GraphicalPlayerAdapter(),
                        PLAYER_2, new GraphicalPlayerAdapter(),
                        PLAYER_3, new GraphicalPlayerAdapter()
                );
        Random rng = new Random();


        // set title for the stage
        primaryStage.setTitle("tCHu");

        // create a tile pane
        TilePane pane = new TilePane();


        // create a text input dialog
        TextInputDialog playerNameInputDialog = new TextInputDialog();
        playerNameInputDialog.setTitle("Pseudo");
        playerNameInputDialog.setHeaderText("Choissez votre pseudo");
        playerNameInputDialog.setContentText("Pseudo : ");
        playerNameInputDialog.setGraphic(null);


        // create a label to show the input in text dialog
        Label pseudoPlayer = new Label(names.get(PLAYER_1));
        // create a button
        Button pseudoButton = new Button("Pseudo");

        // create and set on action of event
        pseudoButton.setOnAction(e -> {
            // show the text input dialog
            Optional<String> result =playerNameInputDialog.showAndWait();

            if (result.isPresent()) {
                // set the text of the label
                String username = playerNameInputDialog.getEditor().getText();
                if (username.length() > 0) {
                    names.put(PLAYER_1, username);
                    pseudoPlayer.setText(username);
                }
            }
        });

        // add button and label
        pane.getChildren().add(pseudoButton);
        pane.getChildren().add(pseudoPlayer);

        Player s1 = new GraphicalSpectatorAdapter(/*names,*/ true);
        spectators.add(s1);
        s1.initPlayers(PLAYER_1, names);

        // create a button
        Button startButton = new Button("Start");
        startButton.setOnAction(e -> {
            System.out.println("start");
            new Thread(() -> Game.play(players, names, tickets, rng, spectators))
                    .start();
//            primaryStage.hide();

        });
        pane.getChildren().add(startButton);

        // create a scene
        Scene scene = new Scene(pane, 500, 300);

        // set the scene
        primaryStage.setScene(scene);

        primaryStage.show();
    }
}