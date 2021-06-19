package game;

import java.io.Serializable;

public class Board implements Serializable {
  private int[][] board;
  private int[] playCards, stones, piranhas, wins;
  private boolean currentPlayer, currentCard, choosePiranha;
  private boolean[] lowerCards, upperCards;

  public Board(
      int[][] board,
      int[] playCards,
      int[] stones,
      int[] piranhas,
      int[] wins,
      boolean currentPlayer,
      boolean currentCard,
      boolean choosePiranha,
      boolean[] lowerCards,
      boolean[] upperCards) {
    this.board = new int[board.length][board[0].length];
    copy2DIntArray(board, this.board);
    this.playCards = new int[playCards.length];
    this.stones = new int[stones.length];
    this.piranhas = new int[piranhas.length];
    this.wins = new int[wins.length];
    copy1DIntArray(playCards, this.playCards);
    copy1DIntArray(stones, this.stones);
    copy1DIntArray(piranhas, this.piranhas);
    copy1DIntArray(wins, this.wins);
    this.currentPlayer = currentPlayer;
    this.currentCard = currentCard;
    this.choosePiranha = choosePiranha;
    this.lowerCards = new boolean[lowerCards.length];
    this.upperCards = new boolean[upperCards.length];
    copy1DBooleanArray(lowerCards, this.lowerCards);
    copy1DBooleanArray(upperCards, this.upperCards);
  }

  public Board(Board gameBoard) {
    this.board = new int[gameBoard.getBoard().length][gameBoard.getBoard()[0].length];
    copy2DIntArray(gameBoard.getBoard(), this.board);
    this.playCards = new int[gameBoard.getPlayCards().length];
    copy1DIntArray(gameBoard.getPlayCards(), this.playCards);
    this.stones = new int[gameBoard.getStones().length];
    copy1DIntArray(gameBoard.getStones(), this.stones);
    this.piranhas = new int[gameBoard.getPiranhas().length];
    copy1DIntArray(gameBoard.getPiranhas(), this.piranhas);
    this.wins = new int[gameBoard.getWins().length];
    copy1DIntArray(gameBoard.getWins(), this.wins);
    this.currentPlayer = gameBoard.getCurrentPlayer();
    this.currentCard = gameBoard.getCurrentCard();
    this.choosePiranha = gameBoard.getChoosePiranha();
    this.lowerCards = new boolean[gameBoard.getLowerCards().length];
    copy1DBooleanArray(gameBoard.getLowerCards(), this.lowerCards);
    this.upperCards = new boolean[gameBoard.getUpperCards().length];
    copy1DBooleanArray(gameBoard.getUpperCards(), this.upperCards);
  }

  private void copy2DIntArray(int[][] source, int[][] dest) {
    for (int i = 0; i < dest.length; i++) {
      for (int j = 0; j < dest[i].length; j++) {
        dest[i][j] = source[i][j];
      }
    }
  }

  private void copy1DIntArray(int[] source, int[] dest) {
    for (int i = 0; i < dest.length; i++) {
      dest[i] = source[i];
    }
  }

  private void copy1DBooleanArray(boolean[] source, boolean[] dest) {
    for (int i = 0; i < dest.length; i++) {
      dest[i] = source[i];
    }
  }

  // GETTER
  public int[][] getBoard() {
    return board;
  }

  public int[] getPlayCards() {
    return playCards;
  }

  public int[] getStones() {
    return stones;
  }

  public int[] getPiranhas() {
    return piranhas;
  }

  public int[] getWins() {
    return wins;
  }

  public boolean getCurrentPlayer() {
    return currentPlayer;
  }

  public boolean getCurrentCard() {
    return currentCard;
  }

  public boolean getChoosePiranha() {
    return choosePiranha;
  }

  public boolean[] getLowerCards() {
    return lowerCards;
  }

  public boolean[] getUpperCards() {
    return upperCards;
  }

  // SETTER
  public void resetLowerAndUpperCards() {
    for (int i = 0; i < 12; i++) {
      lowerCards[i] = false;
      upperCards[i] = false;
    }
  }

  public void nullifyBoard() {
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        board[i][j] = 0;
      }
    }
  }

  public void predefineBoard() {
    // Piranha
    board[0][0] = 2;
    board[0][13] = 2;
    board[2][11] = 2;
    board[7][12] = 2;
    board[8][2] = 2;
    board[8][13] = 2;
    board[10][8] = 2;

    // Land
    board[2][4] = 1;
    board[3][4] = 1;
    board[3][5] = 1;
    board[4][2] = 1;
    board[4][3] = 1;
    board[4][4] = 1;
    board[4][5] = 1;
    board[5][4] = 1;

    // Pedro
    board[4][4] = 3;
  }

  public void setBoard(int i, int j, int value) {
    board[i][j] = value;
  }

  public void setPlayCard(int i, int value) {
    playCards[i] = value;
  }

  public void setStones(int i, int value) {
    stones[i] = value;
  }

  public void setPiranhas(int i, int value) {
    piranhas[i] = value;
  }

  public void setWins(int i, int value) {
    wins[i] = value;
  }

  public void setCurrentPlayer(boolean value) {
    currentPlayer = value;
  }

  public void setCurrentCard(boolean value) {
    currentCard = value;
  }

  public void setChoosePiranha(boolean value) {
    choosePiranha = value;
  }

  public void setLowerCards(int i, boolean value) {
    lowerCards[i] = value;
  }

  public void setUpperCards(int i, boolean value) {
    upperCards[i] = value;
  }
}
