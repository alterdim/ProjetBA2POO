package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.gui.Info;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Fichier créé à 14:49 le 22/03/2021
 *
 * @author Louis Gerard (296782)
 * @author Célien Muller (310777)
 */
public final class Game {

    public static void play(Map<PlayerId, Player> players, Map<PlayerId, String> playerNames,
                            SortedBag<Ticket> tickets, Random rng) {
        Player player;
        //Initialiser le jeu
        GameState gameState = GameState.initial(tickets, rng);
        Map<PlayerId, Info> playerInfos = new HashMap<>();
        Deck<Ticket> ticketDeck = Deck.of(tickets, rng);
        Map<PlayerId, PlayerState> playerStates = new HashMap<>();
        for (PlayerId p : players.keySet()) {
            player = players.get(p);
            playerInfos.put(p, new Info(p.name()));
            player.initPlayers(p, playerNames);
            playerStates.put(p, gameState.playerState(p));
        }
        //Annoncer le premier joueur
        PlayerId firstPlayer = gameState.currentPlayerId();
        tellEveryone(players, playerInfos.get(firstPlayer).willPlayFirst());

        //Générer les premiers tickets
        for (Player p : players.values()) {
            p.setInitialTicketChoice(ticketDeck.topCards(Constants.INITIAL_TICKETS_COUNT));
            ticketDeck = ticketDeck.withoutTopCards(Constants.INITIAL_TICKETS_COUNT);
            p.chooseInitialTickets();
        }

        //Annoncer le nombre de tickets gardés
        for (PlayerId p : players.keySet()) {
            tellEveryone(players, playerInfos.get(p)
                    .keptTickets(playerStates.get(p)
                            .ticketCount()));
        }





    }


    private static void tellEveryone(Map<PlayerId, Player> players, String info) {
        for (Player p : players.values()) {
            p.receiveInfo(info);
        }
    }
}
