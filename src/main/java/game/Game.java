package game;

import AI.GeneralAI;
import AI.RandomAI;
import common.*;
import main.Main;

public class Game {
    private PlayerStats[] players = new PlayerStats[2];
    private boolean startingPlayer, isFinished = false;
    private boolean[] playerZeroCards;
    private Turn[] turns = new Turn[3];
    private AI ai = AI.RANDOM;
    private GeneralAI aiPlayer;
    private GameCell cellUnderPedro = GameCell.ISLAND;

    public Game() {
        startingPlayer = Math.round(Math.random()) == 0;
        players[0] = new PlayerStats(startingPlayer);
        players[1] = new PlayerStats(!startingPlayer);
        playerZeroCards = players[0].getCardsPlayed();
        // TODO determine which ai you want to create later
        aiPlayer = new RandomAI(startingPlayer, players[1].getCardsPlayed(), turns);
    }

    /**
     * Adds Turns to turns array, when you click on the buttons in the GUI.
     * Also acts as kind of the main game loop
     *
     * @param turn to be added to the array of cards played in this round
     */
    public void addTurn(Turn turn) {
        // startingPlayer true means the player has to select two cards, because he has the sombrero card
        if (startingPlayer) {
            // no cards selected yet, two remaining
            if (turns[0] == null) {
                turns[0] = turn;
                playerZeroCards[turn.turnToInt()] = true;
                Main.gameController.updateClickableButtons();
                return;
                // one card selected, one remaining
            } else {
                turns[2] = turn;
                playerZeroCards[turn.turnToInt()] = true;
            }
        } else {
            turns[0] = turn;
            playerZeroCards[turn.turnToInt()] = true;
        }
        Main.gameController.changeDisabledStateAllButtons(true);
        aiPlayer.calculateMove(); // let AI choose turn/turns itself
        playOutTurns(); // play out all three cards if possible, if not do something
    }

    /**
     * Play out all three moves if possible
     */
    private boolean playOutTurns() {
        if (startingPlayer) {
            var success = playOutTurn(turns[0], players[0]);
            if (!success) {
                return false;
            }
            success = playOutTurn(turns[1], players[1]);
            if (!success) {
                return false;
            }
            return playOutTurn(turns[2], players[0]);
        } else {
            var success = playOutTurn(turns[1], players[1]);
            if (!success) {
                return false;
            }
            success = playOutTurn(turns[0], players[0]);
            if (!success) {
                return false;
            }
            return playOutTurn(turns[2], players[1]);
        }
    }

    /**
     * Play out one specific turn
     *
     * @param turn   to be played out
     * @param player whose turn it is
     * @return true if everything was successful, false otherwise
     */
    private boolean playOutTurn(Turn turn, PlayerStats player) {
        var field = Main.gameController.getGameField().getField();
        for (int i = 0; i < turn.getAmount(); i++) {
            var posPedro = calculatePositionOfPedro(field);
            Point futurePosPedro;
            // get possible future position of pedro, can also be in a "round end" state
            switch (turn.getDirection()) {
                case UP:
                    futurePosPedro = new Point(posPedro.getX(), posPedro.getY() - 1);
                    break;
                case RIGHT:
                    futurePosPedro = new Point(posPedro.getX() + 1, posPedro.getY());
                    break;
                case DOWN:
                    futurePosPedro = new Point(posPedro.getX(), posPedro.getY() + 1);
                    break;
                default:
                    futurePosPedro = new Point(posPedro.getX() - 1, posPedro.getY());
                    break;
            }
            // pedro is about to walk off the board
            if (futurePosPedro.getY() < 0 || futurePosPedro.getX() > 14 || futurePosPedro.getY() > 10 || futurePosPedro.getX() < 0) {
                player.increaseNumberOfPiranhas();
                return false;
            }
            // pedro is about to step onto a piranha
            if (field[futurePosPedro.getY()][futurePosPedro.getX()] == GameCell.PIRANHA) {
                player.increaseNumberOfPiranhas();
                return false;
            }
            // pedro is about to fall into the water
            if (field[futurePosPedro.getY()][futurePosPedro.getX()] == GameCell.WATER && player.getNumberOfStones() - 1 < 0) {
                player.increaseNumberOfPiranhas();
                return false;
            }
            // pedro is about to place a stone in the water and walk onto this
            if (field[futurePosPedro.getY()][futurePosPedro.getX()] == GameCell.WATER && player.getNumberOfStones() - 1 >= 0) {
                field[posPedro.getY()][posPedro.getX()] = cellUnderPedro;
                cellUnderPedro = GameCell.STONE;
                field[futurePosPedro.getY()][futurePosPedro.getX()] = GameCell.PEDRO;
                player.decreaseNumberOfStones();
                Main.gameController.paintField(field);
                continue;
            }
            // pedro is about to walk on already existing land
            if (field[futurePosPedro.getY()][futurePosPedro.getX()] == GameCell.ISLAND || field[futurePosPedro.getY()][futurePosPedro.getX()] == GameCell.STONE) {
                field[posPedro.getY()][posPedro.getX()] = cellUnderPedro;
                cellUnderPedro = field[futurePosPedro.getY()][futurePosPedro.getX()];
                field[futurePosPedro.getY()][futurePosPedro.getX()] = GameCell.PEDRO;
                Main.gameController.paintField(field);
            }
        }
        return true;
    }

    private Point calculatePositionOfPedro(GameCell[][] field) {
        for (int y = 0; y < field.length; y++) {
            for (int x = 0; x < field[y].length; x++) {
                if (field[y][x] == GameCell.PEDRO) {
                    return new Point(x, y);
                }
            }
        }
        return null;
    }

    public PlayerStats[] getPlayers() {
        return players;
    }
}
