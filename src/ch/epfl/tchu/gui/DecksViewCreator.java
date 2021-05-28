package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Constants;
import ch.epfl.tchu.game.Ticket;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
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
 * Construction un graphe de scène représentant des cartes
 * Créé le 30.04.2021 à 21:06
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
class DecksViewCreator {
    private DecksViewCreator(){}

    /**
     * Gère le visuel de la main du joueur
     *
     * @param observableGameState l 'état du jeu observable
     * @return HBox de la main
     */
    public static HBox createHandView(ObservableGameState observableGameState) {
        HBox canvas = new HBox();

        ListView<Ticket> ticketListView = new ListView<>();
        HBox handPaneHBox = new HBox();

        canvas.getStylesheets().add("decks.css");
        canvas.getStylesheets().add("colors.css");

        ticketListView.setId("tickets");
        handPaneHBox.setId("hand-pane");
        ticketListView.setItems(observableGameState.tickets());
        canvas.getChildren().add(ticketListView);

        for (Card card : Card.ALL) {
            StackPane stackPane = createCardStack(getColorString(card));

            ReadOnlyIntegerProperty count = observableGameState.cardsCountOf(card);
            stackPane.visibleProperty().bind(Bindings.greaterThan(count, 0));

            Text textCount = new Text();
            textCount.getStyleClass().add("count");
            textCount.textProperty().bind(Bindings.convert(count));
            textCount.visibleProperty().bind(Bindings.greaterThan(count, 1));

            stackPane.getChildren().add(textCount);
            handPaneHBox.getChildren().add(stackPane);
        }
        canvas.getChildren().add(handPaneHBox);
        return canvas;
    }

    /**
     * Gère le visuel de la pioche
     *
     * @param observableGameState l' état de jeu observable
     * @param ticketHandler       gestionnaire d' action des billets (tickets)
     * @param cardHandler         gestionnaire d' action des cartes
     * @return VBox de la pioche
     */
    public static VBox createCardsView(ObservableGameState observableGameState, ObjectProperty<ActionHandlers.DrawTicketsHandler> ticketHandler, ObjectProperty<ActionHandlers.DrawCardHandler> cardHandler) {
        VBox canvas = new VBox();

        canvas.setId("card-pane");
        canvas.getStylesheets().add("decks.css");
        canvas.getStylesheets().add("colors.css");

        Button ticketsButton = createButton(observableGameState.leftTicketsPercentage());
        ticketsButton.disableProperty().bind(ticketHandler.isNull());
        ticketsButton.setOnMouseClicked((event -> ticketHandler.get().onDrawTickets()));
        ticketsButton.setText(StringsFr.TICKETS);
        canvas.getChildren().add(ticketsButton);

        for (int index : Constants.FACE_UP_CARD_SLOTS) {
            StackPane stackPane = createCardStack(getColorString(observableGameState.faceUpCard(index).get()));

            //p = property, o = old, n= new
            observableGameState.faceUpCard(index).addListener((p, o, n) -> {
                //S'il y a un changement de carte -> changement de couleur
                if (!n.equals(o)) {
                    String oldColorName = getColorString(o);
                    String newColorName = getColorString(n);

                    //Contrôle si l'ancienne couleur fait partie de la liste
                    if (stackPane.getStyleClass().contains(oldColorName)) {
                        int pos = stackPane.getStyleClass().indexOf(oldColorName);
                        stackPane.getStyleClass().set(pos, newColorName);
                    } else {
                        stackPane.getStyleClass().add(newColorName);
                    }
                }
            });

            canvas.getChildren().add(stackPane);
            stackPane.disableProperty().bind(cardHandler.isNull());

            stackPane.setOnMouseClicked((event -> cardHandler.get().onDrawCard(index)));
        }

        Button cardsButton = createButton(observableGameState.leftCardsPercentage());

        cardsButton.disableProperty().bind(cardHandler.isNull());
        cardsButton.setOnMouseClicked((event -> cardHandler.get().onDrawCard(Constants.DECK_SLOT)));
        cardsButton.setText(StringsFr.CARDS);
        canvas.getChildren().add(cardsButton);
        return canvas;
    }

    private static StackPane createCardStack(String cardColorName) {
        StackPane stackPane = new StackPane();
        stackPane.getStyleClass().add("card");

        stackPane.getStyleClass().add(cardColorName);

        Rectangle rectangleOutside = new Rectangle(60, 90);
        rectangleOutside.getStyleClass().add("outside");

        Rectangle rectangleInside = new Rectangle(40, 70);
        rectangleInside.getStyleClass().addAll("inside", "filled");

        Rectangle rectangleImage = new Rectangle(40, 70);
        rectangleImage.getStyleClass().add("train-image");

        stackPane.getChildren().addAll(rectangleOutside, rectangleInside, rectangleImage);
        return stackPane;
    }

    private static Button createButton(ReadOnlyDoubleProperty percentage) {
        Button button = new Button();
        button.getStyleClass().add("gauged");
        Group group = new Group();

        Rectangle rectangleBackground = new Rectangle(50, 5);
        rectangleBackground.getStyleClass().add("background");
        Rectangle rectangleForeground = new Rectangle(50, 5);
        rectangleForeground.getStyleClass().add("foreground");

        rectangleForeground.widthProperty().bind(percentage.multiply(50).divide(100));
        group.getChildren().addAll(rectangleBackground, rectangleForeground);
        button.setGraphic(group);

        return button;
    }

    /**
     * Retourne le nom de la couleur de la carte, NEUTRAL si la carte est nulle
     * @param card Carte dont on veux obtenir le nom
     * @return String contenant le nom de la couleur associée à la carte
     */
    private static String getColorString(Card card) {
        String colorString;
        if (card != null && card.color() != null) {
            colorString = card.color().toString();

        } else {
            colorString = "NEUTRAL";
        }
        return colorString;
    }
}
