package ch.epfl.tchu.net;

/**
 * Créé le 12.04.2021 à 14:55
 *
 * Enumère les types de messages que le serveur peut envoyer aux clients.
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public enum MessageId {
    INIT_PLAYERS,
    RECEIVE_INFO,
    UPDATE_STATE,
    SET_INITIAL_TICKETS,
    CHOOSE_INITIAL_TICKETS,
    NEXT_TURN,
    CHOOSE_TICKETS,
    DRAW_SLOT,
    ROUTE,
    CARDS,
    CHOOSE_ADDITIONAL_CARDS
}
