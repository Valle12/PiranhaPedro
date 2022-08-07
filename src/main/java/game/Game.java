package game;

import common.PlayerStats;

public class Game {
    private PlayerStats[] players = new PlayerStats[2];

    public Game() {
        boolean startingPlayer = Math.round(Math.random()) == 0 ? false : true;
        players[0] = new PlayerStats(startingPlayer);
        players[1] = new PlayerStats(!startingPlayer);
    }

    public PlayerStats[] getPlayers() {
        return players;
    }
}
