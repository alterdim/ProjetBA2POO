package ch.epfl.tchu.game;

import java.util.Arrays;
import java.util.List;

/**
 * Created at 13:36 on 22.02.2021
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public enum Card {
    BLACK(Color.BLACK),
    VIOLET(Color.VIOLET),
    BLUE(Color.BLUE),
    GREEN(Color.GREEN),
    YELLOW(Color.YELLOW),
    ORANGE(Color.YELLOW),
    RED(Color.RED),
    WHITE(Color.WHITE),
    LOCOMOTIVE(null);

    private final Color color;

    Card(Color color){
        this.color=color;
    }

    public static final List<Card> ALL = List.of(Card.values());
    public static final int COUNT = ALL.size();
    public static final List<Card> CARS = Arrays.asList(BLACK,VIOLET,BLUE,GREEN,YELLOW,ORANGE,RED,WHITE);

    public static Card of(Color color){
        return Card.valueOf(color.toString());
    }

    public Color color(){
        return CARS.contains(this) ? this.color : null;
    }
}
