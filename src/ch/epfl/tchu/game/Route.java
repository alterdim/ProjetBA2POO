package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Une route relie deux stations et possède plusieurs caractéristiques.
 * Fichier créé à 14:28 le 23.02.2021
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */

public final class Route {
    private final String id;
    private final Station station1;
    private final Station station2;
    private final int length;
    private final Level level;
    private final Color color;

    /**
     * @param id       L'identification unique de la route. Chaine de caractères, pas un int comme les stations !
     * @param station1 Station de départ
     * @param station2 Station d'arrivée
     * @param length   Longueur de la route (affecte le nombre de cartes à utiliser pour la capturer)
     * @param level    Level.UNDERGROUND ou Level.OVERGROUND, affecte la règle de capture
     * @param color    Couleur de la route, null si elle est neutre
     * @throws IllegalArgumentException si les deux gares sont égal (au sens de equals)
     * @throws IllegalArgumentException si la longueur n'est pas compris dans les bornes défini dans Constant
     * @throws NullPointerException     si une des gare ou le niveau est null
     */
    public Route(String id, Station station1, Station station2, int length, Level level, Color color) {
        Preconditions.checkArgument(!station1.equals(station2));
        Preconditions.checkArgument(!(length > Constants.MAX_ROUTE_LENGTH));
        Preconditions.checkArgument(!(length < Constants.MIN_ROUTE_LENGTH));

        this.id = Objects.requireNonNull(id);
        this.station1 = Objects.requireNonNull(station1);
        this.station2 = Objects.requireNonNull(station2);
        this.length = length;
        this.level = Objects.requireNonNull(level);
        this.color = color;
    }

    /**
     * @return Renvoie l'identification textuelle unique de la route
     */
    public String id() {
        return id;
    }

    /**
     * @return Renvoie la station de départ
     */
    public Station station1() {
        return station1;
    }

    /**
     * @return Renvoie la station d'arrivée
     */
    public Station station2() {
        return station2;
    }

    /**
     * @return Renvoie la longueur de la route (int)
     */
    public int length() {
        return length;
    }

    /**
     * @return Renvoie le niveau de la route (UNDERGROUND ou OVERGROUND)
     */
    public Level level() {
        return level;
    }

    /**
     * @return Renvoie la couleur de la route, ou null s'il n'y en a pas.
     */
    public Color color() {
        return color;
    }

    /**
     * @return Renvoie une liste des stations avec dans l'ordre la station 1 puis la 2.
     */
    public List<Station> stations() {
        return List.of(station1, station2);
    }

    /**
     * Retourne la station opposée à celle donnée en argument
     *
     * @param station La station de départ ou d'arrivée de la route.
     * @return Renvoie la station de départ si l'argument est la station d'arrivée et vice-versa.
     * @throws IllegalArgumentException si la station n'est ni l'arrivée ni le départ.
     */
    public Station stationOpposite(Station station) {
        Preconditions.checkArgument(stations().contains(station));
        return station.equals(station1) ? station2 : station1;
    }

    /**
     * @return Retourne toutes les combinaisons de cartes utilisables pour capturer la route dans une List de SortedBags
     */
    public List<SortedBag<Card>> possibleClaimCards() {
        List<SortedBag<Card>> bagList = new ArrayList<>();
        //Route sans couleurs
        if (this.color==null){
            //Rajoute toutes les cartes de couleurs
            for (Card car : Card.CARS) {
                bagList.add(SortedBag.of(length, car));
            }
        }
        //Route avec couleur
        else {
            //Rajoute la carte avec la couleur de la route
            bagList.add(SortedBag.of(length, Card.of(color)));
        }

        //Tunnel, remplace les cartes de couleurs avec des cartes locomotives
        if (level.equals(Level.UNDERGROUND)){
            for (SortedBag<Card> cards : List.copyOf(bagList)) {
                for (int i = 1; i < length; i++) {
                    bagList.add(SortedBag.of(length-i, cards.get(i), i, Card.LOCOMOTIVE));
                }
            }
            bagList.add(SortedBag.of(length, Card.LOCOMOTIVE));
        }

        // Trie la liste en fonction du nombre de locomotives
        bagList.sort(
                Comparator.comparingInt(cs -> cs.countOf(Card.LOCOMOTIVE))
        );

        return bagList;
    }

    /**
     * @param claimCards Les cartes utilisées pour capturer le tunnel
     * @param drawnCards Les trois cartes piochées par le joueur lors de la tentative de capture
     * @return le nombre de cartes à rejouer pour s'emparer du tunnel
     * @throws IllegalArgumentException si il n'y a pas exactement 3 cartes dans drawnCards ou si la route n'est pas
     *                                  UNDERGROUND
     */
    public int additionalClaimCardsCount(SortedBag<Card> claimCards, SortedBag<Card> drawnCards) {
        Preconditions.checkArgument(drawnCards.size() == Constants.ADDITIONAL_TUNNEL_CARDS && this.level().equals(Level.UNDERGROUND));
        int requirement = 0;
        for (Card c : drawnCards) {
            if (claimCards.contains(c) || c.equals(Card.LOCOMOTIVE)) {
                requirement++;
            }
        }
        return requirement;
    }

    /**
     * @return retourne le nombre de points appropriés vis-à-vis de la longueur de la route.
     */
    public int claimPoints() {
        return Constants.ROUTE_CLAIM_POINTS.get(length);
    }

    /**
     * Enum qui indique si la route est en surface ou en tunnel
     */
    public enum Level {
        OVERGROUND,
        UNDERGROUND
    }
}

