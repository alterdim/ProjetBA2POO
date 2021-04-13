package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Color;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Créé le 13.04.2021 à 08:47
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
class SerdeTest {

    @Test
    void intOfSerde() {
        Serde<Integer> intSerde = Serde.of(i -> Integer.toString(i), Integer::parseInt);
        assertEquals("2021", intSerde.serialize(2021));
        assertEquals(2021, intSerde.deserialize("2021"));
    }

    @Test
    void  colorOneOfSerde() {
        Serde<Color> colorSerde = Serde.oneOf(Color.ALL);
        assertEquals("0", colorSerde.serialize(Color.BLACK));
        assertEquals(Color.BLACK, colorSerde.deserialize("0"));
    }

    @Test
    void intListOfSerde(){
        Serde<Integer> intSerde = Serde.of(i -> Integer.toString(i), Integer::parseInt);
        Serde<List<Integer>> listOfInt = Serde.listOf(intSerde, "/");

        assertEquals("1/2/3", listOfInt.serialize(List.of(1,2,3)));
        assertEquals(List.of(1,2,3), listOfInt.deserialize("1/2/3"));
    }

    @Test
    void colorListOfSerde(){
        Serde<Color> colorSerde = Serde.oneOf(Color.ALL);
        Serde<List<Color>> listOfColor = Serde.listOf(colorSerde, "+");

        assertEquals("0+2+7+0", listOfColor.serialize(List.of(Color.BLACK, Color.BLUE, Color.WHITE,Color.BLACK)));
        assertEquals(List.of(Color.BLACK, Color.BLUE, Color.WHITE), listOfColor.deserialize("0+2+7"));
    }

    @Test
    void colorBagOfSerde(){
        Serde<Color> colorSerde = Serde.oneOf(Color.ALL);
        Serde<SortedBag<Color>> bagOfColor = Serde.bagOf(colorSerde, "+");

        SortedBag.Builder bagBuilder = new SortedBag.Builder();
        bagBuilder.add(Color.BLACK);
        bagBuilder.add(Color.BLUE);
        bagBuilder.add(Color.WHITE);
        bagBuilder.add(Color.BLACK);
        assertEquals("0+0+2+7", bagOfColor.serialize(bagBuilder.build()));
        assertEquals(bagBuilder.build(), bagOfColor.deserialize("0+2+7+0"));
        assertEquals(bagBuilder.build(), bagOfColor.deserialize("0+0+2+7"));
    }


}