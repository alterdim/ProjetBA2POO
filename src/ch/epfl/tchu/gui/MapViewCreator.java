package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.game.Route;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
        Pane canvas = new Pane();
        canvas.getStylesheets().add("map.css");
        canvas.getStylesheets().add("colors.css");
        ImageView background = new ImageView();
        canvas.getChildren().add(background);

        for (Route r : ChMap.routes()) {
            Group routeGroup = new Group();
            if (r.color() != null) {
                routeGroup.getStyleClass().addAll("route", r.level().toString(), r.color().toString());
            } else {
                routeGroup.getStyleClass().addAll("route", r.level().toString(), "NEUTRAL");
            }
            routeGroup.setId(r.id());

            for (int i = 0; i < r.length(); i++) {
                //Création des groupes
                Group caseGroup = new Group();
                caseGroup.setId(r.id() + "_" + (i + 1));
                Group wagonGroup = new Group();
                wagonGroup.getStyleClass().add("car");

                //Rectangle
                Rectangle trackRectangle = new Rectangle(36, 12);
                trackRectangle.getStyleClass().addAll("track", "filled");
                Rectangle carRectangle = new Rectangle(36, 12);
                carRectangle.getStyleClass().add("filled");

                // Cercles
                Circle circle1 = new Circle(12, 6, CAR_CIRCLE_RADIUS);
                Circle circle2 = new Circle(24, 6, CAR_CIRCLE_RADIUS);

                //On réunit et organise en groupes
                wagonGroup.getChildren().addAll(carRectangle, circle1, circle2);
                caseGroup.getChildren().addAll(trackRectangle, wagonGroup);
                routeGroup.getChildren().add(caseGroup);
            }

            gameState.routeOwner(r).addListener((observable, oldValue, newValue) -> {
                if (oldValue == null && newValue != null) {
                    routeGroup.getStyleClass().add(newValue.name());
                }
            });

            routeGroup.disableProperty().bind(claimRouteHandler.isNull().or(gameState.canClaimRoute(r).not()));
            routeGroup.setOnMouseClicked((event) -> {
                List<SortedBag<Card>> possibleClaimCards = gameState.possibleClaimCards(r);
                if (possibleClaimCards.size() == 1) {
                    claimRouteHandler.get().onClaimRoute(r, possibleClaimCards.get(0));
                } else {
                    handleSpecialCardCase(gameState, r, claimRouteHandler.get(), cardChooser);
                }
            });
            canvas.getChildren().addAll(routeGroup);
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
