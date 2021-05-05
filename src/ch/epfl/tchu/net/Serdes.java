package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

/**
 *
 * Serdes utile au projet
 *
 * Créé le 13.04.2021 à 08:53
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public final class Serdes {

    /**
     * Serde utile pour les entiers
     */
    public static final Serde<Integer> INTEGER = Serde.of(
            i -> Integer.toString(i),
            Integer::parseInt);

    /**
     * Serde utile pour les chaines de caractères
     */
    public static final Serde<String> STRING = Serde.of(
            s -> Base64.getEncoder().encodeToString(s.getBytes(StandardCharsets.UTF_8)),
            s -> new String(Base64.getDecoder().decode(s), StandardCharsets.UTF_8));

    /**
     * Serde utile pour PlayerID
     */
    public static final Serde<PlayerId> PLAYER_ID = Serde.oneOf(PlayerId.ALL);
    /**
     * Serde utile pour TurnKind
     */
    public static final Serde<Player.TurnKind> TURN_KIND = Serde.oneOf(Player.TurnKind.ALL);
    /**
     * Serde utile pour Card
     */
    public static final Serde<Card> CARD = Serde.oneOf(Card.ALL);
    /**
     * Serde utile pour Route
     */
    public static final Serde<Route> ROUTE = Serde.oneOf(ChMap.routes());
    /**
     * Serde utile pour Ticket
     */
    public static final Serde<Ticket> TICKET = Serde.oneOf(ChMap.tickets());
    /**
     * Serde utile pour une listes de strings
     */
    public static final Serde<List<String>> LIST_STRING = Serde.listOf(STRING, ",");
    /**
     * Serde utile pour une listes de cartes
     */
    public static final Serde<List<Card>> LIST_CARD = Serde.listOf(CARD, ",");
    /**
     * Serde utile pour une listes de routes
     */
    public static final Serde<List<Route>> LIST_ROUTE = Serde.listOf(ROUTE, ",");
    /**
     * Serde utile pour un SortedBag de cartes
     */
    public static final Serde<SortedBag<Card>> SORTED_BAG_CARD = Serde.bagOf(CARD, ",");
    /**
     * Serde utile pour un SortedBag de tickets
     */
    public static final Serde<SortedBag<Ticket>> SORTED_BAG_TICKET = Serde.bagOf(TICKET, ",");
    /**
     * Serde utile pour une liste de SortedBag de cartes
     */
    public static final Serde<List<SortedBag<Card>>> LIST_SORTED_BAG_CARD = Serde.listOf(SORTED_BAG_CARD, ";");
    /**
     * Serde utile pour un PublicCardState
     */
    public static final Serde<PublicCardState> PUBLIC_CARD_STATE = Serde.of(Serdes::serializePCS, Serdes::deserializePCS);
    /**
     * Serde utile pour un PublicPlayerState
     */
    public static final Serde<PublicPlayerState> PUBLIC_PLAYER_STATE = Serde.of(Serdes::serializePPS, Serdes::deserializePPS);
    /**
     * Serde utile pour un PlayerState
     */
    public static final Serde<PlayerState> PLAYER_STATE = Serde.of(Serdes::serializePS, Serdes::deserializePS);
    /**
     * Serde utile pour un PublicGameState
     */
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
        infos.add(INTEGER.serialize(publicPlayerState.cardCount()));
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

        for (PlayerId playerId : PlayerId.values()) {
            infos.add(PUBLIC_PLAYER_STATE.serialize(publicGameState.playerState(playerId)));
        }

        if (publicGameState.lastPlayer() == null) infos.add("");
        else infos.add(PLAYER_ID.serialize(publicGameState.lastPlayer()));

        return String.join(":", infos);
    }

    private static PublicGameState deserializePGS(String string) {
        String[] infos = Pattern.quote(string).split(":", -1);
        Map<PlayerId, PublicPlayerState> playerMap = new EnumMap<>(PlayerId.class);
        for (int i = 0; i < PlayerId.COUNT; i++) {
            int index = i+3;
            playerMap.put(PlayerId.values()[i], PUBLIC_PLAYER_STATE.deserialize(infos[index]));
        }

        return new PublicGameState(INTEGER.deserialize(infos[0]),
                PUBLIC_CARD_STATE.deserialize(infos[1]),
                PLAYER_ID.deserialize(infos[2]),
                playerMap,
                PLAYER_ID.deserialize(infos[3+PlayerId.COUNT]));
    }
}
