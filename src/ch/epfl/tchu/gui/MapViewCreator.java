package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Constants;
import ch.epfl.tchu.game.Route;
import com.sun.javafx.fxml.builder.JavaFXImageBuilder;
import com.sun.javafx.fxml.builder.JavaFXSceneBuilder;
import javafx.scene.shape.Rectangle;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
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
        Group tempRouteGroup;
        Group tempCaseGroup;
        Group tempVoieGroup;
        Group tempWagonGroup;
        Rectangle tempRect;
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
            }
            else {
                tempRouteGroup.getStyleClass().addAll("route", r.level().toString(), "NEUTRAL");
            }
            tempRouteGroup.setId(r.id());

            for (int i = 0; i< r.length(); i++) {
                //Création des groupes
                tempCaseGroup = new Group();
                tempCaseGroup.setId(r.id()+"_"+(i+1));
                tempVoieGroup = new Group();
                tempWagonGroup = new Group();
                tempWagonGroup.getStyleClass().add("car");

                //Rectangle
                tempRect = new Rectangle(36, 12);
                tempRect.getStyleClass().addAll("track", "filled");

                // Cercles
                tempCirc1 = new Circle(3);
                tempCirc1.centerXProperty().setValue(12);
                tempCirc1.centerYProperty().setValue(6);
                tempCirc2 = new Circle(3);
                tempCirc1.centerXProperty().setValue(24);
                tempCirc2.centerYProperty().setValue(6);

                //On réunit et organise en groupes
                tempWagonGroup.getChildren().addAll(tempRect, tempCirc1, tempCirc2);
                tempCaseGroup.getChildren().addAll(tempVoieGroup, tempWagonGroup);
                tempRouteGroup.getChildren().add(tempCaseGroup);
            }

            canvas.getChildren().addAll(tempRouteGroup);
        }
        return canvas;
    }
}
