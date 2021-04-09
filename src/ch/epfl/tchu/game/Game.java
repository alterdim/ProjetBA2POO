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
//        Preconditions.checkArgument(players.values().size()==2);
        Preconditions.checkArgument(playerNames.values().size()==PlayerId.COUNT);
//        Preconditions.checkArgument(playerNames.values().size()==2);
        //Initialisation de variables utiles
        boolean finished = false;
        int addCardsCount;
        int drawSlot;
        Player player;
        Player currentPlayer;
        Player.TurnKind turnKind;
        Card drawnCard;
        SortedBag<Ticket> drawnTickets;
        SortedBag<Ticket> keptTickets;
        SortedBag<Card> claimCards;
        SortedBag<Card> drawnCards;
        SortedBag<Card> chosenCards;
        List<SortedBag<Card>> additionalCards;

        Route claimedRoute;
        //Initialiser le jeu
        GameState gameState = GameState.initial(tickets, rng);

        Map<PlayerId, Info> playerInfos = new HashMap<>();
        Deck<Ticket> ticketDeck = Deck.of(tickets, rng);
//        Map<PlayerId, PlayerState> playerStates = new HashMap<>();
        for (PlayerId p : players.keySet()) {
            player = players.get(p);
            playerInfos.put(p, new Info(p.name()));
            player.initPlayers(p, playerNames);
//            playerStates.put(p, gameState.playerState(p));
        }
        //Annoncer le premier joueur
        PlayerId firstPlayer = gameState.currentPlayerId();
        tellEveryone(players, playerInfos.get(firstPlayer).willPlayFirst());

        //Générer les premiers tickets
        for (PlayerId p : players.keySet()) {
            player = players.get(p);
            drawnTickets = ticketDeck.topCards(Constants.INITIAL_TICKETS_COUNT);
            ticketDeck = ticketDeck.withoutTopCards(Constants.INITIAL_TICKETS_COUNT);
            player.setInitialTicketChoice(drawnTickets);

            gameState = gameState.withInitiallyChosenTickets(p, player.chooseInitialTickets());
            updateEveryone(players, gameState);
        }

        //Annoncer le nombre de tickets gardés
        for (PlayerId p : players.keySet()) {
            tellEveryone(players,
//                    playerInfos.get(p).keptTickets(playerStates.get(p).ticketCount()));
                    playerInfos.get(p).keptTickets(gameState.playerState(p).ticketCount()));
        }
        //Boucle de jeu
        while(!finished) {
            updateEveryone(players, gameState);
            currentPlayer = players.get(gameState.currentPlayerId());
            turnKind = currentPlayer.nextTurn();
            tellEveryone(players, playerInfos.get(gameState.currentPlayerId()).canPlay());
            System.out.println("current player Cards  : "+gameState.currentPlayerState().cards());
            if (gameState.lastTurnBegins()) {
                tellEveryone(players, playerInfos.get(gameState.currentPlayerId()).lastTurnBegins(gameState.currentPlayerState().carCount()));
//                tellEveryone(players, playerInfos.get(gameState.currentPlayerId()).lastTurnBegins(playerStates.get(gameState.currentPlayerId()).carCount()));
            }
            switch (turnKind) {
                case DRAW_TICKETS:
                    drawnTickets = ticketDeck.topCards(Constants.IN_GAME_TICKETS_COUNT);
                    tellEveryone(players, playerInfos.get(gameState.currentPlayerId()).drewTickets(Constants.IN_GAME_TICKETS_COUNT));
                    keptTickets = currentPlayer.chooseTickets(drawnTickets);
                    gameState = gameState.withChosenAdditionalTickets(drawnTickets, keptTickets);
                    tellEveryone(players, playerInfos.get(gameState.currentPlayerId()).keptTickets(keptTickets.size()));
                    break;
                case DRAW_CARDS:
                    for (int i = 0; i<2; i++) {
                        updateEveryone(players, gameState);
                        gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
                        drawSlot = currentPlayer.drawSlot();
                        if (drawSlot == -1) {
                            tellEveryone(players, playerInfos.get(gameState.currentPlayerId()).drewBlindCard());
                            gameState = gameState.withBlindlyDrawnCard();
                        }
                        else {
                            gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
                            drawnCard = gameState.cardState().faceUpCard(drawSlot);
                            tellEveryone(players, playerInfos.get(gameState.currentPlayerId()).drewVisibleCard(drawnCard));
                            gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
                            gameState = gameState.withDrawnFaceUpCard(drawSlot);
                        }
                    }
                    break;
                case CLAIM_ROUTE:
                    claimedRoute = currentPlayer.claimedRoute();
                    claimCards = currentPlayer.initialClaimCards();

                    if (claimedRoute.level().equals(Route.Level.UNDERGROUND)) {
                        tellEveryone(players, playerInfos.get(gameState.currentPlayerId()).attemptsTunnelClaim(claimedRoute, claimCards));
                        SortedBag.Builder<Card> builder = new SortedBag.Builder<>();
                        for (int i = 0; i<3; i++) {
                            gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
                            builder.add(1, gameState.topCard());
                            gameState = gameState.withoutTopCard();

                        }
                        drawnCards = builder.build();
                        addCardsCount = claimedRoute.additionalClaimCardsCount(claimCards, drawnCards);
                        gameState = gameState.withMoreDiscardedCards(drawnCards);

                        tellEveryone(players, playerInfos.get(gameState.currentPlayerId()).drewAdditionalCards(drawnCards, addCardsCount));
                        if (addCardsCount > 0) {
                            additionalCards = gameState.currentPlayerState().possibleAdditionalCards(addCardsCount, claimCards, drawnCards);
                            chosenCards = currentPlayer.chooseAdditionalCards(additionalCards);
                            if (chosenCards.isEmpty()) {
                                tellEveryone(players, playerInfos.get(gameState.currentPlayerId()).didNotClaimRoute(claimedRoute));
                                break;
                            }
                            //claims avec carte + cartes additionnels
                            else {
                                gameState = gameState.withClaimedRoute(claimedRoute, chosenCards.union(claimCards));
                                tellEveryone(players, playerInfos.get(gameState.currentPlayerId()).claimedRoute(claimedRoute, claimCards));
                            }
                        }
                        //claims si pas de cartes additinnells
                        else {
                            gameState = gameState.withClaimedRoute(claimedRoute, claimCards);
                            tellEveryone(players, playerInfos.get(gameState.currentPlayerId()).claimedRoute(claimedRoute, claimCards));
                        }
                    }
                    //claim route surface
                    else {
                        gameState = gameState.withClaimedRoute(claimedRoute, claimCards);
                        tellEveryone(players, playerInfos.get(gameState.currentPlayerId()).claimedRoute(claimedRoute, claimCards));
                    }
                    break;
            }
            System.out.println("Decksize  : "+gameState.cardState().deckSize());
            System.out.println("DiscardSize  : "+gameState.cardState().discardsSize());
            System.out.println("current player Cards  : "+gameState.currentPlayerState().cards());
            if (gameState.lastPlayer() != null) {
                finished = true;
            }
            gameState = gameState.forNextTurn();

        }
//        Trail longestTrail1 = Trail.longest(playerStates.get(PlayerId.PLAYER_1).routes());
        Trail longestTrail1 = Trail.longest(gameState.playerState(PlayerId.PLAYER_1).routes());
//        Trail longestTrail2 = Trail.longest(playerStates.get(PlayerId.PLAYER_2).routes());
        Trail longestTrail2 = Trail.longest(gameState.playerState(PlayerId.PLAYER_2).routes());
        int score1 = gameState.playerState(PlayerId.PLAYER_1).finalPoints();
//        int score1 = playerStates.get(PlayerId.PLAYER_1).finalPoints();
        int score2 = gameState.playerState(PlayerId.PLAYER_2).finalPoints();
//        int score2 = playerStates.get(PlayerId.PLAYER_2).finalPoints();
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
