package ch.epfl.tchu.game;

import com.sun.java.accessibility.util.AccessibilityListenerList;

import java.util.List;

/**
 * Identité du joueur
 *
 * Créé le 08.03.2021 à 12:43
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public enum PlayerId {
    PLAYER_1,
    PLAYER_2;

    /**
     * Liste des joueurs
     */
    public static final List<PlayerId> ALL = List.of(PlayerId.values());
    /**
     * Nombres des joueurs
     */
    public static final int COUNT = ALL.size();

    /**
     * Retourne l'autre joueur
     * @return le joueur suivant
     */
    public PlayerId next(){
        /*if (this.equals(PLAYER_1)) return PLAYER_2;
        else return PLAYER_1;*/
        return this.equals(PLAYER_2) ? PLAYER_1 : PLAYER_2;
    }
}