package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.Player;
import ch.epfl.tchu.net.RemotePlayerClient;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static ch.epfl.tchu.gui.StringsFr.*;
import static ch.epfl.tchu.gui.StringsFr.START;

/**
 * Créé le 19.05.2021 à 13:19
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public class SpectatorMain extends Application {
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
        AtomicReference<String> address = new AtomicReference<>("localhost");
        AtomicInteger port = new AtomicInteger(5109);

        Stage mainWindow = new Stage();
        //Titre du stage
        mainWindow.setTitle(GAME_CLIENT_NAME);
        //Création d'un pane
        TilePane pane = new TilePane();

        //Création de l'inputDialog
        TextInputDialog addressInputDialog = new TextInputDialog();
        addressInputDialog.setTitle(ADDRESS_CHOICE_TITLE);
        addressInputDialog.setHeaderText(CHOOSE_ADDRESS_HEADER);
        addressInputDialog.setContentText(CHOOSE_ADDRESS_CONTENT);
        addressInputDialog.setGraphic(null);


        Label addressLabel = new Label(address.get());
        Button addessButton = new Button(CHOOSE);
        addessButton.setOnAction(e -> {
            Optional<String> result =addressInputDialog.showAndWait();

            if (result.isPresent()) {
                String tempAddress = addressInputDialog.getEditor().getText();
                if (tempAddress.length() > 0) {
                    address.set(tempAddress);
                    addressLabel.setText(tempAddress);

                }
            }
        });
        pane.getChildren().add(addessButton);
        pane.getChildren().add(addressLabel);

        //Création de l'inputDialog
        TextInputDialog portInputDialog = new TextInputDialog();
        portInputDialog.setTitle(PORT_CHOICE_TITLE);
        portInputDialog.setHeaderText(CHOOSE_PORT_HEADER);
        portInputDialog.setContentText(CHOOSE_PORT_CONTENT);
        portInputDialog.setGraphic(null);


        Label portLabel = new Label(String.valueOf(port.get()));
        Button portButton = new Button(CHOOSE);
        portButton.setOnAction(e -> {
            Optional<String> result =portInputDialog.showAndWait();

            if (result.isPresent()) {
                String addressString = portInputDialog.getEditor().getText();
                if (addressString.length() > 0) {
                    port.set(Integer.parseInt(addressString));
                    portLabel.setText(addressString);
                }
            }
        });
        pane.getChildren().add(portButton);
        pane.getChildren().add(portLabel);


        TextInputDialog playerNameInputDialog = new TextInputDialog();
        playerNameInputDialog.setTitle(NAME_CHOICE_TITLE);
        playerNameInputDialog.setHeaderText(CHOOSE_NAME_HEADER);
        playerNameInputDialog.setContentText(CHOOSE_NAME_CONTENT);
        playerNameInputDialog.setGraphic(null);


        Scene scene = new Scene(pane, 500, 300);

        Button startButton = new Button(START);
        startButton.setOnAction(e -> {
            Player sp =new GraphicalSpectatorAdapter(true);
            RemotePlayerClient remoteClient = new RemotePlayerClient(sp, address.get(), port.get());
            mainWindow.hide();
            new Thread(remoteClient::run).start();
        });
        pane.getChildren().add(startButton);

        mainWindow.setScene(scene);
        mainWindow.show();

        mainWindow.setOnCloseRequest(windowsEvent -> System.exit(0));
    }
}
