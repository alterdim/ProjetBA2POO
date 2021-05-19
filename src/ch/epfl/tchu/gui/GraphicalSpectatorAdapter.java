package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.*;

import java.util.Map;

import static javafx.application.Platform.runLater;

/**
 * Créé le 19.05.2021 à 11:42
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public class GraphicalSpectatorAdapter implements Spectator{
    private GraphicalSpectator graphicalPlayer;


    public GraphicalSpectatorAdapter() {
    }

    @Override
    public void launchSpectator(Map<PlayerId, String> playerNames, boolean showHand) {
        //Choix arbitraire d'afficher le PLAYER_1 en premier
        runLater(() -> graphicalPlayer = new GraphicalSpectator(PlayerId.PLAYER_1, playerNames, showHand));
    }

    @Override
    public void receiveInfo(String info) {
        runLater(() -> graphicalPlayer.receiveInfo(info));
    }

    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {
        runLater(() -> graphicalPlayer.setState(newState, ownState));
    }
}
