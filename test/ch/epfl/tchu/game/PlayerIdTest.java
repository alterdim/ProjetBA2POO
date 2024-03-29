package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

import java.util.List;

import static ch.epfl.tchu.game.PlayerId.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Créé le 08.03.2021 à 12:52
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
class PlayerIdTest {
    //Given test
    @Test
    void playerIdAllIsDefinedCorrectly() {
        assertEquals(List.of(PlayerId.PLAYER_1, PlayerId.PLAYER_2), PlayerId.ALL);
    }

    @Test
    void playerIdNextWorks() {
        assertEquals(PlayerId.PLAYER_2, PlayerId.PLAYER_1.next());
        assertEquals(PlayerId.PLAYER_1, PlayerId.PLAYER_2.next());
    }

    //Personal test
    @Test
    void workOnALL(){
        assertEquals(List.of(PLAYER_1, PLAYER_2), ALL);
    }

    @Test
    void workOnCOUNT(){
        assertEquals(2, COUNT);
    }

    @Test
    void workOnNextPlayer1(){
        assertEquals(PLAYER_2,PLAYER_1.next());
    }

    @Test
    void workOnNextPlayer2(){
        assertEquals(PLAYER_1,PLAYER_2.next());
    }

}