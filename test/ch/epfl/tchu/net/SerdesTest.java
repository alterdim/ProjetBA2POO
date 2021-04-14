package ch.epfl.tchu.net;

import ch.epfl.tchu.game.*;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import static ch.epfl.tchu.game.Card.*;
import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Créé le 13.04.2021 à 11:32
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
class SerdesTest {
    @Test
    void IntegerSerdes() {
        assertEquals("2021", Serdes.INTEGER.serialize(2021));
        assertEquals(2021, Serdes.INTEGER.deserialize("2021"));
    }

    @Test
    void StringSerdes() {
        assertEquals("Q2hhcmxlcw==", Serdes.STRING.serialize("Charles"));
        assertEquals("Charles", Serdes.STRING.deserialize("Q2hhcmxlcw=="));
    }

    @Test
    void ListStringSerdes() {
        List<String> list = new ArrayList<>();
        list.add("Alice");
        list.add("Bob");
        list.add("Charles");

        StringBuilder string= new StringBuilder();
        for (String s : list) {
            if (string.length()!=0) string.append(",");
            string.append(Serdes.STRING.serialize(s));
        }

        assertEquals(string.toString(), Serdes.LIST_STRING.serialize(list));
        assertEquals(list, Serdes.LIST_STRING.deserialize(string.toString()));
    }

    @Test
    void PublicGameStateSerdes(){
        List<Card> fu = List.of(RED, WHITE, BLUE, BLACK, RED);
        PublicCardState cs = new PublicCardState(fu, 30, 31);
        List<Route> rs1 = ChMap.routes().subList(0, 2);
        Map<PlayerId, PublicPlayerState> ps = Map.of(
                PLAYER_1, new PublicPlayerState(10, 11, rs1),
                PLAYER_2, new PublicPlayerState(20, 21, List.of()));
        PublicGameState gs =
                new PublicGameState(40, cs, PLAYER_2, ps, null);

//        assertEquals("40:6,7,2,0,6;30;31:1:10;11;0,1:20;21;:", Serdes.PUBLIC_GAME_STATE.serialize(gs));
    }

}