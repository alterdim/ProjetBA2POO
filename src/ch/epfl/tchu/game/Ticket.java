package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import java.util.*;

/**
 *
 * Created at 14:02 on 22.02.2021
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public final class Ticket implements Comparable<Ticket> {
    private final List<Trip> trips;
    private final String text;

    public Ticket(List<Trip> trips) {
        //Check if not empty
        Preconditions.checkArgument(!trips.isEmpty());

        //Check if dublicates
        if (trips.size()>1){
        for (Trip trip : trips) {
                Preconditions.checkArgument(trip.from().name().equals(trips.get(0).from().name()));
            }
        }

        this.trips = trips;
        this.text = computeText();
    }

    public Ticket(Station from, Station to, int points) {
        this(Collections.singletonList(new Trip(from, to, points)));
//        this(List.of(new Trip(from, to, points)));
    }

    private String computeText() {
        String text = trips.get(0).from().name();
        text = String.format("%s - ", text);
        if (trips.size() > 1) {
            text += "{";
        }
        TreeSet<String> destinations = new TreeSet<>();
        for (Trip trip : trips) {
            destinations.add(trip.to().name() + " (" + trip.points() +")");
        }
        text = String.format("%s%s", text, String.join(", ", destinations));
        if (trips.size() > 1) {
            text += "}";
        }
        return text;
    }

    public String text() {
        return text;
    }

    public int points(StationConnectivity connectivity){
        List<Integer> points = new ArrayList<>();
        for (Trip trip : trips) {
            //ajoute les points du voyage (positif ou négatif)
            points.add(trip.points(connectivity));
        }
        //Récupère l'élément avec le plus de points dans la liste
        return Collections.max(points);
    }

    @Override
    public int compareTo(Ticket ticket) {
        return this.text().compareTo(ticket.text());
    }

    @Override
    public String toString() {
        return text();
    }
}
