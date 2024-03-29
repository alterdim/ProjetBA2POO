package ch.epfl.tchu.game;

/**
 * Créé à 13:48 le 22.02.2021
 *
 *  * @author Louis Gerard (296782)
 *  * @author Célien Muller (310777)
 *
 *  Interface qui valide les trajets reliés par les joueurs au cours de la partie.
 */
public interface StationConnectivity {
    boolean connected(Station s1, Station s2);
}
