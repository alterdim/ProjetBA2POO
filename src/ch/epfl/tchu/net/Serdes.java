package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

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

    //Types composites :

    //List<Card>
    //int
    //int
    public static final Serde<PublicCardState> PUBLIC_CARD_STATE = null;

    public static final Serde<PublicPlayerState> PUBLIC_PLAYER_STATE = null;
    public static final Serde<PlayerState> PLAYER_STATE = null;


    public static final Serde<PublicGameState> PUBLIC_GAME_STATE=null;


}
