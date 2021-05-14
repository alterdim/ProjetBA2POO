package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static ch.epfl.tchu.game.Constants.ADDITIONAL_TUNNEL_CARDS;

/**
 * Représente l' état complet d' un joueur
 * <p>
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
        super(tickets.size(), cards.size(), routes); // cards est sortedBag (immuable) et routes n'est pas assigné à une variable dans cette classe, mais dispose bien d'une copy dans la classe mère
        this.tickets = tickets; // SortedBag est immuable, donc pas besoin de faire une copie
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
        Preconditions.checkArgument(initialCards.size() == Constants.INITIAL_CARDS_COUNT);
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
     * Le joueur possède-t-il les wagons et les cartes pour s'emparer de la route
     *
     * @param route une route
     * @return vrai s'il peut s'en emparer
     */
    public boolean canClaimRoute(Route route) {
        if (carCount() >= route.length()) {
            for (SortedBag<Card> combination : route.possibleClaimCards()) {
                if (cards.contains(combination)) {
                    return true;
                }
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
     *                             //     * @param drawnCards           les cartes piochées
     * @return une liste de SortedBags contenant les possibilités de cartes à jouer pour finir la prise d'un tunnel.
     * @throws IllegalArgumentException si le nombre de carte pas compris entre 1 et 3 (inclus), si ensemble cartes initiales vide ou contient plus de 2 types de cartes différents, l'ensemble des cartes tirées ne contient pas exactement 3 cartes
     */
    public List<SortedBag<Card>> possibleAdditionalCards(int additionalCardsCount, SortedBag<Card> initialCards) {
        Preconditions.checkArgument(additionalCardsCount >= 1); //nb de cartes 3>=x>=1
        Preconditions.checkArgument(additionalCardsCount <= ADDITIONAL_TUNNEL_CARDS);

        Preconditions.checkArgument(!initialCards.isEmpty()); // InitialCards vide
        Preconditions.checkArgument(initialCards.toMap().keySet().size() <= 2); // MAX 2 types de cartes


        SortedBag.Builder<Card> usableCardsType = new SortedBag.Builder<>();
        //Calcul les cartes qui restent dans la main et qui pourraient être utilisées
        for (Card cardsLeft : cards.difference(initialCards))
            if (initialCards.contains(cardsLeft) || cardsLeft.equals(Card.LOCOMOTIVE)) usableCardsType.add(cardsLeft);

        //Détermine les sous-ensembles
        List<SortedBag<Card>> options;
        if (usableCardsType.build().size() < additionalCardsCount) {
            return new ArrayList<>();
        } else
            options = new ArrayList<>(usableCardsType.build().subsetsOfSize(additionalCardsCount)); // liste qui sera renvoyée

        // Trie la liste en fonction du nombre de locomotives
        options.sort(
                Comparator.comparingInt(cs -> cs.countOf(Card.LOCOMOTIVE))
        );

        return options;
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
        return ticketPoints() + claimPoints();
    }


}
