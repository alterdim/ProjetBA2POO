package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.gui.Info;

import java.util.*;

/**
 * Représente une partie de tCHu
 * <p>
 * Fichier créé à 14:49 le 22/03/2021
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public final class Game {

    private Game() {}

    /**
     * @param players     Une Map liant les id des joueurs (enum PlayerId) et les joueurs.
     * @param playerNames Une Map liant les id des joueurs et leurs noms.
     * @param tickets     Un SortedBag contenant les tickets (objectifs) à mettre en jeu.
     * @param rng         Un générateur de nombre aléatoires.
     */
    public static void play(Map<PlayerId, Player> players, Map<PlayerId, String> playerNames,
                            SortedBag<Ticket> tickets, Random rng) {
        //Préconditions (nombre de joueurs adéquat)
        Preconditions.checkArgument(players.size() == PlayerId.COUNT);
        Preconditions.checkArgument(playerNames.size() == PlayerId.COUNT);

        //Initialisation de variables utiles
        int endingGameTrigger = 0;


        //Initialiser le jeu
        GameState gameState = GameState.initial(tickets, rng);
        Map<PlayerId, Info> playerInfos = new EnumMap<>(PlayerId.class);

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
            gameState = gameState.withoutTopTickets(Constants.INITIAL_TICKETS_COUNT);
            updateEveryone(players, gameState);
            gameState = gameState.withInitiallyChosenTickets(p, player.chooseInitialTickets());

        }

        //Annoncer le nombre de tickets gardés
        for (PlayerId p : players.keySet()) {
            tellEveryone(players,
                    playerInfos.get(p).keptTickets(gameState.playerState(p).ticketCount()));
        }

        //Boucle de jeu
        while (endingGameTrigger < 2) {
            Player currentPlayer = players.get(gameState.currentPlayerId());
            tellEveryone(players, playerInfos.get(gameState.currentPlayerId()).canPlay());

            updateEveryone(players, gameState);
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
                    for (int i = 0; i < 2; i++) {
                        gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
                        updateEveryone(players, gameState);
                        int drawSlot = currentPlayer.drawSlot();
                        if (drawSlot == Constants.DECK_SLOT) {
                            tellEveryone(players, playerInfos.get(gameState.currentPlayerId()).drewBlindCard());
                            gameState = gameState.withBlindlyDrawnCard();
                        } else {
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

                    if (claimedRoute.level().equals(Route.Level.OVERGROUND)) {
                        //claim route surface
                        gameState = gameState.withClaimedRoute(claimedRoute, claimCards);
                        tellEveryone(players, playerInfos.get(gameState.currentPlayerId()).claimedRoute(claimedRoute, claimCards));
                    }
                    //Tunnel
                    else {
                        tellEveryone(players, playerInfos.get(gameState.currentPlayerId()).attemptsTunnelClaim(claimedRoute, claimCards));

                        SortedBag.Builder<Card> builder = new SortedBag.Builder<>();
                        for (int i = 0; i < Constants.ADDITIONAL_TUNNEL_CARDS; i++) {
                            gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
                            builder.add(gameState.topCard());
                            gameState = gameState.withoutTopCard();

                        }
                        SortedBag<Card> drawnCards = builder.build();

                        int addCardsCount = claimedRoute.additionalClaimCardsCount(claimCards, drawnCards);
                        gameState = gameState.withMoreDiscardedCards(drawnCards);

                        tellEveryone(players, playerInfos.get(gameState.currentPlayerId()).drewAdditionalCards(drawnCards, addCardsCount));

                        if (addCardsCount > 0) {
                            List<SortedBag<Card>> listPossibleAdditionalCards = gameState.currentPlayerState().possibleAdditionalCards(addCardsCount, claimCards/*, drawnCards*/);
                            if (!listPossibleAdditionalCards.isEmpty()) {
                                SortedBag<Card> chosenCards = currentPlayer.chooseAdditionalCards(
                                        listPossibleAdditionalCards
                                );
                                //prends la route avec les cartes initiales et additionnels
                                if (!chosenCards.isEmpty()) {
                                    gameState = gameState.withClaimedRoute(claimedRoute, chosenCards.union(claimCards));
                                    tellEveryone(players, playerInfos.get(gameState.currentPlayerId()).claimedRoute(claimedRoute, claimCards));
                                    break;
                                }
                            }
                            else {
                                //le joueur ne prend pas la route
                                tellEveryone(players, playerInfos.get(gameState.currentPlayerId()).didNotClaimRoute(claimedRoute));
                            }
                        }
                        //claims si pas de cartes additionnelles
                        else {
                            gameState = gameState.withClaimedRoute(claimedRoute, claimCards);
                            tellEveryone(players, playerInfos.get(gameState.currentPlayerId()).claimedRoute(claimedRoute, claimCards));
                        }
                    }
                    break;
            }

            //Gestion des derniers tours et annonce de la fin.
            if (gameState.lastTurnBegins()) {
                tellEveryone(players, playerInfos.get(gameState.currentPlayerId()).lastTurnBegins(gameState.currentPlayerState().carCount()));
            }

            if (gameState.lastPlayer() != null) {
                endingGameTrigger++;
            }

            gameState = gameState.forNextTurn();
        }
        //Informe les joueurs de l'état du jeu à la fin de la partie
        updateEveryone(players, gameState);


        //TODO faire une boucle sur PlayerId (pour permettre l'ajout de davantage de joueurs
        //Calcul du longestTrail, du total des points
        /*Map<PlayerId, Trail> playersLongestTrail = new EnumMap<>(PlayerId.class);
        int longestTrailLength=0;
        for (PlayerId playerId : PlayerId.values()) {
            Trail currentPlayerTrail = Trail.longest(gameState.playerState(playerId).routes());
            int currentPlayerLength = currentPlayerTrail.length();
            playersLongestTrail.put(playerId, currentPlayerTrail);
            if (currentPlayerLength>longestTrailLength) {
                longestTrailLength=currentPlayerLength;
            }
        }
        Map<PlayerId, Integer> playersScores = new EnumMap<>(PlayerId.class);
        int maxScore=0;
        for (PlayerId playerId : PlayerId.values()) {
            Trail currentPlayerTrail = playersLongestTrail.get(playerId);
            int currentPlayerScore = gameState.playerState(playerId).finalPoints();
            if (longestTrailLength==currentPlayerTrail.length()){
                currentPlayerScore+=Constants.LONGEST_TRAIL_BONUS_POINTS;
                tellEveryone(players, playerInfos.get(playerId).getsLongestTrailBonus(currentPlayerTrail));
            }
            if (currentPlayerScore>maxScore) maxScore = currentPlayerScore;
            playersScores.put(playerId, currentPlayerScore);
        }

        for (PlayerId playerId : PlayerId.values()) {

            int finalMaxScore = maxScore;
            var n =playersScores.values().stream().filter(score -> score.equals(finalMaxScore)).count();

            if (n==PlayerId.COUNT){
                List<String> names = new ArrayList<>(playerNames.values());
                tellEveryone(players, Info.draw(names, maxScore));
            }
            else if (n==1){
//                tellEveryone(players, playerInfos.get(PlayerId.PLAYER_1).won(score1, score2));
            }

        }*/


        Trail longestTrail1 = Trail.longest(gameState.playerState(PlayerId.PLAYER_1).routes());
        Trail longestTrail2 = Trail.longest(gameState.playerState(PlayerId.PLAYER_2).routes());
        int score1 = gameState.playerState(PlayerId.PLAYER_1).finalPoints();
        int score2 = gameState.playerState(PlayerId.PLAYER_2).finalPoints();

        if (longestTrail1.length() >= longestTrail2.length()) {
            score1 += Constants.LONGEST_TRAIL_BONUS_POINTS;
            tellEveryone(players, playerInfos.get(PlayerId.PLAYER_1).getsLongestTrailBonus(longestTrail1));
        }
        if (longestTrail2.length() >= longestTrail1.length()) {
            score2 += Constants.LONGEST_TRAIL_BONUS_POINTS;
            tellEveryone(players, playerInfos.get(PlayerId.PLAYER_2).getsLongestTrailBonus(longestTrail2));
        }


        //Annonce du gagnant ou de la gagnante de la partie.
        if (score1 == score2) {
            List<String> names = new ArrayList<>(playerNames.values());
            tellEveryone(players, Info.draw(names, score1));
        } else if (score1 > score2) {
            tellEveryone(players, playerInfos.get(PlayerId.PLAYER_1).won(score1, score2));
        } else {
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
