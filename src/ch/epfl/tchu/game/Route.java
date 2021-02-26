package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *  Une route relie deux stations et possède plusieurs caractéristiques.
 *  Fichier créé à 14:28 le 23.02.2021
 *
 *  @author Louis Gerard (296782)
 *  @author Célien Muller (310777)
 *
 *
 */

public final class Route {
    private final String id;
    private final Station station1;
    private final Station station2;
    private final int length;
    private final Level level;
    private final Color color;
    SortedBag.Builder<Card> cardsB;

    /**
     * Enum qui indique si la route est en surface ou en tunnel
     */
    public enum Level {
        OVERGROUND,
        UNDERGROUND
    }

    /**
     * @param id L'identification unique de la route. Chaine de caractères, pas un int comme les stations !
     * @param station1 Station de départ
     * @param station2 Station d'arrivée
     * @param length Longueur de la route (affecte le nombre de cartes à utiliser pour la capturer)
     * @param level Level.UNDERGROUND ou Level.OVERGROUND, affecte la règle de capture
     * @param color Couleur de la route, null si elle est neutre
     */
    public Route(String id, Station station1, Station station2, int length, Level level, Color color) {
        Preconditions.checkArgument(!station1.equals(station2));
        Preconditions.checkArgument(!(length > Constants.MAX_ROUTE_LENGTH));
        Preconditions.checkArgument(!(length < Constants.MIN_ROUTE_LENGTH));
        Objects.requireNonNull(level);
        Objects.requireNonNull(station1);
        Objects.requireNonNull(station2);
        Objects.requireNonNull(id);

        this.id = id;
        this.station1 = station1;
        this.station2 = station2;
        this.length = length;
        this.level = level;
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
        List<Station> stationList = new ArrayList<>();
        stationList.add(station1);
        stationList.add(station2);
        return stationList;
    }

    /**
     * @param station La station de départ ou d'arrivée de la route.
     * @throws IllegalArgumentException si la station n'est ni l'arrivée ni le départ.
     * @return Renvoie la station de départ si l'argument est la station d'arrivée et vice-versa.
     */
    public Station stationOpposite(Station station) {
        boolean isStation1 = station.equals(station1);
        boolean isStation2 = station.equals(station2);
        Preconditions.checkArgument(isStation1 || isStation2);
        if (isStation1) {
            return station2;
        }
        else {
            return station1;
        }
    }

    /**
     * @return Retourne toutes les combinaisons de cartes utilisables pour capturer la route dans une List de SortedBags
     */
    public List<SortedBag<Card>> possibleClaimCards() {
        boolean rainbow = true;
        //Si la route ne possède pas le couleur, le flag rainbow s'active pour autoriser toutes les couleurs de cartes.
        if (color != null) {
            rainbow = false;
        }
        List<SortedBag<Card>> bagList = new ArrayList<>();
        SortedBag<Card> bag;
        //On itère de 0 à la longueur pour commencer par les mains qui utilisent le moins de locomotives.
        for (int i = 0; i <= length; i++) {
            //A chaque itération, le builder ainsi que le bag sont remis à zéro.
            cardsB = new SortedBag.Builder<>();
            if (!rainbow) {
                cardsB.add(i, Card.LOCOMOTIVE);
                bag = cardsB.add(length - i, Card.of(color)).build();
                bagList.add(bag);
            }
            else {
                for (Color c : Color.values()) {
                    cardsB = new SortedBag.Builder<>();
                    // pour éviter les doublons de mains à n locomotives, on arrête la fonction prématurément quand
                    // arrive i = length.
                    if (i == length) {
                        bag = cardsB.add(i, Card.LOCOMOTIVE).build();
                        bagList.add(bag);
                        return bagList;
                    }
                    cardsB.add(i, Card.LOCOMOTIVE);
                    bag = cardsB.add(length - i, Card.of(c)).build();
                    bagList.add(bag);
                }
            }

        }
            return bagList;
    }

    /**
     * @param claimCards Les cartes utilisées pour capturer le tunnel
     * @param drawnCards Les trois cartes piochées par le joueur lors de la tentative de capture
     * @throws IllegalArgumentException si il n'y a pas exactement 3 cartes dans drawnCards ou si la route n'est pas
     * UNDERGROUND
     * @return le nombre de cartes à rejouer pour s'emparer du tunnel
     */
    public int additionalClaimCardsCount(SortedBag<Card> claimCards, SortedBag<Card> drawnCards) {
        int requirement = 0;

        //TODO Relire consigne pour exception (ou /et) Je (Célien) pense et -> && au lieu de ||
        Preconditions.checkArgument(drawnCards.size() == 3 && this.level().equals(Level.UNDERGROUND));
        for (Card c : drawnCards) {
            if (claimCards.contains(c)) {
                requirement++;
            }
        }
        return requirement;

    }

    public int claimPoints(){
        //TODO ajouter méthode
        return 0;
    }
}

