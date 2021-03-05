package ch.epfl.tchu.gui;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Trail;

import java.util.ArrayList;
import java.util.List;

import static ch.epfl.tchu.gui.StringsFr.*;

/**
 * Fichier créé à 14:34 le 02/03/2021
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public class Info {
    private final String playerName;

    public Info(String playerName) {
        this.playerName = playerName;

    }

    /**
     * @param card Une carte
     * @param count Le nombre de cartes. Doit être supérieur à 0.
     * @throws IllegalArgumentException si la carte demandée n'a pas de texte français associé (ne devrait jamais arriver),
     * ou si le nombre de cartes n'est pas strictement positif.
     * @return un string en français lié à la couleur et à la multiplicité d'une carte.
     */
    public static String cardName(Card card, int count) {
        Preconditions.checkArgument(count > 0);
        StringBuilder colorString=new StringBuilder();
        switch (card) {
            case BLACK:
                colorString.append(BLACK_CARD);
                break;
            case VIOLET:
                colorString.append(VIOLET_CARD);
                break;
            case BLUE:
                colorString.append(BLUE_CARD);
                break;
            case GREEN:
                colorString.append(GREEN_CARD);
                break;
            case YELLOW:
                colorString.append(YELLOW_CARD);
                break;
            case ORANGE:
                colorString.append(ORANGE_CARD);
                break;
            case RED:
                colorString.append(RED_CARD);
                break;
            case WHITE:
                colorString.append(WHITE_CARD);
                break;
            case LOCOMOTIVE:
                colorString.append(LOCOMOTIVE_CARD);
                break;
            default:
                colorString.append("somethingIsWrong !");
                throw new IllegalArgumentException();
        }
        colorString.append(plural(count));
        return colorString.toString();
    }

    public static String draw(List<String> playerNames, int points) {
        String names = playerNames.get(0) + AND_SEPARATOR + playerNames.get(1);
        return String.format(DRAW, names, points);
    }

    public String willPlayFirst() {
        return String.format(WILL_PLAY_FIRST, playerName);
    }

    public String keptTickets(int count) {
        return String.format(KEPT_N_TICKETS, playerName, count, plural(count));
    }

    public String canPlay() {
        return String.format(CAN_PLAY, playerName);
    }

    public String drewTickets(int count) {
        return String.format(DREW_TICKETS, playerName, count, plural(count));
    }

    public String drewBlindCard() {
        return String.format(DREW_BLIND_CARD, playerName);
    }

    public String drewVisibleCard(Card card) {
        return String.format(DREW_VISIBLE_CARD, playerName, cardName(card, 1));
    }

    public String claimedRoute(Route route, SortedBag<Card> cards) {
        return String.format(CLAIMED_ROUTE, playerName, generateRouteString(route), generateCardString(cards));
    }

    public String attemptsTunnelClaim(Route route, SortedBag<Card> initialCards) {
        return String.format(ATTEMPTS_TUNNEL_CLAIM, playerName, generateRouteString(route), generateCardString(initialCards));
    }

    public String drewAdditionalCards(SortedBag<Card> drawnCards, int additionalCost) {
        if (additionalCost == 0) {
            return String.format(ADDITIONAL_CARDS_ARE, generateCardString(drawnCards)) + NO_ADDITIONAL_COST;
        }
        return String.format(ADDITIONAL_CARDS_ARE, generateCardString(drawnCards) + String.format(SOME_ADDITIONAL_COST, additionalCost, plural(additionalCost)));
    }

    public String didNotClaimRoute(Route route) {
        return String.format(DID_NOT_CLAIM_ROUTE, playerName, generateRouteString(route));
    }

    public String lastTurnBegins(int carCount) {
        return String.format(LAST_TURN_BEGINS, playerName, carCount, plural(carCount));
    }

    public String getsLongestTrailBonus(Trail longestTrail) {
        return String.format(GETS_BONUS, playerName, longestTrail.station1() + EN_DASH_SEPARATOR + longestTrail.station2());
    }

    public String won(int points, int loserPoints) {
        return String.format(WINS, playerName, points, plural(points), loserPoints, plural(loserPoints));
    }

    private String generateCardString(SortedBag<Card> cards) {
        String cardString = "";
        int count;
        List<Card> presentCards = new ArrayList<>();
        for (Card c : Card.values()) {
            if (cards.contains(c)) {
                presentCards.add(c);
            }
        }
        Card lastBeforeLast = presentCards.get(presentCards.size()-2); // Permet de prendre l'avant-dernière carte alphabétiquement
        Card lastCard = presentCards.get(presentCards.size()-1);
        for (Card c: cards.toSet()) {
            cardString = String.format("%s%s %s",cardString, cards.countOf(c), cardName(c, cards.countOf(c)));
            if (c.equals(lastBeforeLast)) {
                cardString = String.format("%s%s", cardString, AND_SEPARATOR);
            }
            else if (!c.equals(lastCard)){
                cardString = String.format("%s, ", cardString);
            }
        }
    return cardString;
    }

    private String generateRouteString(Route route) {
        return route.station1() + EN_DASH_SEPARATOR + route.station2();
    }

}
