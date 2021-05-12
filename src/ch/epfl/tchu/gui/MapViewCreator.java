package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Route;
import javafx.scene.shape.Rectangle;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.util.List;

import ch.epfl.tchu.gui.ActionHandlers.ClaimRouteHandler;

abstract class MapViewCreator {//TODO vérifier si bien abstarct

    public static final int CAR_CIRCLE_RADIUS = 3;

    public static Pane createMapView(ObservableGameState gameState, ObjectProperty<ActionHandlers.ClaimRouteHandler> claimRouteHandler, CardChooser cardChooser) {
        Group tempRouteGroup;
        Group tempCaseGroup;
        Group tempWagonGroup;
        Rectangle tempRect;
        Rectangle tempRect2;
        Circle tempCirc1;
        Circle tempCirc2;

        Pane canvas = new Pane();
        canvas.getStylesheets().add("map.css");
        canvas.getStylesheets().add("colors.css");
        ImageView background = new ImageView();
        canvas.getChildren().add(background);

        for (Route r : ChMap.routes()) {
            tempRouteGroup = new Group();
            if (r.color() != null) {
                tempRouteGroup.getStyleClass().addAll("route", r.level().toString(), r.color().toString());
            } else {
                tempRouteGroup.getStyleClass().addAll("route", r.level().toString(), "NEUTRAL");
            }
            tempRouteGroup.setId(r.id());

            for (int i = 0; i < r.length(); i++) {
                //Création des groupes
                tempCaseGroup = new Group();
                tempCaseGroup.setId(r.id() + "_" + (i + 1));
                tempWagonGroup = new Group();
                tempWagonGroup.getStyleClass().add("car");

                //Rectangle
                tempRect = new Rectangle(36, 12);
                tempRect.getStyleClass().addAll("track", "filled");
                tempRect2 = new Rectangle(36, 12);
                tempRect2.getStyleClass().add("filled");


                // Cercles
                tempCirc1 = new Circle(CAR_CIRCLE_RADIUS);
                tempCirc1.centerXProperty().setValue(12);
                tempCirc1.centerYProperty().setValue(6);
                tempCirc2 = new Circle(CAR_CIRCLE_RADIUS);
                tempCirc1.centerXProperty().setValue(24);
                tempCirc2.centerYProperty().setValue(6);

                //On réunit et organise en groupes
                tempWagonGroup.getChildren().addAll(tempRect2, tempCirc1, tempCirc2);
                tempCaseGroup.getChildren().addAll(tempRect, tempWagonGroup);
                tempRouteGroup.getChildren().add(tempCaseGroup);
            }
            tempRouteGroup.disableProperty().bind(claimRouteHandler.isNull().or(gameState.canClaimRoute(r).not()));
            tempRouteGroup.setOnMouseClicked((event) -> {
                List<SortedBag<Card>> possibleClaimCards = gameState.possibleClaimCards(r);
                if (possibleClaimCards.size() == 1) {
                    claimRouteHandler.get().onClaimRoute(r, possibleClaimCards.get(0));
                } else {
                    handleSpecialCardCase(gameState, r, claimRouteHandler.get(), cardChooser);
                }
            });
            canvas.getChildren().addAll(tempRouteGroup);
        }
        return canvas;
    }

    private static void handleSpecialCardCase(ObservableGameState gameState, Route route, ClaimRouteHandler claimRouteHandler, CardChooser cardChooser) {
        List<SortedBag<Card>> possibleClaimCards = gameState.possibleClaimCards(route);
        ActionHandlers.ChooseCardsHandler chooseCardsHandler = (chosenCards) -> claimRouteHandler.onClaimRoute(route, chosenCards);
        cardChooser.chooseCards(possibleClaimCards, chooseCardsHandler);
    }

    @FunctionalInterface
    interface CardChooser {
        void chooseCards(List<SortedBag<Card>> options,
                         ActionHandlers.ChooseCardsHandler handler);
    }

}
