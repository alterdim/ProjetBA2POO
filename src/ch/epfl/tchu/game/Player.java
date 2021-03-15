package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;

import java.util.List;
import java.util.Map;

/**
 * Créé le 15.03.2021 à 14:04
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public interface Player {

    /**
     * représente les trois types d'actions qu'un joueur peut effectuer durant un tour
     */
    enum TurnKind{
        DRAW_TICKETS,
        DRAW_CARDS,
        CLAIM_ROUTE;

        /**
         * Liste de toutes les types d'actions faisable durant un tour
         */
        public static final List<TurnKind> ALL = List.of(TurnKind.values());
    }

    /**
     * est appelée au début de la partie pour communiquer au joueur son nom et la liste de tous les joueurs (le siens inclus)
     * @param ownId sa propre identité
     * @param playerNames noms des différents joueurs, le sien inclus
     */
    void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames);

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

    /**
     * communiquer au joueur les cinq billets qui lui ont été distribués au début de la partie
     * @param tickets SortedBag de 5 tickets
     */
    void setInitialTicketChoice(SortedBag<Ticket> tickets);

    /**
     * demande au joueur quels billets qu'on lui a distribué initialement(au début de la partie) il garde
     */
    SortedBag<Ticket> chooseInitialTickets();

    /**
     * Quel action le joueur décide de faire durant son tour
     * @return Type d'action effectuée durant le tour
     */
    TurnKind nextTurn();

    /**
     * est appelée lorsque le joueur a décidé de tirer des billets supplémentaires en cours de partie, afin de lui communiquer les billets tirés et de savoir lesquels il garde
     * @param options les billets tirés
     * @return les billets garder
     */
    SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options);

    /**
     * Lorsque DRAW_CARDS est choisi, détermine depuis quel emplacement les cartes seront prises : cartes visible ou la pioche
     * @return si carte visible entre 0 et 4 inclus, ou si pioche Constants.DECK_SLOT (c-à-d -1)
     */
    int drawSlot();

    /**
     * est appelée lorsque le joueur a décidé de (tenter de) s'emparer d'une route, afin de savoir de quelle route il s'agit
     */
    Route claimedRoute();

    /**
     * est appelée lorsque le joueur a décidé de (tenter de) s'emparer d'une route, afin de savoir quelle(s) carte(s) il désire initialement utiliser pour cela
     * @return carte(s) que le joueur désir initialement utiliser
     */
    SortedBag<Card> initialClaimCards();

    /**
     * est appelée lorsque le joueur a décidé de tenter de s'emparer d'un tunnel et que des cartes additionnelles sont nécessaires, afin de savoir quelle(s) carte(s) il désire utiliser pour cela
     * @param options carte(s) additionnelles disponibles
     * @return carte(s) additionnelles choisies par le joueur si non vide, sinon (multiensemble vide) cela signifie que le joueur ne désire pas (ou ne peut pas) choisir l'une de ces possibilités
     */
    SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options);

}
