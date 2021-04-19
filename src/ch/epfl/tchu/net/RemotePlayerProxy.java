package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * Créé le 19.04.2021 à 12:00
 *
 * Représente un mandataire (proxy) d'un joueur distant.
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public class RemotePlayerProxy implements Player {

    private Socket socket;

    public RemotePlayerProxy(Socket socket) {
        this.socket = socket;

    }

    private void sendThisDeafly(String sentString) {
        try (BufferedWriter writer =
                     new BufferedWriter(
                             new OutputStreamWriter(socket.getOutputStream(),
                                     US_ASCII))) {
            writer.write(sentString);
            writer.flush();

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

    }

    private String sendThisAndListen(String sentString) {
        try (BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(socket.getInputStream(),
                                     US_ASCII));
             BufferedWriter writer =
                     new BufferedWriter(
                             new OutputStreamWriter(socket.getOutputStream(),
                                     US_ASCII))) {
            writer.write(sentString);
            writer.flush();
            return reader.readLine();

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

    }

    private String shutUpAndListen() {
        try (BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(socket.getInputStream(),
                                     US_ASCII))) {
            return reader.readLine();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private String stringBaker(String... strings) {
        StringBuilder baker = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            baker.append(strings[i]);
            if (i != strings.length-1) {
                baker.append(" ");
            }
            else {
                baker.append("\n");
            }
        }
        return baker.toString();

    }

    /**
     * est appelée au début de la partie pour communiquer au joueur son nom et la liste de tous les joueurs (le siens inclus)
     *
     * @param ownId       sa propre identité
     * @param playerNames noms des différents joueurs, le sien inclus
     */
    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        String ownIdSerded = Serdes.PLAYER_ID.serialize(ownId);
        String playerNamesSerded = Serdes.LIST_STRING.serialize(new ArrayList<>(playerNames.values()));
        String readyString = stringBaker(MessageId.INIT_PLAYERS.name(), ownIdSerded, playerNamesSerded);
        sendThisDeafly(readyString);
    }

    /**
     * est appelée chaque fois qu'une information doit être communiquée au joueur au cours de la partie (sous forme de chaine de caractères)
     *
     * @param info information a communiquer
     */
    @Override
    public void receiveInfo(String info) {
        String infoSerded = Serdes.STRING.serialize(info);
        String readyString = stringBaker(MessageId.RECEIVE_INFO.name(), infoSerded);
        sendThisDeafly(readyString);
    }

    /**
     * Informe le joueur de la nouvelle composante publique lors d'un changement d'état
     *
     * @param newState nouvel état
     * @param ownState son propre état
     */
    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {
        String newStateSerded = Serdes.PUBLIC_GAME_STATE.serialize(newState);
        String ownStateSerded = Serdes.PLAYER_STATE.serialize(ownState);
        String readyString = stringBaker(MessageId.UPDATE_STATE.name(), newStateSerded, ownStateSerded);
        sendThisDeafly(readyString);
    }

    /**
     * communiquer au joueur les cinq billets qui lui ont été distribués au début de la partie
     *
     * @param tickets SortedBag de 5 tickets
     */
    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
        String ticketsSerded = Serdes.SORTED_BAG_TICKET.serialize(tickets);
        String readyString = stringBaker(ticketsSerded);
        sendThisDeafly(readyString);

    }

    /**
     * demande au joueur quels billets qu'on lui a distribué initialement(au début de la partie) il garde
     */
    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        String ticketsSerded = shutUpAndListen();
        return Serdes.SORTED_BAG_TICKET.deserialize(ticketsSerded);
    }

    /**
     * Quel action le joueur décide de faire durant son tour
     *
     * @return Type d'action effectuée durant le tour
     */
    @Override
    public TurnKind nextTurn() {
        String turnSerded = shutUpAndListen();
        return Serdes.TURN_KIND.deserialize(turnSerded);
    }

    /**
     * est appelée lorsque le joueur a décidé de tirer des billets supplémentaires en cours de partie, afin de lui communiquer les billets tirés et de savoir lesquels il garde
     *
     * @param options les billets tirés
     * @return les billets garder
     */
    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        String optionsSerded = Serdes.SORTED_BAG_TICKET.serialize(options);
        String readyString = stringBaker(optionsSerded);
        String serdedTickets = sendThisAndListen(readyString);
        return Serdes.SORTED_BAG_TICKET.deserialize(serdedTickets);
    }

    /**
     * Lorsque DRAW_CARDS est choisi, détermine depuis quel emplacement les cartes seront prises : cartes visible ou la pioche
     *
     * @return si carte visible entre 0 et 4 inclus, ou si pioche Constants.DECK_SLOT (c-à-d -1)
     */
    @Override
    public int drawSlot() {
        String receivedInt = shutUpAndListen();
        return Serdes.INTEGER.deserialize(receivedInt);
    }

    /**
     * est appelée lorsque le joueur a décidé de (tenter de) s'emparer d'une route, afin de savoir de quelle route il s'agit
     */
    @Override
    public Route claimedRoute() {
        String receivedRoute = shutUpAndListen();
        return Serdes.ROUTE.deserialize(receivedRoute);
    }

    /**
     * est appelée lorsque le joueur a décidé de (tenter de) s'emparer d'une route, afin de savoir quelle(s) carte(s) il désire initialement utiliser pour cela
     *
     * @return carte(s) que le joueur désir initialement utiliser
     */
    @Override
    public SortedBag<Card> initialClaimCards() {
        String receivedCards = shutUpAndListen();
        return Serdes.SORTED_BAG_CARD.deserialize(receivedCards);
    }

    /**
     * est appelée lorsque le joueur a décidé de tenter de s'emparer d'un tunnel et que des cartes additionnelles sont nécessaires, afin de savoir quelle(s) carte(s) il désire utiliser pour cela
     *
     * @param options carte(s) additionnelles disponibles
     * @return carte(s) additionnelles choisies par le joueur si non vide, sinon (multiensemble vide) cela signifie que le joueur ne désire pas (ou ne peut pas) choisir l'une de ces possibilités
     */
    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        String optionsSerded = Serdes.LIST_SORTED_BAG_CARD.serialize(options);
        String readyString = stringBaker(optionsSerded);
        String cardsSerded = sendThisAndListen(readyString);
        return Serdes.SORTED_BAG_CARD.deserialize(cardsSerded);
    }
}
