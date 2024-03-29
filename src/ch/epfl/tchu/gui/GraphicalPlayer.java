package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
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

import ch.epfl.tchu.gui.ActionHandlers.*;

import static ch.epfl.tchu.gui.StringsFr.*;

/**
 * Représente l' interface graphique d' un joueur.
 *
 * Fichier créé à 08:45 le 07/05/2021
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public class GraphicalPlayer {

    private final ObservableGameState gameState;
    private final ObservableList<Text> observableText;

    private final Stage mainWindow;

    private final ObjectProperty<DrawCardHandler> cardHandler;
    private final ObjectProperty<DrawTicketsHandler> ticketsHandler;
    private final ObjectProperty<ClaimRouteHandler> routeHandler;

    /**
     * Constructeur
     *
     * @param playerId    l'identité du joueur auquel l'instance correspond
     * @param playerNames table associative des noms des joueurs
     */
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

        Pane mapView = MapViewCreator.createMapView(gameState, routeHandler, this::chooseClaimCards);
        VBox infoView = InfoViewCreator.createInfoView(playerId, playerNames, gameState, observableText);
        VBox deckView = DecksViewCreator.createCardsView(gameState, ticketsHandler, cardHandler);
        HBox handView = DecksViewCreator.createHandView(gameState);
        borderPane.centerProperty().setValue(mapView);
        borderPane.bottomProperty().setValue(handView);
        borderPane.leftProperty().setValue(infoView);
        borderPane.rightProperty().setValue(deckView);

        mainWindow.show();
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

    /**
     * Permet d'effectuer une action
     *
     * @param newTicketHandler gestionnaire d'actions pour les tickets
     * @param newCardHandler   gestionnaire d'actions pour les cartes
     * @param newRouteHandler  gestionnaire d'actions pour les routes
     */
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

    /**
     * Permet au joueur de sélectionner une carte
     *
     * @param cardHandler un gestionnaire de tirage de carte
     */
    public void drawCard(ActionHandlers.DrawCardHandler cardHandler) {
        assert Platform.isFxApplicationThread();
        this.cardHandler.setValue((slot) -> {
            cardHandler.onDrawCard(slot);
            this.cardHandler.set(null);
            this.ticketsHandler.set(null);
            this.routeHandler.set(null);
        });
    }

    /**
     * ouvre une fenêtre permettant au joueur de faire son choix de tickets
     *
     * @param tickets un multi ensemble contenant cinq ou trois billets
     * @param handler gestionnaire de choix de billets
     */
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
        Text chooseTicketsText = new Text(String.format(StringsFr.CHOOSE_TICKETS, ticketCount, plural(ticketCount)));
        chooseTicketsTextFlow.getChildren().add(chooseTicketsText);

        chooseTicketsVBox.getChildren().add(chooseTicketsTextFlow);

        //ListView
        ObservableList<Ticket> observableTickets = FXCollections.observableArrayList(tickets.toList());
        ListView<Ticket> ticketList = new ListView<>(observableTickets);
        chooseTicketsVBox.getChildren().add(ticketList);

        ticketList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        //Bouton
        Button vboxButton = new Button();
        chooseTicketsVBox.getChildren().add(vboxButton);
        IntegerBinding ticketChoiceBinding = Bindings.size(ticketList.getSelectionModel().getSelectedItems());
        vboxButton.disableProperty().bind(ticketChoiceBinding.lessThan(ticketCount));
        vboxButton.setText(StringsFr.CHOOSE);
        vboxButton.setOnAction(event -> {
            SortedBag.Builder<Ticket> builder = new SortedBag.Builder<>();
            for (Ticket t : ticketList.getSelectionModel().getSelectedItems()) {
                builder.add(t);
            }
            handler.onChooseTickets(builder.build());
            chooseTicketsStage.hide();

        });

        chooseTicketsStage.show();
    }

    /**
     * ouvre une fenêtre permettant au joueur de faire son choix de cartes
     *
     * @param cardLists    une liste de multi ensembles de cartes
     * @param cardsHandler un gestionnaire de choix de cartes
     */
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
        ListView<SortedBag<Card>> listView = new ListView<>(FXCollections.observableList(cardLists));
        listView.setCellFactory(v -> new TextFieldListCell<>(new CardBagStringConverter()));
        vBox.getChildren().add(listView);

        //Bouton
        Button vboxButton = new Button();
        vBox.getChildren().add(vboxButton);
        IntegerBinding selectionModelObserver = Bindings.size(listView.getSelectionModel().getSelectedItems());
        vboxButton.disableProperty().bind(selectionModelObserver.isNotEqualTo(1));
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

    /**
     * ouvre une fenêtre permettant au joueur de faire son choix de cartes additionnelles
     *
     * @param cardLists    une liste de multi ensembles de cartes additionnelles possible pour s' emparer du tunnel
     * @param cardsHandler gestionnaire de choix de cartes
     */
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
        ListView<SortedBag<Card>> listView = new ListView<>(FXCollections.observableList(cardLists));
        listView.setCellFactory(v -> new TextFieldListCell<>(new CardBagStringConverter()));
        vBox.getChildren().add(listView);

        //Bouton
        Button vboxButton = new Button();
        vBox.getChildren().add(vboxButton);
        vboxButton.setText(StringsFr.CHOOSE);
        IntegerBinding selectionModelObserver = Bindings.size(listView.getSelectionModel().getSelectedItems());
        vboxButton.disableProperty().bind(selectionModelObserver.lessThan(1));
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
