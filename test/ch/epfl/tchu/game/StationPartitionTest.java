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
    public final Station LYO = new Station(1, "Berne");
    public final Station BOR = new Station(2, "Berne");
    public final Station MAR = new Station(3, "Berne");
    public final Station TOU = new Station(4, "Berne");
    public final Station NIC = new Station(5, "Berne");
    public final Station BRE = new Station(6, "Berne");
    public final Station NAN = new Station(7, "Berne");


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


    }


    @Test
    void build(){
        var builder = new StationPartition.Builder(8);
        builder.connect(PAR, NIC);
        builder.connect(NIC, BRE);

        builder.connect(NAN, BOR);
        builder.connect(BOR,BRE);

        builder.connect(MAR, LYO);
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
    }

}