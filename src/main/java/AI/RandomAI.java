package AI;

import common.Turn;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomAI extends GeneralAI {

    public RandomAI(boolean startingPlayer, boolean[] playerOneCards, Turn[] turns) {
        super(startingPlayer, playerOneCards, turns);
    }

    @Override
    public void calculateMove() {
        var indexes = getIndexesOfUnplayedCards();
        var random = new Random();
        int index;
        if (startingPlayer) {
            index = random.nextInt(indexes.size());
            turns[1] = Turn.intToTurn(indexes.get(index));
            playerOneCards[index] = true;
        } else {
            index = random.nextInt(indexes.size());
            turns[1] = Turn.intToTurn(indexes.get(index));
            playerOneCards[index] = true;
            indexes = getIndexesOfUnplayedCards();
            index = random.nextInt(indexes.size());
            turns[2] = Turn.intToTurn(indexes.get(index));
            playerOneCards[index] = true;
        }
    }

    private List<Integer> getIndexesOfUnplayedCards() {
        var indexes = new ArrayList<Integer>();
        for (int i = 0; i < playerOneCards.length; i++) {
            if (!playerOneCards[i]) {
                indexes.add(i);
            }
        }
        return indexes;
    }
}
