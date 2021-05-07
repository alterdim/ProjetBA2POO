package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.zip.CheckedOutputStream;

import static ch.epfl.tchu.gui.StringsFr.KEPT_N_TICKETS;
import static ch.epfl.tchu.gui.StringsFr.plural;

/**
 * Représente l'interface graphique d'un joueur.
 *
 * Fichier créé à 08:45 le 07/05/2021
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */

public class GraphicalPlayer {

    private ObservableGameState gameState;
    private ObservableList<Text> observableText;

    private Stage mainWindow;

    private ActionHandlers.DrawCardHandler cardHandler;
    private ActionHandlers.DrawTicketsHandler ticketsHandler;
    private ActionHandlers.ClaimRouteHandler routeHandler;

    public GraphicalPlayer(PlayerId playerId, Map<PlayerId, String> playerNames) {
        assert Platform.isFxApplicationThread();
        gameState = new ObservableGameState(playerId);

        mainWindow = new Stage();
        BorderPane borderPane = new BorderPane();

        ObservableList<Text> infos = FXCollections.observableArrayList(
                new Text("Première information.\n"),
                new Text("\nSeconde information.\n"));

        //Pane mapView = MapViewCreator.createMapView(gameState, )
        VBox infoView = InfoViewCreator.createInfoView(playerId, playerNames, gameState, observableText);

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

    public void startTurn(ActionHandlers.DrawCardHandler cardHandler,
                          ActionHandlers.DrawTicketsHandler ticketsHandler,
                          ActionHandlers.ClaimRouteHandler routeHandler) {
        assert Platform.isFxApplicationThread();
        this.routeHandler = routeHandler;
        this.cardHandler = cardHandler;
        this.ticketsHandler = ticketsHandler;
    }

    public void drawCard(ActionHandlers.DrawCardHandler cardHandler) {
        assert Platform.isFxApplicationThread();
        this.cardHandler = cardHandler;
    }

    public void chooseTickets(SortedBag<Ticket> tickets, ActionHandlers.ChooseTicketsHandler ticketsHandler) {
        assert Platform.isFxApplicationThread();

        int ticketCount = tickets.size() - Constants.DISCARDABLE_TICKETS_COUNT;

        //Initialisation
        Stage chooseTicketsStage = new Stage(StageStyle.UTILITY);
        chooseTicketsStage.initOwner(mainWindow);
        chooseTicketsStage.initModality(Modality.WINDOW_MODAL);
        VBox chooseTicketsVBox = new VBox();
        Scene chooseTicketsScene = new Scene(chooseTicketsVBox);
        chooseTicketsScene.getStylesheets().add("chooser.css");
        chooseTicketsStage.setOnCloseRequest(Event::consume);

        //Texte
        TextFlow chooseTicketsTextFlow = new TextFlow();
        Text chooseTicketsText = new Text(String.format(StringsFr.CHOOSE_TICKETS, ticketCount, plural(ticketCount))); //TODO DEMANDER ASSITANT
        chooseTicketsTextFlow.getChildren().add(chooseTicketsText);

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
        vboxButton.disableProperty().bind(new SimpleBooleanProperty(ticketList.getSelectionModel().getSelectedItems().size()== ticketCount) ) ;
        vboxButton.setOnAction((button) -> ticketsHandler.onChooseTickets(tickets));
    }

    public void chooseClaimCards(List<SortedBag<Card>> cardLists, ActionHandlers.ChooseCardsHandler cardsHandler) {

    }

    public void chooseAdditionalCards (List<SortedBag<Card>> cardLists, ActionHandlers.ChooseCardsHandler cardsHandler) {
        assert Platform.isFxApplicationThread();

        //Initialisation
        Stage chooseCardsStage = new Stage(StageStyle.UTILITY);
        chooseCardsStage.initOwner(mainWindow);
        chooseCardsStage.initModality(Modality.WINDOW_MODAL);
        VBox chooseCardsVBox = new VBox();
        Scene chooseCardsScene = new Scene(chooseCardsVBox);
        chooseCardsScene.getStylesheets().add("chooser.css");
        chooseCardsScene.chooseCardsVBox(Event::consume);

        //Texte
        TextFlow chooseCardsFlow = new TextFlow();
        Text chooseCardsText = new Text(StringsFr.CHOOSE_ADDITIONAL_CARDS);
        chooseCardsFlow.getChildren().add(chooseCardsText);

        //ListView
        ObservableList<Ticket> observableTickets = FXCollections.observableArrayList(cardLists);
        ListView<Ticket> ticketList = new ListView(observableTickets);
        chooseTicketsVBox.getChildren().add(ticketList);
        if (ticketCount > 1) {
            ticketList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        }

        //Bouton
        Button vboxButton = new Button();
        chooseTicketsVBox.getChildren().add(vboxButton);
        vboxButton.disableProperty().bind(new SimpleBooleanProperty(ticketList.getSelectionModel().getSelectedItems().size()== ticketCount) ) ;
        vboxButton.setOnAction((button) -> ticketsHandler.onChooseTickets(tickets));
    }



}
