package ch.epfl.tchu.game;

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
    PLAYER_2
    //, PLAYER_3 //A commenter/décommenter pour activer/désactiver le 3 troisième joueur
    ;

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
    public PlayerId next() {
        //Permettrait l 'ajout de davantage de joueurs
        if (this.ordinal()+1==values().length) return values()[0];
        else return values()[this.ordinal()+1];
    }
}
