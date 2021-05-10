package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Trail;

import java.util.ArrayList;
import java.util.List;

import static ch.epfl.tchu.gui.StringsFr.*;

/**
 * Générations des messages des actions des joueurs
 * <p>
 * Fichier créé à 14:34 le 02/03/2021
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public final class Info {
    private final String playerName;

    /**
     * Lie les messages au joueur
     *
     * @param playerName Nom du joueur
     */
    public Info(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Nom de la carte
     *
     * @param card  Une carte
     * @param count Le nombre de cartes. Doit être supérieur à 0.
     * @return un string en français lié à la couleur et à la multiplicité d'une carte.
     */
    public static String cardName(Card card, int count) {
        StringBuilder colorString = new StringBuilder();
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
        }
        colorString.append(plural(count));
        return colorString.toString();
    }

    /**
     * Message expliquant que les joueurs ont terminé æqo
     *
     * @param playerNames Noms des joueurs
     * @param points      Nombre de points des joueurs
     * @return retourne un message expliquant que les joueurs ont fini æqo avec le nombre de points
     */
    public static String draw(List<String> playerNames, int points) {
        String names = String.join(AND_SEPARATOR, playerNames);
        return String.format(DRAW, names, points);
    }

    public static String generateCardString(SortedBag<Card> cards) {
        String cardString = "";
        List<Card> presentCards = new ArrayList<>();
        for (Card c : Card.values()) {
            if (cards.contains(c)) {
                presentCards.add(c);
            }
        }
        if (presentCards.size() == 1) {
            Card uniqueColorCard = presentCards.get(0);
            cardString = String.format("%s %s", cards.countOf(uniqueColorCard), cardName(uniqueColorCard, cards.countOf(uniqueColorCard)));
        } else {
            Card lastBeforeLast = presentCards.get(presentCards.size() - 2); // Permet de prendre l'avant-dernière carte alphabétiquement
            Card lastCard = presentCards.get(presentCards.size() - 1);
            for (Card c : cards.toSet()) {
                cardString = String.format("%s%s %s", cardString, cards.countOf(c), cardName(c, cards.countOf(c)));
                if (c.equals(lastBeforeLast)) {
                    cardString = String.format("%s%s", cardString, AND_SEPARATOR);
                } else if (!c.equals(lastCard)) {
                    cardString = String.format("%s, ", cardString);
                }
            }
        }

        return cardString;
    }

    /**
     * Un message expliquant que le joueur commence à jouer
     *
     * @return Retourne un message expliquant que le joueur commence à jouer
     */
    public String willPlayFirst() {
        return String.format(WILL_PLAY_FIRST, playerName);
    }

    /**
     * @param count nombre de billets gardé
     * @return Retourne un message indiquant que le joueur a gardé un certain nombre de billets
     */
    public String keptTickets(int count) {
        return String.format(KEPT_N_TICKETS, playerName, count, plural(count));
    }

    /**
     * @return Retourne un message indiquant que c'est au joueur de jouer
     */
    public String canPlay() {
        return String.format(CAN_PLAY, playerName);
    }

    /**
     * @param count nombre de billets tirés
     * @return retourne le message déclarant que le joueur a tiré le nombre donné de billets
     */
    public String drewTickets(int count) {
        return String.format(DREW_TICKETS, playerName, count, plural(count));
    }

    /**
     * @return Retourne le message déclarant que le joueur a tiré une carte, sans indiquer quelle carte
     */
    public String drewBlindCard() {
        return String.format(DREW_BLIND_CARD, playerName);
    }

    /**
     * @param card Carte tirée
     * @return Retourne le message déclarant que le joueur a tiré une carte, avec le nom de cette dernière
     */
    public String drewVisibleCard(Card card) {
        return String.format(DREW_VISIBLE_CARD, playerName, cardName(card, 1));
    }

    /**
     * @param route Route prise par le joueur
     * @param cards Cartes utilisées pour prendre la route
     * @return Retourne le message déclarant que le joueur s'est emparé de la route donnée au moyen des cartes données
     */
    public String claimedRoute(Route route, SortedBag<Card> cards) {
        return String.format(CLAIMED_ROUTE, playerName, generateRouteString(route), generateCardString(cards));
    }

    /**
     * @param route        Route que le joueur souhaite s'emparer
     * @param initialCards Cartes avec lesquels le joueur souhaite prendre la route
     * @return retourne le message déclarant que le joueur désire s'emparer de la route en tunnel donnée en utilisant les cartes données qui sont indiquées
     */
    public String attemptsTunnelClaim(Route route, SortedBag<Card> initialCards) {
        return String.format(ATTEMPTS_TUNNEL_CLAIM, playerName, generateRouteString(route), generateCardString(initialCards));
    }

    /**
     * @param drawnCards     Cartes supplémentaires
     * @param additionalCost Nombre de cartes additionnels
     * @return retourne le message déclarant que le joueur a tiré les trois cartes additionnelles données et qu'elles impliquent un coût additionnel du nombre de cartes donné
     */
    public String drewAdditionalCards(SortedBag<Card> drawnCards, int additionalCost) {
        if (additionalCost == 0) {
            return String.format(ADDITIONAL_CARDS_ARE, generateCardString(drawnCards)) + NO_ADDITIONAL_COST;
        }
        return String.format(ADDITIONAL_CARDS_ARE, generateCardString(drawnCards)) + String.format(SOME_ADDITIONAL_COST, additionalCost, plural(additionalCost));
    }

    /**
     * @param route Route donnée
     * @return Retourne le message déclarant que le joueur n'a pas pu (ou voulu) s'emparer du tunnel donné
     */
    public String didNotClaimRoute(Route route) {
        return String.format(DID_NOT_CLAIM_ROUTE, playerName, generateRouteString(route));
    }

    /**
     * @param carCount nombre de wagon
     * @return retourne le message déclarant que le joueur n'a plus que le nombre donné de wagons et donc qu'il s'agit du dernier tour
     */
    public String lastTurnBegins(int carCount) {
        return String.format(LAST_TURN_BEGINS, playerName, carCount, plural(carCount));
    }

    /**
     * @param longestTrail Chemin le plus long, ou l'un des plus long
     * @return retourne le message déclarant que le joueur obtient le bonus de longueur octroyé en fin de partie
     */
    public String getsLongestTrailBonus(Trail longestTrail) {
        return String.format(GETS_BONUS, playerName, longestTrail.station1() + EN_DASH_SEPARATOR + longestTrail.station2());
    }

    /**
     * @param points      Nombre de points du gagnant
     * @param loserPoints Nombre de points du perdant
     * @return retourne le message déclarant que le joueur remporte la partie avec le nombre de points donnés contre le nombre de points du perdant
     */
    public String won(int points, int loserPoints) {
        return String.format(WINS, playerName, points, plural(points), loserPoints, plural(loserPoints));
    }

    private String generateRouteString(Route route) {
        return String.join(EN_DASH_SEPARATOR, route.station1().name(), route.station2().name());
    }

}
