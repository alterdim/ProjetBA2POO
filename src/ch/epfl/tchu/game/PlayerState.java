package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.List;

/**
 * Créé le 08.03.2021 à 14:02
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public final class PlayerState extends PublicPlayerState {
    private final SortedBag<Ticket> tickets;
    private final  SortedBag<Card> cards;


    /**
     * Construit un état de joueur
     * @param tickets tickets du joueur
     * @param cards cartes du joueur
     * @param routes routes appartenant au joueur
     */
    public PlayerState(SortedBag<Ticket> tickets, SortedBag<Card> cards, List<Route> routes) {
        super(tickets.size(), cards.size(), routes);
        this.tickets=tickets;
        this.cards=cards;
    }

    /**
     * Initialise un état de joueur avec uniquement les cartes initiales distribuées, mais sans route ni tickets
     * @param initialCards les cartes initiales données
     * @return l'état initial d'un joueur
     * @throws IllegalArgumentException si le nombre de cartes initiales ne vaut pas 4
     */
    public static PlayerState initial(SortedBag<Card> initialCards){
        Preconditions.checkArgument(initialCards.size()==4);
        return new PlayerState(new SortedBag.Builder<Ticket>().build(), initialCards, List.of());
    }

    /**
     *
     * @return les billets du joueur
     */
    public SortedBag<Ticket> tickets(){
        return tickets;
    }

    /**
     * Ajoute des billets
     * @param newTickets des billets
     * @return Un nouvel état du joueur avec des billets en plus
     */
    public PlayerState withAddedTickets(SortedBag<Ticket> newTickets){
        return new PlayerState(newTickets.union(tickets()), cards(), routes());
    }

    /**
     * @return  les cartes wagon/locomotive du joueur
     */
    public SortedBag<Card> cards(){
        return cards;
    }

    /**
     * Ajoute une carte a l'état du joueur
     * @param card Une carte
     * @return Un nouvel état du joueur avec une carte en plus
     */
    public PlayerState withAddedCard(Card card){
        return new PlayerState(tickets(), new SortedBag.Builder<Card>().add(cards()).add(card).build(), routes());
    }

    /**
     * Ajoute des cartes
     * @param additionalCards des cartes
     * @return Un nouvel état du joueur avec des cartes en plus
     */
    public PlayerState withAddedCards(SortedBag<Card> additionalCards){
        return new PlayerState(tickets(), additionalCards.union(cards()), routes());
    }

    /**
     * Le joueur possède-t-il les wagons et les cartes pour s'emparer de la route
     * @param route une route
     * @return vrai s'il peut s'en emparer
     */
    public boolean canClaimRoute(Route route){
        if (cards.size() < route.length()) {
            return false;
        }
        List<SortedBag<Card>> possibleClaimCards = route.possibleClaimCards();
        for (SortedBag<Card> combination : possibleClaimCards) {
            if(cards.contains(combination)) {
                return true;
            }
        }
        return false;
    }
}
