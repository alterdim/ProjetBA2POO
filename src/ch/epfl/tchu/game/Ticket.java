package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import java.util.*;

/**
 * Créé à 14:02 le 22.02.2021
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 *
 * Classe Ticket, caractérise les cartes Tickets qui permettent de gagner de points en reliant les trajets indiqués.
 */
public final class Ticket implements Comparable<Ticket> {
    private final List<Trip> trips;
    private final String text;

    /**
     * Constructeur de l'objet ticket
     * @param trips : La liste des trajets du ticket.
     * @throws IllegalArgumentException si dans la liste rentrée comme paramètre est vide ou s'il y a plusieurs gare de départ qui ne portent pas le même nom
     */
    public Ticket(List<Trip> trips) {
        //Check si les trips sont vides
        Preconditions.checkArgument(!trips.isEmpty());

        //Check si les trips sont en duplicates
        if (trips.size()>1){
        for (Trip trip : trips) {
                Preconditions.checkArgument(trip.from().name().equals(trips.get(0).from().name()));
            }
        }

        this.trips = trips;
        this.text = computeText();
    }

    /**
     * Constructeur d'un ticket avec un voyage unique.
     * @param from : point de départ
     * @param to : point d'arrivée
     * @param points : nombre de points du ticket
     */
    public Ticket(Station from, Station to, int points) {
        this(Collections.singletonList(new Trip(from, to, points)));
    }

    /**
     * @return Renvoie le texte descriptif du ticket de la forme Départ - Destination
     */
    private String computeText() {
        String text = String.format("%s - ", trips.get(0).from().name());

        if (trips.size()>1) text+="{";

        TreeSet<String> destinations = new TreeSet<>();
        for (Trip trip : trips) {
            destinations.add(String.format("%s (%s)", trip.to().name(), trip.points()));
        }

        text = String.format("%s%s", text, String.join(", ", destinations));

        if (trips.size()>1) text+="}";

        return text;
    }

    /**
     * @return Renvoie le texte descriptif du ticket de la forme Départ - Destination
     */
    public String text() {
        return text;
    }

    /**
     * @param connectivity La connectivité du joueur à calculer
     * @return Renvoie le nombre de points calculé du ticket en fonction de la connectivité du joueur
     */
    public int points(StationConnectivity connectivity){
        List<Integer> points = new ArrayList<>();
        for (Trip trip : trips) {
            //ajoute les points du voyage (positif ou négatif)
            points.add(trip.points(connectivity));
        }
        //Récupère l'élément avec le plus de points dans la list
        return Collections.max(points);
    }

    /** Compare deux tickets par ordre alphabétique
     * @param ticket Le ticket à comparer
     * @return Renvoie un integer négatif si le texte du ticket comparé se trouve plus loin dans l'alphabet,
     * un integer positif dans le cas contraire, et 0 si les deux textes sont identiques.
     */
    @Override
    public int compareTo(Ticket ticket) {
        return this.text().compareTo(ticket.text());
    }

    /**
     * @return Renvoie le texte descriptif du ticket. Préférer .text().
     */
    @Override
    public String toString() {
        return text();
    }

    public Station from(){
        return trips.get(0).from();
    }

    public Station to(){
        return trips.get(trips.size()-1).to();
    }

    public List<Station> stationsExtremity(){
        return List.of(from(), to());
    }
}
