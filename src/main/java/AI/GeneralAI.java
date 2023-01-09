package AI;

public abstract class GeneralAI {
    private boolean[] cardsPlayed = new boolean[12];
    private boolean startingPlayer;

    public GeneralAI(boolean startingPlayer) {
        this.startingPlayer = startingPlayer;
    }

    public void calculateMove() {

    }
}
