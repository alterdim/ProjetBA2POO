package ch.epfl.tchu.game;

import java.util.List;

/**
 * Created at 13:36 on 22.02.2021
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public enum Card {
    BLACK,
    VIOLET,
    BLUE,
    GREEN,
    YELLOW,
    ORANGE,
    RED,
    WHITE,
    LOCOMOTIVE;

    public static final List<Card> ALL = List.of(Card.values());
    public static final int COUNT = ALL.size();
    public static final List<Card> CARS = ALL.subList(0, COUNT-2);

    public static Card of(Color color){
        return Card.valueOf(color.toString());
    }

    public Color color(){
        if (this.equals(LOCOMOTIVE)){
            return null;
        }
        else {
            return Color.valueOf(this.toString());
        }
    }
}
