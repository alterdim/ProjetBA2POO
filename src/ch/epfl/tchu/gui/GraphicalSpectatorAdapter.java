package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.util.List;
import java.util.Map;

import static javafx.application.Platform.runLater;

/**
 * Créé le 19.05.2021 à 11:42
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public class GraphicalSpectatorAdapter implements Player{
    private GraphicalSpectator graphicalPlayer;
private boolean showHand;

    public GraphicalSpectatorAdapter(/*Map<PlayerId, String> playerNames,*/ boolean showHand) {
        this.showHand=showHand;
//        runLater(() -> graphicalPlayer = new GraphicalSpectator(PlayerId.PLAYER_1, playerNames, showHand));
    }


    /*@Override
    public void launchSpectator(Map<PlayerId, String> playerNames, boolean showHand) {
        //Choix arbitraire d'afficher le PLAYER_1 en premier
        runLater(() -> graphicalPlayer = new GraphicalSpectator(PlayerId.PLAYER_1, playerNames, showHand));
    }*/

    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
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

    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {

    }

    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        return null;
    }

    @Override
    public TurnKind nextTurn() {
        return null;
    }

    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        return null;
    }

    @Override
    public int drawSlot() {
        return 0;
    }

    @Override
    public Route claimedRoute() {
        return null;
    }

    @Override
    public SortedBag<Card> initialClaimCards() {
        return null;
    }

    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        return null;
    }
}
