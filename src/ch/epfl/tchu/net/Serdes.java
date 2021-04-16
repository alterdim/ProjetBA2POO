package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Créé le 13.04.2021 à 08:53
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public final class Serdes {

    public static final Serde<Integer> INTEGER = Serde.of(
            i -> Integer.toString(i),
            Integer::parseInt);

    public static final Serde<String> STRING = Serde.of(
            s -> Base64.getEncoder().encodeToString(s.getBytes(StandardCharsets.UTF_8)),
            s -> new String(Base64.getDecoder().decode(s), StandardCharsets.UTF_8));

    public static final Serde<PlayerId> PLAYER_ID = Serde.oneOf(PlayerId.ALL);
    public static final Serde<Player.TurnKind> TURN_KIND = Serde.oneOf(Player.TurnKind.ALL);
    public static final Serde<Card> CARD = Serde.oneOf(Card.ALL);
    public static final Serde<Route> ROUTE = Serde.oneOf(ChMap.routes());
    public static final Serde<Ticket> TICKET = Serde.oneOf(ChMap.tickets());

    public static final Serde<List<String>> LIST_STRING = Serde.listOf(STRING, ",");
    public static final Serde<List<Card>> LIST_CARD = Serde.listOf(CARD, ",");
    public static final Serde<List<Route>> LIST_ROUTE = Serde.listOf(ROUTE, ",");

    public static final Serde<SortedBag<Card>> SORTED_BAG_CARD = Serde.bagOf(CARD, ",");
    public static final Serde<SortedBag<Ticket>> SORTED_BAG_TICKET = Serde.bagOf(TICKET, ",");

    public static final Serde<List<SortedBag<Card>>> LIST_SORTED_BAG_CARD = Serde.listOf(SORTED_BAG_CARD, ";");

    public static final Serde<PublicCardState> PUBLIC_CARD_STATE = Serde.of(Serdes::serializePCS, Serdes::deserializePCS);
    public static final Serde<PublicPlayerState> PUBLIC_PLAYER_STATE = Serde.of(Serdes::serializePPS, Serdes::deserializePPS);
    public static final Serde<PlayerState> PLAYER_STATE = Serde.of(Serdes::serializePS, Serdes::deserializePS);
    public static final Serde<PublicGameState> PUBLIC_GAME_STATE = Serde.of(Serdes::serializePGS, Serdes::deserializePGS);

    private static String serializePCS(PublicCardState publicCardState) {
        List<String> infos = new ArrayList<>();
        infos.add(LIST_CARD.serialize(publicCardState.faceUpCards()));
        infos.add(INTEGER.serialize(publicCardState.deckSize()));
        infos.add(INTEGER.serialize(publicCardState.discardsSize()));
        return String.join(";", infos);
    }

    private static PublicCardState deserializePCS(String string) {
        String[] infos = Pattern.quote(string).split(";", -1);
        return new PublicCardState(LIST_CARD.deserialize(infos[0]),
                INTEGER.deserialize(infos[1]),
                INTEGER.deserialize(infos[2]));
    }


    private static String serializePPS(PublicPlayerState publicPlayerState) {
        List<String> infos = new ArrayList<>();
        infos.add(INTEGER.serialize(publicPlayerState.ticketCount()));
        infos.add(INTEGER.serialize(publicPlayerState.carCount()));
        infos.add(LIST_ROUTE.serialize(publicPlayerState.routes()));
        return String.join(";", infos);
    }

    private static PublicPlayerState deserializePPS(String string) {
        String[] infos = Pattern.quote(string).split(";", -1);
        return new PublicPlayerState(INTEGER.deserialize(infos[0]),
                INTEGER.deserialize(infos[1]),
                LIST_ROUTE.deserialize(infos[2]));
    }


    private static String serializePS(PlayerState playerState) {
        List<String> infos = new ArrayList<>();
        infos.add(SORTED_BAG_TICKET.serialize(playerState.tickets()));
        infos.add(SORTED_BAG_CARD.serialize(playerState.cards()));
        infos.add(LIST_ROUTE.serialize(playerState.routes()));
        return String.join(";", infos);
    }

    private static PlayerState deserializePS(String string) {
        String[] infos = Pattern.quote(string).split(";", -1);
        return new PlayerState(SORTED_BAG_TICKET.deserialize(infos[0]),
                SORTED_BAG_CARD.deserialize(infos[1]),
                LIST_ROUTE.deserialize(infos[2]));
    }


    private static String serializePGS(PublicGameState publicGameState) {
        List<String> infos = new ArrayList<>();
        infos.add(INTEGER.serialize(publicGameState.ticketsCount()));
        infos.add(PUBLIC_CARD_STATE.serialize(publicGameState.cardState()));
        infos.add(PLAYER_ID.serialize(publicGameState.currentPlayerId()));
        infos.add(PUBLIC_PLAYER_STATE.serialize(publicGameState.playerState(PlayerId.PLAYER_1)));
        infos.add(PUBLIC_PLAYER_STATE.serialize(publicGameState.playerState(PlayerId.PLAYER_2)));

        if (publicGameState.lastPlayer() == null) {
            infos.add("");
        }
        else {
            infos.add(PLAYER_ID.serialize(publicGameState.lastPlayer()));
        }

        return String.join(":", infos);
    }

    private static PublicGameState deserializePGS(String string) {
        String[] infos = Pattern.quote(string).split(":", -1);
        HashMap<PlayerId, PublicPlayerState> playerMap = new HashMap<>();
        playerMap.put(PlayerId.PLAYER_1, PUBLIC_PLAYER_STATE.deserialize(infos[3]));
        playerMap.put(PlayerId.PLAYER_2, PUBLIC_PLAYER_STATE.deserialize(infos[4]));
        return new PublicGameState(INTEGER.deserialize(infos[0]),
                PUBLIC_CARD_STATE.deserialize(infos[1]),
                PLAYER_ID.deserialize(infos[2]),
                playerMap,
                PLAYER_ID.deserialize(infos[5]));
    }

}
