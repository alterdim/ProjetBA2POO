package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.Constants;
import ch.epfl.tchu.game.PlayerId;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableStringValue;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.List;
import java.util.Map;

/**
 * Créé le 03.05.2021 à 13:07
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
abstract class InfoViewCreator {
    public static VBox createInfoView(PlayerId playerId, Map<PlayerId, String> playerNames, ObservableGameState observableGameState, List<ObservableStringValue> observableText) {
        VBox canvasBox = new VBox();
        canvasBox.getStylesheets().addAll("info.css", "colors.css");

        VBox playersBox = new VBox();
        playersBox.setId("player-stats");

        for (PlayerId id : playerNames.keySet()) {
            TextFlow textFlow = new TextFlow();
            textFlow.getStyleClass().add("PLAYER_n");

            Circle circle = new Circle();
            circle.getStyleClass().add("filled");
            Text text = new Text();
            text.textProperty().bind(Bindings.format(
                    StringsFr.PLAYER_STATS,
                    playerNames.get(id),
                    observableGameState.ticketCount(id),
                    observableGameState.cardCount(id),
                    observableGameState.carCount(id),
                    observableGameState.claimedPoints(id))
            );

            textFlow.getChildren().addAll(circle, text);

            playersBox.getChildren().add(textFlow);
        }
        canvasBox.getChildren().add(playersBox);

        Separator separator = new Separator();
        canvasBox.getChildren().add(separator);

        TextFlow messageTextFlow = new TextFlow();
        messageTextFlow.setId("game-info");

        for (int i = 0; i < Constants.MAX_MESSAGE_DISPLAYED; i++) {
            Text text = new Text();
//            text.textProperty().bind(Bindings.bindContent(observableText, ));
            messageTextFlow.getChildren().add(text);
        }

        canvasBox.getChildren().add(messageTextFlow);

        return null;
    }
}
