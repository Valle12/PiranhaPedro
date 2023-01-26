package common;

public class PlayerStats {
    private int numberOfStones = 4;
    private int numberOfPiranhas = 0;
    private int numberOfWins = 0;
    private boolean[] cardsPlayed = new boolean[12]; // Player starts out with no cards played
    private boolean startingPlayer = false; // Will be determined at the start of each game

    public PlayerStats(boolean startingPlayer) {
        this.startingPlayer = startingPlayer;
    }

    public int getNumberOfStones() {
        return numberOfStones;
    }

    public int getNumberOfWins() {
        return numberOfWins;
    }

    public boolean[] getCardsPlayed() {
        return cardsPlayed;
    }

    public boolean isStartingPlayer() {
        return startingPlayer;
    }

    public int getNumberOfPiranhas() {
        return numberOfPiranhas;
    }

    public void increaseNumberOfPiranhas() {
        numberOfPiranhas++;
    }

    public void decreaseNumberOfStones() {
        numberOfStones--;
    }
}
