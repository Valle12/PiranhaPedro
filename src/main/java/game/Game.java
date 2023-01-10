package game;

import AI.GeneralAI;
import AI.RandomAI;
import common.AI;
import common.PlayerStats;
import common.Turn;
import main.Main;

public class Game {
    private PlayerStats[] players = new PlayerStats[2];
    private boolean startingPlayer, isFinished = false;
    private boolean[] playerZeroCards;
    private Turn[] turns = new Turn[3];
    private AI ai = AI.RANDOM;
    private GeneralAI aiPlayer;

    public Game() {
        startingPlayer = Math.round(Math.random()) == 0;
        players[0] = new PlayerStats(startingPlayer);
        players[1] = new PlayerStats(!startingPlayer);
        playerZeroCards = players[0].getCardsPlayed();
        // TODO determine which ai you want to create later
        aiPlayer = new RandomAI(startingPlayer, players[1].getCardsPlayed(), turns);
    }

    public void addTurn(Turn turn) {
        if (startingPlayer) {
            if (turns[0] == null) {
                turns[0] = turn;
                playerZeroCards[turn.turnToInt()] = true;
                Main.gameController.updateClickableButtons();
            } else {
                turns[2] = turn;
                playerZeroCards[turn.turnToInt()] = true;
                Main.gameController.changeDisabledStateAllButtons(true);
                aiPlayer.calculateMove();
            }
        } else {
            turns[0] = turn;
            playerZeroCards[turn.turnToInt()] = true;
            Main.gameController.changeDisabledStateAllButtons(true);
            aiPlayer.calculateMove();
        }
    }

    public PlayerStats[] getPlayers() {
        return players;
    }
}
