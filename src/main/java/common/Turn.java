package common;

public class Turn {
    private Direction direction;
    private int amount;

    public Turn(Direction direction, int amount) {
        this.direction = direction;
        this.amount = amount;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getAmount() {
        return amount;
    }

    // Calculates the number in the array from the turn parameters
    public int turnToInt() {
        return direction.ordinal() * 3 + amount - 1;
    }

    public static Turn intToTurn(int number) {
        return new Turn(Direction.values()[number / 3], number % 3 + 1);
    }

    // Prints out Turn[]
    public static void print(Turn[] turns) {
        for (Turn turn : turns) {
            if (turn != null) {
                System.out.println("Direction: " + turn.direction + ", Amount: " + turn.amount);
            } else {
                System.out.println("NULL");
            }
        }
    }
}
