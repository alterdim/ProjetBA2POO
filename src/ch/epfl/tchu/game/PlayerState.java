package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Créé le 08.03.2021 à 14:02
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public final class PlayerState extends PublicPlayerState {
    private final SortedBag<Ticket> tickets;
    private final SortedBag<Card> cards;


    /**
     * Construit un état de joueur
     *
     * @param tickets tickets du joueur
     * @param cards   cartes du joueur
     * @param routes  routes appartenant au joueur
     */
    public PlayerState(SortedBag<Ticket> tickets, SortedBag<Card> cards, List<Route> routes) {
        super(tickets.size(), cards.size(), routes);
        this.tickets = tickets;
        this.cards = cards;
    }

    /**
     * Initialise un état de joueur avec uniquement les cartes initiales distribuées, mais sans route ni tickets
     *
     * @param initialCards les cartes initiales données
     * @return l'état initial d'un joueur
     * @throws IllegalArgumentException si le nombre de cartes initiales ne vaut pas 4
     */
    public static PlayerState initial(SortedBag<Card> initialCards) {
        Preconditions.checkArgument(initialCards.size() == 4);
        return new PlayerState(new SortedBag.Builder<Ticket>().build(), initialCards, List.of());
    }

    /**
     * @return les billets du joueur
     */
    public SortedBag<Ticket> tickets() {
        return tickets;
    }

    /**
     * Ajoute des billets
     *
     * @param newTickets des billets
     * @return Un nouvel état du joueur avec des billets en plus
     */
    public PlayerState withAddedTickets(SortedBag<Ticket> newTickets) {
        return new PlayerState(newTickets.union(tickets()), cards(), routes());
    }

    /**
     * @return les cartes wagon/locomotive du joueur
     */
    public SortedBag<Card> cards() {
        return cards;
    }

    /**
     * Ajoute une carte a l'état du joueur
     *
     * @param card Une carte
     * @return Un nouvel état du joueur avec une carte en plus
     */
    public PlayerState withAddedCard(Card card) {
        return new PlayerState(tickets(), new SortedBag.Builder<Card>().add(cards()).add(card).build(), routes());
    }

    /**
     * Ajoute des cartes
     *
     * @param additionalCards des cartes
     * @return Un nouvel état du joueur avec des cartes en plus
     */
    public PlayerState withAddedCards(SortedBag<Card> additionalCards) {
        return new PlayerState(tickets(), additionalCards.union(cards()), routes());
    }

    /**
     * Le joueur possède-t-il les wagons et les cartes pour s'emparer de la route
     *
     * @param route une route
     * @return vrai s'il peut s'en emparer
     */
    public boolean canClaimRoute(Route route) {
        if (carCount() < route.length()) {
            return false;
        }
        List<SortedBag<Card>> possibleClaimCards = route.possibleClaimCards();
        for (SortedBag<Card> combination : possibleClaimCards) {
            if (cards.contains(combination)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param route une route
     * @return la liste de tous les ensembles de cartes que le joueur pourrait utiliser pour prendre possession de la route
     * @throws IllegalArgumentException si le joueur ne possède pas assez de wagons
     */
    public List<SortedBag<Card>> possibleClaimCards(Route route) {
        Preconditions.checkArgument(route.length() <= carCount());

        List<SortedBag<Card>> listPossibleClaimCards = new ArrayList<>();
        for (SortedBag<Card> combination : route.possibleClaimCards()) {
            if (cards.contains(combination)) {
                listPossibleClaimCards.add(combination);
            }
        }

        return listPossibleClaimCards;
    }

    /**
     * Fonction qui calcule les cartes à "rejouer" pour s'emparer d'un tunnel et qui renvoie les façons pour le joueur de les jouer.
     *
     * @param additionalCardsCount le nombre de cartes additionnelles à jouer
     * @param initialCards         les cartes jouées initialement
     * @param drawnCards           les cartes piochées
     * @return une liste de SortedBags contenant les possibilités de cartes à jouer pour finir la prise d'un tunnel.
     * @throws IllegalArgumentException si le nombre de carte pas compris entre 1 et 3 (inclus), si ensemble cartes initiales vide ou contient plus de 2 types de cartes différents, l'ensemble des cartes tirées ne contient pas exactement 3 cartes
     */
    public List<SortedBag<Card>> possibleAdditionalCards(int additionalCardsCount, SortedBag<Card> initialCards, SortedBag<Card> drawnCards) {
        Preconditions.checkArgument(additionalCardsCount >= 1 && additionalCardsCount <= 3); //nb de cartes 3>=x>=1
        Preconditions.checkArgument(!initialCards.isEmpty()); // InitialCards vide
        Preconditions.checkArgument(initialCards.toMap().keySet().size() <= 2); // MAX 2 types de cartes
        Preconditions.checkArgument(drawnCards.size() == 3); //3 éléments dans drawncards
        ArrayList<SortedBag<Card>> options; // liste qui sera renvoyée
        SortedBag.Builder<Card> builder = new SortedBag.Builder<>();
        SortedBag<Card> possibleCards = builder.build(); //won't do anything but we initialize it
        for (Card c : drawnCards) {
            if (initialCards.contains(c)) {
                builder.add(c);
            }
            possibleCards = builder.build(); //list of cards the player can add to capture the route for good
        }
        builder = new SortedBag.Builder<>(); //clean the builder
        List<Card> reducedHand = cards.toList();

        for (Card c : Card.values()) {
            if (initialCards.contains(c)) {
                removeMultipleFromList(reducedHand, c, initialCards.countOf(c));
            }
            if (!possibleCards.contains(c)) {
                reducedHand.removeAll(Collections.singletonList(c));
            }
        }
        if (reducedHand.size()  < additionalCardsCount) {
            return new ArrayList<>();
        }
        for (Card c : reducedHand) {
            builder.add(c);
        }
        if (cards.contains(Card.LOCOMOTIVE)) {
            builder.add(additionalCardsCount, Card.LOCOMOTIVE);
        }

        options = new ArrayList<>(builder.build().subsetsOfSize(additionalCardsCount));
        options.sort(Comparator.comparingInt(cs -> cs.countOf(Card.LOCOMOTIVE))); // tri par nombre de locomotives
        return options;

    }

    private void removeMultipleFromList(List<Card> list, Card card, int count) {
        for (int i = 0; i < count; i++) {
            list.remove(card);
        }
    }

    /**
     * @param route      La route à ajouter au nouveau playerstate
     * @param claimCards Les cartes dépensées pour obtenir la route
     * @return Le nouveau playerstate muni de la route et sans les claimcards.
     */
    public PlayerState withClaimedRoute(Route route, SortedBag<Card> claimCards) {
        List<Route> newRoutes = new ArrayList<>(routes());
        newRoutes.add(route);
        return new PlayerState(tickets, cards.difference(claimCards), newRoutes);
    }

    /**
     * @return Retourne le nombre de points obtenus par le joueur grâce à ses tickets.
     */
    public int ticketPoints() {
        int highestId = -1;
        int points = 0;
        for (Route r : routes()) {
            if (r.station1().id() > highestId) {
                highestId = r.station1().id();
            }
            if (r.station2().id() > highestId) {
                highestId = r.station2().id();
            }
        }

        StationPartition.Builder builder = new StationPartition.Builder(highestId + 1); //+1 car les arrays commencent à 0

        for (Route r : routes()) {
            builder.connect(r.station1(), r.station2());
        }

        StationPartition partition = builder.build();

        for (Ticket t : tickets) {
            points += t.points(partition);
        }
        return points;
    }

    /**
     * @return Retourne la somme des points des tickets et des bonus de routes.
     */
    public int finalPoints() {
        int totalPoints = 0;
        totalPoints += ticketPoints();
        totalPoints += claimPoints();
        return totalPoints;
    }


}
