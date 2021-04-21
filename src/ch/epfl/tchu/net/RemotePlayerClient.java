package ch.epfl.tchu.net;

import ch.epfl.tchu.game.Player;
import ch.epfl.tchu.game.PlayerId;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 *
 * Client de joueur distant
 *
 * Créé le 19.04.2021 à 12:48
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public class RemotePlayerClient {
    private Player player;
    private String address;
    private int port;

    /**
     *Initialise le RemotePlayerClient
     *
     * @param player  le joueur auquel elle doit fournir un accès distant
     * @param address nom à utiliser pour se connecter au mandataire
     * @param port port à utiliser pour se connecter au mandataire
     */
    public RemotePlayerClient(Player player, String address, int port) {
        this.player = player;
        this.address = address;
        this.port = port;
    }

    /**
     * Gère le player en fonction des messages reçu par RemotePlayerProxy
     */
    public void run() {
        try (Socket s = new Socket(address, port);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(s.getInputStream(),
                             StandardCharsets.US_ASCII)
             );
             BufferedWriter writer = new BufferedWriter(
                     new OutputStreamWriter(s.getOutputStream(),
                             StandardCharsets.US_ASCII)
             )
        ){
            String message;
            while ((message = reader.readLine()) != null) {
                String[] data = message.split(Pattern.quote(" "));

                switch (MessageId.valueOf(data[0])) {
                    case INIT_PLAYERS:
                        player.initPlayers(Serdes.PLAYER_ID.deserialize(data[1]), getPlayerNames(Serdes.LIST_STRING.deserialize(data[2])));
                        break;
                    case RECEIVE_INFO:
                        player.receiveInfo(Serdes.STRING.deserialize(data[1]));
                        break;
                    case UPDATE_STATE:
                        player.updateState(Serdes.PUBLIC_GAME_STATE.deserialize(data[1]), Serdes.PLAYER_STATE.deserialize(data[2]));
                        break;
                    case SET_INITIAL_TICKETS:
                        player.setInitialTicketChoice(Serdes.SORTED_BAG_TICKET.deserialize(data[1]));
                        break;
                    case CHOOSE_INITIAL_TICKETS:

                        writer.write(Serdes.SORTED_BAG_TICKET.serialize(player.chooseInitialTickets()) + "\n");
                        writer.flush();
                        break;
                    case NEXT_TURN:
                        writer.write(Serdes.TURN_KIND.serialize(player.nextTurn()) + "\n");
                        writer.flush();
                        break;
                    case CHOOSE_TICKETS:
                        writer.write(Serdes.SORTED_BAG_TICKET.serialize(player.chooseTickets(Serdes.SORTED_BAG_TICKET.deserialize(data[1]))) + "\n");
                        writer.flush();
                        break;
                    case DRAW_SLOT:
                        writer.write(Serdes.INTEGER.serialize(player.drawSlot()) + "\n");
                        writer.flush();
                        break;
                    case ROUTE:
                        writer.write(Serdes.ROUTE.serialize(player.claimedRoute()) + "\n");
                        writer.flush();
                        break;
                    case CARDS:
                        writer.write(Serdes.SORTED_BAG_CARD.serialize(player.initialClaimCards()) + "\n");
                        writer.flush();
                        break;
                    case CHOOSE_ADDITIONAL_CARDS:
                        writer.write(Serdes.SORTED_BAG_CARD.serialize(player.chooseAdditionalCards(Serdes.LIST_SORTED_BAG_CARD.deserialize(data[1])))+ "\n");
                        writer.flush();
                        break;
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Convertit une liste en map de noms de joueurs
     * @param names Liste de String avec les noms de joueurs
     * @return Map des noms de joueurs
     */
    private Map<PlayerId, String> getPlayerNames(List<String> names) {
        return Map.of(PlayerId.PLAYER_1, names.get(PlayerId.PLAYER_1.ordinal()), PlayerId.PLAYER_2, names.get(PlayerId.PLAYER_2.ordinal()));
    }
}
