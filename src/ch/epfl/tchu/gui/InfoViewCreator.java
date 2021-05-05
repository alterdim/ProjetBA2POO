package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.Constants;
import ch.epfl.tchu.game.PlayerId;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.Map;

/**
 * Construction de la vue des informations.
 * Créé le 03.05.2021 à 13:07
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
abstract class InfoViewCreator {
    public static final int MAX_MESSAGE_DISPLAYED = 5;
    public static final int PLAYER_CIRCLE_RADIUS = 5;


    /**
     * Informations concernant l'état de la partie
     *
     * @param ownerPlayerId       l'identité du joueur auquel l'interface correspond
     * @param playerNames         la table associative des noms des joueurs
     * @param observableGameState l'état de jeu observable
     * @param observableText      une liste (observable) contenant les informations sur le déroulement de la partie, sous la forme d'instances de Text.
     * @return VBox, vue contant des informations sur l'état de la partie
     */
    public static VBox createInfoView(PlayerId ownerPlayerId, Map<PlayerId, String> playerNames, ObservableGameState observableGameState, ObservableList<Text> observableText) {
        VBox canvasBox = new VBox();
        canvasBox.getStylesheets().addAll("info.css", "colors.css");

        canvasBox.getChildren().add(playerStatsBox(ownerPlayerId, playerNames, observableGameState));

        canvasBox.getChildren().add(new Separator());

        TextFlow messageTextFlow = new TextFlow();
        messageTextFlow.setId("game-info");

        for (int i = 0; i < MAX_MESSAGE_DISPLAYED; i++) {
            Text text = new Text();

//            text.textProperty().bind(Bindings.bindContent(messageTextFlow.getChildren(), observableText));
            //TODO comment bind
            messageTextFlow.getChildren().add(text);
        }
        Bindings.bindContent(messageTextFlow.getChildren(), observableText);
        canvasBox.getChildren().add(messageTextFlow);

        return canvasBox;
    }

    private static TextFlow playerProperties(PlayerId playerId, Map<PlayerId, String> playerNames, ObservableGameState observableGameState) {
        TextFlow textFlow = new TextFlow();
        textFlow.getStyleClass().add(playerId.name());

        Circle circle = new Circle();
        circle.getStyleClass().add("filled");
        circle.setRadius(PLAYER_CIRCLE_RADIUS);
        Text text = new Text();
        text.textProperty().bind(Bindings.format(
                StringsFr.PLAYER_STATS,
                playerNames.get(playerId),
                observableGameState.ticketCount(playerId),
                observableGameState.cardCount(playerId),
                observableGameState.carCount(playerId),
                observableGameState.claimedPoints(playerId))
        );

        textFlow.getChildren().addAll(circle, text);

        return textFlow;
    }

    private static VBox playerStatsBox(PlayerId ownerPlayerId, Map<PlayerId, String> playerNames, ObservableGameState observableGameState) {
        VBox playersBox = new VBox();
        playersBox.setId("player-stats");

        //Permettrait d'ajouter plus de joueurs
        PlayerId lastPlayerId = ownerPlayerId;
        for (int i = 0; i < PlayerId.COUNT; i++) {
            playersBox.getChildren().add(playerProperties(lastPlayerId, playerNames, observableGameState));
            lastPlayerId=lastPlayerId.next();
        }
//        playersBox.getChildren().add(playerProperties(ownerPlayerId, playerNames, observableGameState));
//        playersBox.getChildren().add(playerProperties(ownerPlayerId.next(), playerNames, observableGameState));

        return playersBox;
    }
}
