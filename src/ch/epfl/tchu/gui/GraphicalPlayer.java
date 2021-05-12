package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.List;
import java.util.Map;

import static ch.epfl.tchu.gui.StringsFr.plural;
import ch.epfl.tchu.gui.ActionHandlers.*;

/**
 * Représente l'interface graphique d'un joueur.
 * <p>
 * Fichier créé à 08:45 le 07/05/2021
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */

public class GraphicalPlayer {

    private ObservableGameState gameState;
    private ObservableList<Text> observableText;

    private Stage mainWindow;

    private ObjectProperty<DrawCardHandler> cardHandler;
    private ObjectProperty<DrawTicketsHandler> ticketsHandler;
    private ObjectProperty<ClaimRouteHandler> routeHandler;

    public GraphicalPlayer(PlayerId playerId, Map<PlayerId, String> playerNames) {
        assert Platform.isFxApplicationThread();
        gameState = new ObservableGameState(playerId);

        mainWindow = new Stage();
        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane);
        mainWindow.setScene(scene);

        cardHandler = new SimpleObjectProperty<>();
        ticketsHandler = new SimpleObjectProperty<>();
        routeHandler = new SimpleObjectProperty<>();

        observableText = FXCollections.observableArrayList();

               /* FXCollections.observableArrayList(
                new Text("Première information.\n"),
                new Text("\nSeconde information.\n"));*/

        Pane mapView = MapViewCreator.createMapView(gameState, routeHandler, this::chooseClaimCards);
        VBox infoView = InfoViewCreator.createInfoView(playerId, playerNames, gameState, observableText);
        VBox deckView = DecksViewCreator.createCardsView(gameState, ticketsHandler,
                cardHandler);
        HBox handView = DecksViewCreator.createHandView(gameState);
        borderPane.centerProperty().setValue(mapView);
        borderPane.bottomProperty().setValue(handView);
        borderPane.leftProperty().setValue(infoView);
        borderPane.rightProperty().setValue(deckView);

        mainWindow.show();


    }

    public void setState(PublicGameState newGameState, PlayerState newPlayerState) {
        assert Platform.isFxApplicationThread();
        gameState.setState(newGameState, newPlayerState);
    }

    public void receiveInfo(String message) {
        assert Platform.isFxApplicationThread();
        if (observableText.size() >= 5) {
            observableText.remove(0);
        }
        observableText.add(new Text(message));
    }

    public void startTurn(ActionHandlers.DrawTicketsHandler newTicketHandler,
                          ActionHandlers.DrawCardHandler newCardHandler,
                          ActionHandlers.ClaimRouteHandler newRouteHandler) {
        assert Platform.isFxApplicationThread();
        if (gameState.canDrawTickets()) {
            this.ticketsHandler.setValue(() -> {
                newTicketHandler.onDrawTickets();
                this.cardHandler.set(null);
                this.ticketsHandler.set(null);
                this.routeHandler.set(null);
            });

        }
        if (gameState.canDrawCards()) {
            this.cardHandler.setValue((slot) -> {
                newCardHandler.onDrawCard(slot);
                this.cardHandler.set(null);
                this.ticketsHandler.set(null);
                this.routeHandler.set(null);
            });
        }
        this.routeHandler.set((route, cards) -> {
            newRouteHandler.onClaimRoute(route, cards);
            this.cardHandler.set(null);
            this.ticketsHandler.set(null);
            this.routeHandler.set(null);
        });
    }

    public void drawCard(ActionHandlers.DrawCardHandler cardHandler) {
        assert Platform.isFxApplicationThread();
        this.cardHandler.setValue((slot) -> {
            cardHandler.onDrawCard(slot);
            this.cardHandler.set(null);
            this.ticketsHandler.set(null);
            this.routeHandler.set(null);
        });
    }

    public void chooseTickets(SortedBag<Ticket> tickets, ActionHandlers.ChooseTicketsHandler handler) {
        assert Platform.isFxApplicationThread();

        int ticketCount = tickets.size() - Constants.DISCARDABLE_TICKETS_COUNT;

        //Initialisation
        Stage chooseTicketsStage = new Stage(StageStyle.UTILITY);
        chooseTicketsStage.initOwner(mainWindow);
        chooseTicketsStage.initModality(Modality.WINDOW_MODAL);
        chooseTicketsStage.setTitle(StringsFr.TICKETS_CHOICE);
        VBox chooseTicketsVBox = new VBox();
        Scene chooseTicketsScene = new Scene(chooseTicketsVBox);
        chooseTicketsScene.getStylesheets().add("chooser.css");
        chooseTicketsStage.setOnCloseRequest(Event::consume);

        chooseTicketsStage.setScene(chooseTicketsScene);

        //Texte
        TextFlow chooseTicketsTextFlow = new TextFlow();
        Text chooseTicketsText = new Text(String.format(StringsFr.CHOOSE_TICKETS, ticketCount, plural(ticketCount))); //TODO DEMANDER ASSITANT
        chooseTicketsTextFlow.getChildren().add(chooseTicketsText);

        chooseTicketsVBox.getChildren().add(chooseTicketsTextFlow);

        //ListView
        ObservableList<Ticket> observableTickets = FXCollections.observableArrayList(tickets.toList());
        ListView<Ticket> ticketList = new ListView(observableTickets);
        chooseTicketsVBox.getChildren().add(ticketList);
        if (ticketCount > 1) {
            ticketList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        }

        //Bouton
        Button vboxButton = new Button();
        chooseTicketsVBox.getChildren().add(vboxButton);
        vboxButton.disableProperty().bind(new SimpleBooleanProperty(ticketList.getSelectionModel().getSelectedItems().size() < ticketCount));
        vboxButton.setText(StringsFr.CHOOSE);
        vboxButton.setOnAction(event -> {
            chooseTicketsStage.hide();
            SortedBag.Builder<Ticket> builder = new SortedBag.Builder<>();
            for (Ticket t : ticketList.getSelectionModel().getSelectedItems()) {
                builder.add(t);
            }
            handler.onChooseTickets(builder.build());

        });

        chooseTicketsStage.show();
    }

    public void chooseClaimCards(List<SortedBag<Card>> cardLists, ActionHandlers.ChooseCardsHandler cardsHandler) {
        assert Platform.isFxApplicationThread();

        //Initialisation
        Stage stage = new Stage(StageStyle.UTILITY);
        stage.initOwner(mainWindow);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle(StringsFr.CARDS_CHOICE);
        VBox vBox = new VBox();
        Scene scene = new Scene(vBox);
        scene.getStylesheets().add("chooser.css");
        stage.setOnCloseRequest(Event::consume);
        stage.setScene(scene);

        //Texte
        TextFlow flow = new TextFlow();
        Text description = new Text(StringsFr.CHOOSE_CARDS);
        flow.getChildren().add(description);

        vBox.getChildren().add(flow);

        //ListView
        ListView<SortedBag<Card>> listView = new ListView(FXCollections.observableList(cardLists));
        listView.setCellFactory(v -> new TextFieldListCell<>(new CardBagStringConverter()));
        vBox.getChildren().add(listView);

        //Bouton
        Button vboxButton = new Button();
        vBox.getChildren().add(vboxButton);
        IntegerBinding ticketChoiceWatcher = Bindings.size(listView.getSelectionModel().getSelectedItems());
        vboxButton.disableProperty().bind(ticketChoiceWatcher.lessThan(3));
        vboxButton.setText(StringsFr.CHOOSE);
        vboxButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.hide();
                cardsHandler.onChooseCards(listView.getSelectionModel().getSelectedItem());
                cardHandler.set(null);
                ticketsHandler.set(null);
                routeHandler.set(null);
            }
        });
        stage.show();
    }

    public void chooseAdditionalCards(List<SortedBag<Card>> cardLists, ActionHandlers.ChooseCardsHandler cardsHandler) {
        assert Platform.isFxApplicationThread();

        //Initialisation
        Stage stage = new Stage(StageStyle.UTILITY);
        stage.initOwner(mainWindow);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle(StringsFr.CARDS_CHOICE);
        VBox vBox = new VBox();
        Scene scene = new Scene(vBox);
        scene.getStylesheets().add("chooser.css");
        stage.setOnCloseRequest(Event::consume);
        stage.setScene(scene);

        //Texte
        TextFlow flow = new TextFlow();
        Text description = new Text(StringsFr.CHOOSE_ADDITIONAL_CARDS);
        flow.getChildren().add(description);

        vBox.getChildren().add(flow);

        //ListView
        ListView<SortedBag<Card>> listView = new ListView(FXCollections.observableList(cardLists));
        listView.setCellFactory(v -> new TextFieldListCell<>(new CardBagStringConverter()));
        vBox.getChildren().add(listView);

        //Bouton
        Button vboxButton = new Button();
        vBox.getChildren().add(vboxButton);
        vboxButton.setText(StringsFr.CHOOSE);
        vboxButton.setOnAction(event -> {
            stage.hide();
            cardsHandler.onChooseCards(listView.getSelectionModel().getSelectedItem());
            cardHandler.set(null);
            ticketsHandler.set(null);
            routeHandler.set(null);
        });
        stage.show();
    }


}
