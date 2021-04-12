package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.gui.Info;

import java.util.*;

/**
 * Fichier créé à 14:49 le 22/03/2021
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public final class Game {

    public static void play(Map<PlayerId, Player> players, Map<PlayerId, String> playerNames,
                            SortedBag<Ticket> tickets, Random rng) {
        //CHECK CONDITIONS
        Preconditions.checkArgument(players.values().size()==PlayerId.COUNT);
        Preconditions.checkArgument(playerNames.values().size()==PlayerId.COUNT);
        //Initialisation de variables utiles
        int twoMoreLoops = 0;

        Map<PlayerId, Info> playerInfos = new HashMap<>();
        //Initialiser le jeu
        //Gère les tickets
        GameState gameState = GameState.initial(tickets, rng);

//        Deck<Ticket> ticketDeck = Deck.of(tickets, rng);
        for (PlayerId p : players.keySet()) {
            Player player = players.get(p);
            playerInfos.put(p, new Info(playerNames.get(p)));
            player.initPlayers(p, playerNames);
        }
        //Annoncer le premier joueur
        tellEveryone(players, playerInfos.get(gameState.currentPlayerId()).willPlayFirst());

        //Générer les premiers tickets
        for (PlayerId p : players.keySet()) {
            Player player = players.get(p);

            player.setInitialTicketChoice(gameState.topTickets(Constants.INITIAL_TICKETS_COUNT));
            gameState=gameState.withoutTopTickets(Constants.INITIAL_TICKETS_COUNT);
            gameState = gameState.withInitiallyChosenTickets(p, player.chooseInitialTickets());

            updateEveryone(players, gameState);
        }

        //Annoncer le nombre de tickets gardés
        for (PlayerId p : players.keySet()) {
            tellEveryone(players,
                    playerInfos.get(p).keptTickets(gameState.playerState(p).ticketCount()));
        }
        //Boucle de jeu
        while (twoMoreLoops<=2){
            updateEveryone(players, gameState);
            Player currentPlayer = players.get(gameState.currentPlayerId());
            tellEveryone(players, playerInfos.get(gameState.currentPlayerId()).canPlay());
            if (gameState.lastTurnBegins()) {
                tellEveryone(players, playerInfos.get(gameState.currentPlayerId()).lastTurnBegins(gameState.currentPlayerState().carCount()));
            }
            switch (currentPlayer.nextTurn()) {
                case DRAW_TICKETS:
                    //Sélectionne les 3 premiers tickets
                    SortedBag<Ticket> drawnTickets = gameState.topTickets(Constants.IN_GAME_TICKETS_COUNT);
                    tellEveryone(players, playerInfos.get(gameState.currentPlayerId()).drewTickets(drawnTickets.size()));

                    //demande au joueur quels tickets il conserve
                    SortedBag<Ticket> keptTickets = currentPlayer.chooseTickets(drawnTickets);
                    gameState = gameState.withChosenAdditionalTickets(drawnTickets, keptTickets);
                    tellEveryone(players, playerInfos.get(gameState.currentPlayerId()).keptTickets(keptTickets.size()));
                    break;
                case DRAW_CARDS:
                    for (int i = 0; i<2; i++) {
                        updateEveryone(players, gameState);

                        gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
                        int drawSlot = currentPlayer.drawSlot();
                        if (drawSlot ==  Constants.DECK_SLOT) {
                            tellEveryone(players, playerInfos.get(gameState.currentPlayerId()).drewBlindCard());
                            gameState = gameState.withBlindlyDrawnCard();
                        }
                        else {
                            tellEveryone(players, playerInfos
                                    .get(gameState.currentPlayerId())
                                    .drewVisibleCard(gameState.cardState().faceUpCard(drawSlot))
                            );
                            gameState = gameState.withCardsDeckRecreatedIfNeeded(rng).withDrawnFaceUpCard(drawSlot);
                        }
                    }
                    break;
                case CLAIM_ROUTE:
                    Route claimedRoute = currentPlayer.claimedRoute();
                    SortedBag<Card> claimCards = currentPlayer.initialClaimCards();

                    if (claimedRoute.level().equals(Route.Level.UNDERGROUND)) {
                        tellEveryone(players, playerInfos.get(gameState.currentPlayerId()).attemptsTunnelClaim(claimedRoute, claimCards));

                        SortedBag.Builder<Card> builder = new SortedBag.Builder<>();
                        for (int i = 0; i<3; i++) {
                            gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
                            builder.add(1, gameState.topCard());
                            gameState = gameState.withoutTopCard();

                        }
                        SortedBag<Card> drawnCards = builder.build();

                        int addCardsCount = claimedRoute.additionalClaimCardsCount(claimCards, drawnCards);
                        gameState = gameState.withMoreDiscardedCards(drawnCards);

                        tellEveryone(players, playerInfos.get(gameState.currentPlayerId()).drewAdditionalCards(drawnCards, addCardsCount));
                        if (addCardsCount > 0) {

                            List<SortedBag<Card>> listPossibleAdditionalCards = gameState.currentPlayerState().possibleAdditionalCards(addCardsCount, claimCards, drawnCards);
                            if (!listPossibleAdditionalCards.isEmpty()) {
                                SortedBag<Card> chosenCards = currentPlayer.chooseAdditionalCards(
                                        listPossibleAdditionalCards
                                );
                                if (chosenCards.isEmpty()) {
                                    tellEveryone(players, playerInfos.get(gameState.currentPlayerId()).didNotClaimRoute(claimedRoute));
                                }
                                //claims avec carte + cartes additionnels
                                else {
                                    gameState = gameState.withClaimedRoute(claimedRoute, chosenCards.union(claimCards));
                                    tellEveryone(players, playerInfos.get(gameState.currentPlayerId()).claimedRoute(claimedRoute, claimCards));
                                }
                            }
                            else {
                                tellEveryone(players, playerInfos.get(gameState.currentPlayerId()).didNotClaimRoute(claimedRoute));
                            }
                        }
                        //claims si pas de cartes additionnelles
                        else {
                            gameState = gameState.withClaimedRoute(claimedRoute, claimCards);
                            tellEveryone(players, playerInfos.get(gameState.currentPlayerId()).claimedRoute(claimedRoute, claimCards));
                        }
                        break;
                    }
                    //claim route surface
                    else {
                        gameState = gameState.withClaimedRoute(claimedRoute, claimCards);
                        tellEveryone(players, playerInfos.get(gameState.currentPlayerId()).claimedRoute(claimedRoute, claimCards));
                    }
                    break;
            }
            if (gameState.lastPlayer() != null) {
                if (twoMoreLoops==0) tellEveryone(players, playerInfos.get(gameState.currentPlayerId()).lastTurnBegins(gameState.currentPlayerState().carCount()));
                twoMoreLoops++;
            }
            gameState = gameState.forNextTurn();
        }


        Trail longestTrail1 = Trail.longest(gameState.playerState(PlayerId.PLAYER_1).routes());
        Trail longestTrail2 = Trail.longest(gameState.playerState(PlayerId.PLAYER_2).routes());
        int score1 = gameState.playerState(PlayerId.PLAYER_1).finalPoints();
        int score2 = gameState.playerState(PlayerId.PLAYER_2).finalPoints();
        if (longestTrail1.length()>=longestTrail2.length()) {
            score1+=Constants.LONGEST_TRAIL_BONUS_POINTS;
            tellEveryone(players, playerInfos.get(PlayerId.PLAYER_1).getsLongestTrailBonus(longestTrail1));
        }
        if (longestTrail2.length()>=longestTrail1.length()) {
            score2+=Constants.LONGEST_TRAIL_BONUS_POINTS;
            tellEveryone(players, playerInfos.get(PlayerId.PLAYER_2).getsLongestTrailBonus(longestTrail2));
        }

        updateEveryone(players, gameState);

        if (score1 == score2) {
            List<String> names = new ArrayList<>(playerNames.values());
            tellEveryone(players, Info.draw(names, score1));
        }
        else if (score1 > score2) {
            tellEveryone(players, playerInfos.get(PlayerId.PLAYER_1).won(score1, score2));
        }
        else {
            tellEveryone(players, playerInfos.get(PlayerId.PLAYER_2).won(score2, score1));
        }
    }


    private static void tellEveryone(Map<PlayerId, Player> players, String info) {
        for (Player p : players.values()) {
            p.receiveInfo(info);
        }
    }

    private static void updateEveryone(Map<PlayerId, Player> players, GameState newState) {
        for (PlayerId p : players.keySet()) {
            Player player = players.get(p);
            player.updateState(newState, newState.playerState(p));
        }
    }
}
