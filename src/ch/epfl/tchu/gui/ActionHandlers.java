package ch.epfl.tchu.gui;


import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Ticket;

/**
 * Créé le 28.04.2021 à 15:45
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 *
 * Contient les gestionnaires d'actions pour les actions des joueurs
 */
public interface ActionHandlers {

    /**
     * Interface pour lorsque le joueur désire tirer des billets
     */
    interface DrawTicketsHandler {
        void onDrawTickets();
    }

    /**
     *  Interface pour lorsque le joueur désire tirer une carte à un emplacement donné (0 à 4 ou -1 pour la pioche)
     */
    interface DrawCardHandler {
        void onDrawCard(int slot);
    }

    /**
     * Interface pour lorsque le joueur désire capturer une route au moyen des cartes données.
     */
    interface ClaimRouteHandler {
        void onClaimRoute(Route route, SortedBag<Card> cards);
    }

    /**
     * Interface pour lorsque le joueur a choisi de garder les billets donnés à la suite d'un tirage.
     */
    interface ChooseTicketsHandler {
        void onChooseTickets(SortedBag<Ticket> tickets);
    }

    /**
     * Interface pour lorsque le joueur a choisi de capturer ou d'abandonner un tunnel avec des cartes additionnelles.
     */
    interface ChooseCardsHandler {
        void onChooseCards(SortedBag<Card> cards);
    }
}
