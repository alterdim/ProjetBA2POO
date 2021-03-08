package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.Collections;
import java.util.List;

import static ch.epfl.tchu.game.Constants.INITIAL_CAR_COUNT;

/**
 *
 * partie publique de l'état d'un joueur
 *
 * Créé le 08.03.2021 à 13:01
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public class PublicPlayerState {

    private final int ticketCount;
    private final int cardCount;
    private final List<Route> routes;

    private final int carCount;
    private final int claimPoints;


    /**
     * construit l'état public d'un joueur
     * @param ticketCount nombre de billets
     * @param cardCount nombre de cartes
     * @param routes liste de routes appartenant au joueur
     * @throws IllegalArgumentException si le nombre de billets ou de cartes sont négatifs
     */
    public PublicPlayerState(int ticketCount, int cardCount, List<Route> routes){
        Preconditions.checkArgument(ticketCount>=0);
        Preconditions.checkArgument(cardCount>=0);

        this.ticketCount=ticketCount;
        this.cardCount=cardCount;
        this.routes=routes;

        //TODO a faire
        this.carCount=INITIAL_CAR_COUNT;
        this.claimPoints=3;
    }

    /**
     *
     * @return le nombre de billets que possède le joueur
     */
    public int ticketCount(){
        return ticketCount;
    }

    /**
     *
     * @return le nombre de cartes que possède le joueur
     */
    public int cardCount(){
        return cardCount;
    }

    /**
     *
     * @return les routes dont le joueur s'est emparé
     */
    public List<Route> routes(){
        return routes;
    }

    /**
     *
     * @return le nombre de wagons que possède le joueur
     */
    public int carCount(){
        return carCount;
    }

    /**
     *
     * @return le nombre de points de construction obtenus par le joueur
     */
    public int claimPoints(){
        return claimPoints;
    }
}
