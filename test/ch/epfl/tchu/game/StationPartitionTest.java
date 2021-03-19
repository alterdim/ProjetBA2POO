package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Créé le 08.03.2021 à 15:08
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public class StationPartitionTest {

    public final Station PAR = new Station(0, "Paris");
    public final Station LYO = new Station(1, "Lyon");
    public final Station BOR = new Station(2, "Bordeaux");
    public final Station MAR = new Station(3, "Marseille");
    public final Station TOU = new Station(4, "Toulouse");
    public final Station NIC = new Station(5, "Nice");
    public final Station BRE = new Station(6, "Brest");
    public final Station NAN = new Station(7, "Nantes");


    @Test
    void connect(){
        var builder = new StationPartition.Builder(8);
//        System.out.println(Arrays.toString(builder.flatPartition));
        builder.connect(PAR, NIC);
//        System.out.println(Arrays.toString(builder.flatPartition));
        builder.connect(NIC, BRE);
//        System.out.println(Arrays.toString(builder.flatPartition));

        builder.connect(NAN, BOR);
//        System.out.println(Arrays.toString(builder.flatPartition));
        builder.connect(BOR,BRE);
//        System.out.println(Arrays.toString(builder.flatPartition));

        builder.connect(MAR, LYO);
//        System.out.println(Arrays.toString(builder.flatPartition));


    }


    @Test
    void build(){
        var builder = new StationPartition.Builder(8);
        builder.connect(PAR, NIC);
        builder.connect(NIC, BRE);

        builder.connect(NAN, BOR);
        builder.connect(BOR,BRE);

        builder.connect(MAR, LYO);
//        System.out.println(Arrays.toString(builder.build().partition));
    }

    @Test
    void connected(){
        var builder = new StationPartition.Builder(8);
        builder.connect(PAR, NIC);
        builder.connect(NIC, BRE);

        builder.connect(NAN, BOR);
        builder.connect(BOR,BRE);

        builder.connect(MAR, LYO);

        assertTrue(builder.build().connected(PAR,NIC));
        assertTrue(builder.build().connected(PAR,BRE));
        assertTrue(builder.build().connected(NAN,PAR));
        assertTrue(builder.build().connected(LYO,MAR));

        assertFalse(builder.build().connected(TOU, PAR));
        assertFalse(builder.build().connected(LYO, TOU));
        assertFalse(builder.build().connected(LYO, PAR));
    }

}