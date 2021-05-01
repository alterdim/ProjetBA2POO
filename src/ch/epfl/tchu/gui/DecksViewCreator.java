package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Constants;
import ch.epfl.tchu.game.Ticket;
import ch.epfl.tchu.gui.ActionHandlers.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;


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

        ticketListView.setId("tickets");//TODO id correct?
        handPaneHBox.setId("hand-pane");//TODO id correct?
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
            tempStackPane.visibleProperty().bind(Bindings.greaterThan(count, 1));


            tempStackPane.getChildren().addAll(tempRectangleOutside, tempRectangleInside, tempRectangleImage, tempTextCount);
            canvas.getChildren().add(tempStackPane);
        }


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

        canvas.setId("card-pane");//TODO id correct?
        canvas.getStylesheets().add("decks.css");
        canvas.getStylesheets().add("colors.css");


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
        Group tempGroup = new Group();
        Rectangle tempRectangleBackground = new Rectangle(50, 5);
        Rectangle tempRectangleForeground = new Rectangle(50, 5);

        tempRectangleForeground.widthProperty().bind(observableGameState.leftCardsPercentage().multiply(50).divide(100));
        tempGroup.getChildren().addAll(tempRectangleBackground, tempRectangleForeground);
        cardsButton.setGraphic(tempGroup);
        cardsButton.disableProperty().bind(cardHandler.isNull());
        cardsButton.setOnMouseClicked((event -> cardHandler.get().onDrawCard(Constants.DECK_SLOT)));


        ticketsButton.getStyleClass().add("gauged");
        tempGroup = new Group();
        tempRectangleBackground = new Rectangle(50, 5);
        tempRectangleForeground = new Rectangle(50, 5);
        tempRectangleForeground.widthProperty().bind(observableGameState.leftTicketsPercentage().multiply(50).divide(100));

        tempGroup.getChildren().addAll(tempRectangleBackground, tempRectangleForeground);
        ticketsButton.setGraphic(tempGroup);
        ticketsButton.disableProperty().bind(ticketHandler.isNull());
        ticketsButton.setOnMouseClicked((event -> ticketHandler.get().onDrawTickets()));
        ticketsButton.setText("Billets");//TODO faut-il ajouter le texte comme ca?

        canvas.getChildren().addAll(cardsButton, ticketsButton);
        return canvas;
    }
}
