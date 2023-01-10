package AI;

import common.Turn;

public abstract class GeneralAI {
    protected boolean[] playerOneCards;
    protected boolean startingPlayer;
    protected Turn[] turns;

    public GeneralAI(boolean startingPlayer, boolean[] playerOneCards, Turn[] turns) {
        this.startingPlayer = startingPlayer;
        this.playerOneCards = playerOneCards;
        this.turns = turns;
    }

    public abstract void calculateMove();
}
