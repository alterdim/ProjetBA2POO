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
        JavaFXSceneBuilder sceneBuilder = new JavaFXSceneBuilder();
        List<String> styleSheets = sceneBuilder.getStylesheets();
        ImageView background = new ImageView();
        background.setStyle("map:css");

        List<Node> routeNodes = new ArrayList<Node>();


        Group routeGroup = new Group();
        routeGroup.getStyleClass().addAll("route", "UNDERGROUND", "NEUTRAL");
        routeGroup.getChildren().addAll();

        Pane canvas = new Pane();
        canvas.getChildren().addAll();
        return canvas;
    }
}
