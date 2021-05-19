package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.game.PlayerState;
import ch.epfl.tchu.game.PublicGameState;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Map;

import static ch.epfl.tchu.gui.StringsFr.GAME_SPECTATOR_NAME;

/**
 * Créé le 19.05.2021 à 11:46
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public class GraphicalSpectator {
    private final ObservableGameState gameState;
    private final ObservableList<Text> observableText;

    public GraphicalSpectator(PlayerId playerId, Map<PlayerId, String> playerNames, boolean showHand) {
        assert Platform.isFxApplicationThread();
        gameState = new ObservableGameState(playerId);

        Stage mainWindow = new Stage();
        mainWindow.setTitle(GAME_SPECTATOR_NAME);

        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane);
        mainWindow.setScene(scene);


        observableText = FXCollections.observableArrayList();

        Pane mapView = MapViewCreator.createMapView(gameState, new SimpleObjectProperty<>(),null);
        VBox infoView = InfoViewCreator.createInfoView(playerId, playerNames, gameState, observableText);
        VBox deckView = DecksViewCreator.createCardsView(gameState, new SimpleObjectProperty<>(), new SimpleObjectProperty<>());
        borderPane.centerProperty().setValue(mapView);
        if (showHand) {
            HBox handView = DecksViewCreator.createHandView(gameState);
            borderPane.bottomProperty().setValue(handView);
        }
        borderPane.leftProperty().setValue(infoView);
        borderPane.rightProperty().setValue(deckView);

        mainWindow.show();

        /*mainWindow.setOnCloseRequest(windowsEvent -> {
            System.exit(0);
        });*/
    }

    /**
     * Appelle la méthode setState sur l'état observable, qui met à jour la totalité des propriétés
     *
     * @param newGameState   la partie publique du jeu
     * @param newPlayerState l 'état complet du joueur auquel elle correspond
     */
    public void setState(PublicGameState newGameState, PlayerState newPlayerState) {
        assert Platform.isFxApplicationThread();
        gameState.setState(newGameState, newPlayerState);
    }

    /**
     * Ajoutant le message au bas des informations sur le déroulement de la partie
     *
     * @param message message informant sur l'état de la partie
     */
    public void receiveInfo(String message) {
        assert Platform.isFxApplicationThread();
        if (observableText.size() >= 5) {
            observableText.remove(0);
        }
        observableText.add(new Text(message));
    }
}
