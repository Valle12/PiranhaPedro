package game;

public class Main {
  private Game game;

  public Main() {
    game = new Game(1000, 800);
  }

  public Main(String arg) {
    game = new Game(1000, 800);
    if (arg.length() > 1) {
      game.setPort(Integer.parseInt(arg));
    }
    game.createSpecialGame(Integer.parseInt(arg));
  }

  public static void main(String[] args) {
    if (args.length == 0) {
      new Main();
    } else {
      new Main(args[0]);
    }
  }
}
