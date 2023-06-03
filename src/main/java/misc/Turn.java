package misc;

import lombok.Getter;

@Getter
public enum Turn {
  UP1(Direction.UP, 1),
  UP2(Direction.UP, 2),
  UP3(Direction.UP, 3),
  RIGHT1(Direction.RIGHT, 1),
  RIGHT2(Direction.RIGHT, 2),
  RIGHT3(Direction.RIGHT, 3),
  DOWN1(Direction.DOWN, 1),
  DOWN2(Direction.DOWN, 2),
  DOWN3(Direction.DOWN, 3),
  LEFT1(Direction.LEFT, 1),
  LEFT2(Direction.LEFT, 2),
  LEFT3(Direction.LEFT, 3);

  private final Direction direction;
  private final int length;

  Turn(Direction direction, int length) {
    this.direction = direction;
    this.length = length;
  }

  public int getButtonNumberFromTurn() {
    return direction.ordinal() * 3 + length - 1;
  }
}
