package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.gui.Info;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Représente une partie de tCHu
 *
 * Fichier créé à 14:49 le 22/03/2021
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public final class Game {

    private Game() {
    }

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
            playerInfos.put(p, new Info(playerNames.get(p)));
            players.get(p).initPlayers(p, playerNames);
        }

        //Annoncer le premier joueur
        tellEveryone(players, playerInfos.get(gameState.currentPlayerId()).willPlayFirst());

        //Générer les premiers tickets
        for (PlayerId p : players.keySet()) {
            players.get(p).setInitialTicketChoice(gameState.topTickets(Constants.INITIAL_TICKETS_COUNT));
            gameState = gameState.withoutTopTickets(Constants.INITIAL_TICKETS_COUNT);
        }

        updateEveryone(players, gameState);

        //Demande aux joueurs quels tickets ils souhaitent conserver
        for (PlayerId p : players.keySet()) {
            gameState = gameState.withInitiallyChosenTickets(p, players.get(p).chooseInitialTickets());
        }

        //Annonce le nombre de tickets gardés, une fois que tous les joueurs ont fait leurs choix
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
                            List<SortedBag<Card>> listPossibleAdditionalCards = gameState.currentPlayerState().possibleAdditionalCards(addCardsCount, claimCards);
                            if (!listPossibleAdditionalCards.isEmpty()) {
                                SortedBag<Card> chosenCards = currentPlayer.chooseAdditionalCards(
                                        listPossibleAdditionalCards
                                );
                                //prends la route avec les cartes initiales et additionnels
                                if (!chosenCards.isEmpty()) {
                                    gameState = gameState.withClaimedRoute(claimedRoute, chosenCards.union(claimCards));
                                    tellEveryone(players, playerInfos.get(gameState.currentPlayerId()).claimedRoute(claimedRoute, chosenCards.union(claimCards)));
                                }
                            } else {
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


        //Calcul du longestTrail de la partie
        Map<PlayerId, Trail> playersLongestTrail = new EnumMap<>(PlayerId.class);
        int longestTrailLength = 0;
        for (PlayerId playerId : PlayerId.ALL) {
            Trail currentPlayerTrail = Trail.longest(gameState.playerState(playerId).routes());
            int currentPlayerLength = currentPlayerTrail.length();
            playersLongestTrail.put(playerId, currentPlayerTrail);
            if (currentPlayerLength > longestTrailLength) {
                longestTrailLength = currentPlayerLength;
            }
        }

        //Calcul les points obtenu par chacun des joueurs
        Map<PlayerId, Integer> playersScores = new EnumMap<>(PlayerId.class);
        for (PlayerId playerId : PlayerId.ALL) {
            Trail currentPlayerTrail = playersLongestTrail.get(playerId);
            int currentPlayerScore = gameState.playerState(playerId).finalPoints();
            if (longestTrailLength == currentPlayerTrail.length()) {
                currentPlayerScore += Constants.LONGEST_TRAIL_BONUS_POINTS;
                tellEveryone(players, playerInfos.get(playerId).getsLongestTrailBonus(currentPlayerTrail));
            }
            playersScores.put(playerId, currentPlayerScore);
        }

        //Détermine le gagnant
        int maxScore = Collections.max(playersScores.values()); //Score le plus élevé
        List<PlayerId> playersWithMaxScore = playersScores.entrySet().stream().filter(map -> map.getValue() == maxScore).map(Map.Entry::getKey).collect(Collectors.toList()); //List de playerId qui ont le plus grand score
        if (playersWithMaxScore.size() == PlayerId.COUNT) { // S'il y autant de joueur que joueurs au plus gros score -> égalité
            List<String> names = playersWithMaxScore.stream().map(Enum::name).collect(Collectors.toList());
            tellEveryone(players, Info.draw(names, maxScore));
        } else { //Sinon il y a un gagnant
            PlayerId playerWinner = playersWithMaxScore.get(0);
            tellEveryone(players, playerInfos.get(playerWinner).won(maxScore, playersScores.get(playerWinner.next())));
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
