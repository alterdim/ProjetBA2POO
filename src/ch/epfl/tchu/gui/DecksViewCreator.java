package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Constants;
import ch.epfl.tchu.game.Ticket;
import ch.epfl.tchu.gui.ActionHandlers.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.awt.*;


/**
 * Créé le 30.04.2021 à 21:06
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
abstract class DecksViewCreator {

    public static HBox createHandView(ObservableGameState observableGameState) {
        HBox canvas = new HBox();

        ListView<Ticket> ticketListView = new ListView<>();
        HBox handPaneHBox = new HBox();

        StackPane tempStackPane;

        Text tempTextCount;
        Rectangle tempRectangleOutside;
        Rectangle tempRectangleInside;
        Rectangle tempRectangleImage;

        canvas.getStylesheets().add("decks.css");
        canvas.getStylesheets().add("colors.css");

        ticketListView.setId("tickets");
        handPaneHBox.setId("hand-pane");
        canvas.getChildren().add(ticketListView);
        canvas.getChildren().add(handPaneHBox);

        for (Card card : Card.ALL) {
            tempStackPane = new StackPane();
            ReadOnlyIntegerProperty count = observableGameState.cardsCountOf(card);
            tempStackPane.visibleProperty().bind(Bindings.greaterThan(count, 0));

            if (card.color() != null) {
                tempStackPane.getStyleClass().add(card.color().toString());
            } else {
                tempStackPane.getStyleClass().add("NEUTRAL");
            }
            tempStackPane.getStyleClass().add("card");

            tempRectangleOutside = new Rectangle(60, 90);
            tempRectangleOutside.getStyleClass().add("outside");

            tempRectangleInside = new Rectangle(40, 70);
            tempRectangleInside.getStyleClass().addAll("inside", "filled");

            tempRectangleImage = new Rectangle(40, 70);
            tempRectangleImage.getStyleClass().add("train-image");

            tempTextCount = new Text();
            tempTextCount.getStyleClass().add("count");
            tempTextCount.textProperty().bind(Bindings.convert(count));
            tempTextCount.visibleProperty().bind(Bindings.greaterThan(count, 1));


            tempStackPane.getChildren().addAll(tempRectangleOutside, tempRectangleInside, tempRectangleImage, tempTextCount);
            canvas.getChildren().add(tempStackPane);
        }

        ticketListView.setItems(observableGameState.tickets());



        return canvas;
    }

    public static VBox createCardsView(ObservableGameState observableGameState, ObjectProperty<ActionHandlers.DrawTicketsHandler> ticketHandler, ObjectProperty<ActionHandlers.DrawCardHandler> cardHandler) {
        VBox canvas = new VBox();

        Button ticketsButton = new Button();
        Button cardsButton = new Button();

        StackPane tempStackPane;

        Rectangle tempRectangleOutside;
        Rectangle tempRectangleInside;
        Rectangle tempRectangleImage;

        canvas.setId("card-pane");
        canvas.getStylesheets().add("decks.css");
        canvas.getStylesheets().add("colors.css");


        ticketsButton.getStyleClass().add("gauged");
        Group tempGroupTicket = new Group();
        Rectangle tempRectangleBackgroundTicket = new Rectangle(50, 5);
        Rectangle tempRectangleForegroundTicket = new Rectangle(50, 5);

        tempRectangleBackgroundTicket.getStyleClass().add("background");
        tempRectangleForegroundTicket.getStyleClass().add("foreground");

        tempRectangleForegroundTicket.widthProperty().bind(observableGameState.leftTicketsPercentage().multiply(50).divide(100));
        tempGroupTicket.getChildren().addAll(tempRectangleBackgroundTicket, tempRectangleForegroundTicket);
        ticketsButton.setGraphic(tempGroupTicket);
        ticketsButton.disableProperty().bind(ticketHandler.isNull());
        ticketsButton.setOnMouseClicked((event -> ticketHandler.get().onDrawTickets()));
        ticketsButton.setText(StringsFr.TICKETS);

        canvas.getChildren().add(ticketsButton);


        for (int index : Constants.FACE_UP_CARD_SLOTS) {
            tempStackPane = new StackPane();
            StackPane finalTempStackPane = tempStackPane;
            observableGameState.faceUpCard(index).addListener((p, o , n)-> {
                //TODO vérifier
                if (n.color() != null) {
                    finalTempStackPane.getStyleClass().add(n.color().toString());
                } else {
                    finalTempStackPane.getStyleClass().add("NEUTRAL");
                }
            });


//            Card card = observableGameState.faceUpCard(index).get();
            /*if (card != null && card.color() != null) {
                tempStackPane.getStyleClass().add(card.color().toString());
            } else {
                tempStackPane.getStyleClass().add("NEUTRAL");
            }*/
            tempStackPane.getStyleClass().add("card");

            tempRectangleOutside = new Rectangle(60, 90);
            tempRectangleOutside.getStyleClass().add("outside");

            tempRectangleInside = new Rectangle(40, 70);
            tempRectangleInside.getStyleClass().addAll("inside", "filled");

            tempRectangleImage = new Rectangle(40, 70);
            tempRectangleImage.getStyleClass().add("train-image");




            tempStackPane.getChildren().addAll(tempRectangleOutside, tempRectangleInside, tempRectangleImage);
            canvas.getChildren().add(tempStackPane);
            tempStackPane.disableProperty().bind(cardHandler.isNull());

            tempStackPane.setOnMouseClicked((event -> cardHandler.get().onDrawCard(index)));

        }

        cardsButton.getStyleClass().add("gauged");
        Group tempGroupCard = new Group();
        Rectangle tempRectangleBackgroundCard = new Rectangle(50, 5);
        Rectangle tempRectangleForegroundCard = new Rectangle(50, 5);
        tempRectangleBackgroundCard.getStyleClass().add("background");
        tempRectangleForegroundCard.getStyleClass().add("foreground");

        tempRectangleForegroundCard.widthProperty().bind(observableGameState.leftCardsPercentage().multiply(50).divide(100));
        tempGroupCard.getChildren().addAll(tempRectangleBackgroundCard, tempRectangleForegroundCard);
        cardsButton.setGraphic(tempGroupCard);
        cardsButton.disableProperty().bind(cardHandler.isNull());
        cardsButton.setOnMouseClicked((event -> cardHandler.get().onDrawCard(Constants.DECK_SLOT)));
        cardsButton.setText(StringsFr.CARDS);
        canvas.getChildren().add(cardsButton);
        return canvas;
    }
}
