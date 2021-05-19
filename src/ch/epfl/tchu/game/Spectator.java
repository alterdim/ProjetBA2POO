package ch.epfl.tchu.game;

import java.util.Map;

/**
 *
 * Représente un spectateur
 * Créé le 19.05.2021 à 12:34
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public interface Spectator /*extends Player*/{
    /**
     * est appelée lorsque l'on souhaite démarrer l'interface graphique du spectateur
     * @param playerNames noms des différents joueurs, le sien inclus
     * @param showHand est-ce que le spectateur doit voir la main des joueurs
     */
    void launchSpectator(Map<PlayerId, String> playerNames, boolean showHand);

    /**
     * est appelée chaque fois qu'une information doit être communiquée au joueur au cours de la partie (sous forme de chaine de caractères)
     * @param info information a communiquer
     */
    void receiveInfo(String info);

    /**
     * Informe le joueur de la nouvelle composante publique lors d'un changement d'état
     * @param newState nouvel état
     * @param ownState son propre état
     */
    void updateState(PublicGameState newState, PlayerState ownState);
}
