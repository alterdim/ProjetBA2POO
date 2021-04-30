package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Constants;
import ch.epfl.tchu.game.Route;
import com.sun.javafx.fxml.builder.JavaFXImageBuilder;
import com.sun.javafx.fxml.builder.JavaFXSceneBuilder;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

abstract class MapViewCreator {

    @FunctionalInterface
    interface CardChooser {
        void chooseCards(List<SortedBag<Card>> options,
                         ActionHandlers.ChooseCardsHandler handler);
    }

    public static Pane createMapView(ObservableGameState gameState, ObjectProperty<ActionHandlers.ClaimRouteHandler> claimRouteHandler, CardChooser cardChooser) {
        Pane canvas = new Pane();
        canvas.getStylesheets().add("map.css");
        ImageView background = new ImageView();
        canvas.getChildren().add(background);
        Group routeGroup = new Group();
        Group tempRouteGroup;
        Group tempCaseGroup;
        for (Route r : ChMap.routes()) {
            tempRouteGroup = new Group();
            tempRouteGroup.getStylesheets().addAll("ROUTE", r.level().toString(), r.color().toString());
            tempRouteGroup.setId(r.id());

            for (int i = 0; i< r.length(); i++) {
                tempCaseGroup = new Group();
                tempCaseGroup.setId(r.id()+"_"+(i+1));

            }
            routeGroup.getChildren().addAll(tempRouteGroup);
        }
        canvas.getChildren().add(routeGroup);
        routeGroup.getStyleClass().addAll("route", "UNDERGROUND", "NEUTRAL");
        routeGroup.getChildren().addAll();




        canvas.getChildren().addAll();
        return canvas;
    }
}
