package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public class Frame extends JPanel implements MouseListener {
    private static final long serialVersionUID = 6434731205261840192L;
    private final int CARD_AMOUNT = 12;
    private Insets insets;
    private int[][] board; // 0 = empty, 1 = land, 2 = piranha, 3 = pedro
    private int[] playCards; // First Entry for first player, second entry for second player, third entry for
    // player with sombrero card
    private int[] stones;
    private int[] piranhas;
    private int[] wins;
    private boolean printPossible = false;
    private boolean firstPlayer, secondPlayer;
    private boolean currentPlayer, choosePiranha, currentCard; // false = first player's
    // turn, true = second
    // player's turn
    private boolean[] lowerCards, upperCards; // false = card not yed played, true = card played
    private String[] directions;

    public Frame() {
        this.insets = this.getInsets();
        board = new int[11][15];
        predefineBoard();
        lowerCards = new boolean[CARD_AMOUNT];
        upperCards = new boolean[CARD_AMOUNT];
        directions = new String[]{"Green", "Cyan", "Magenta", "Orange"};
        addMouseListener(this);
        firstPlayer = true;
        secondPlayer = true;
        playCards = new int[]{-1, -1, -1};
        stones = new int[]{4, 4};
        piranhas = new int[2];
        wins = new int[2];
        currentPlayer = (Math.random() > 0.5) ? true : false;
        currentCard = currentPlayer;

        // Last command
        printPossible = true;
        repaint();
    }

    private void predefineBoard() {
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
        board[5][5] = 1;

        // Pedro
        board[4][4] = 3;
    }

    public void choosePiranha() {
        choosePiranha = true;
    }

    public void changePedro(int pedroI, int pedroJ, int nextI, int nextJ) {
        board[pedroI][pedroJ] = 1;
        board[nextI][nextJ] = 3;
    }

    public boolean createLand(int pedroI, int pedroJ, int nextI, int nextJ) {
        if (stones[(currentCard ? 1 : 0)]-- > 0) {
            changePedro(pedroI, pedroJ, nextI, nextJ);
            return true;
        } else {
            choosePiranha();
            return false;
        }
    }

    private void gameOver(boolean firstPlayer) {
        wins[firstPlayer ? 1 : 0]++;
        resetGame();
    }

    public void setPlayCard(int index, int value) {
        playCards[index] = value;
    }

    public void changeCurrentCard() {
        currentCard = !currentCard;
    }

    public void changeCurrentPlayer() {
        currentPlayer = !currentPlayer;
        currentCard = currentPlayer;
    }

    public void repaintBoard() {
        repaint();
    }

    public void resetBoard() {
        playCards[0] = -1;
        playCards[1] = -1;
        playCards[2] = -1;
        stones[0] = getRemainingStones(true);
        stones[1] = getRemainingStones(false);
        resetLowerAndUpperCards();
        repaint();
    }

    private void resetLowerAndUpperCards() {
        for (int i = 0; i < 12; i++) {
            lowerCards[i] = false;
            upperCards[i] = false;
        }
    }

    public void resetGame() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = 0;
            }
        }
        predefineBoard();
        resetLowerAndUpperCards();
        playCards[0] = -1;
        playCards[1] = -1;
        playCards[2] = -1;
        stones[0] = 4;
        stones[1] = 4;
        piranhas[0] = 0;
        piranhas[1] = 0;
        currentPlayer = (Math.random() > 0.5) ? true : false;
        currentCard = currentPlayer;
        repaint();
    }

    private int getRemainingStones(boolean firstPlayer) {
        int numberOfStones = 0;
        int numberOfTwos = 0;
        for (int i = 0; i < 12; i++) {
            if (firstPlayer ? !lowerCards[i] : !upperCards[i]) {
                if ((i % 3) == 0) {
                    numberOfStones++;
                } else if ((i % 3) == 1) {
                    numberOfTwos++;
                    if (numberOfTwos == 2) {
                        numberOfTwos = 0;
                        numberOfStones++;
                    }
                }
            }
        }
        return numberOfStones;
    }

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

    public boolean getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean[] getLowerCards() {
        return lowerCards;
    }

    public boolean[] getUpperCards() {
        return upperCards;
    }

    public boolean getCurrentCard() {
        return currentCard;
    }

    @Override
    public void paintComponent(Graphics gOld) {
        if (printPossible) {
            super.paintComponent(gOld);
            Graphics2D g = (Graphics2D) gOld;
            g.setColor(getBackground());
            g.fillRect(insets.left, insets.top, 1000, 800);
            paintBoard(g);
            paintLowerCards(g);
            paintUpperCards(g);
            paintPlayCards(g);
            paintPedro(g);
            paintStonesAndPiranhasAndWins(g);
            paintChoosePiranhas(g);
        }
    }

    private void paintBoard(Graphics2D g) {
        int boardXOffset = 200 + insets.left;
        int boardYOffset = 180 + insets.top;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                switch (board[i][j]) {
                    case 0:
                        g.setColor(Color.BLACK);
                        g.drawRect(boardXOffset + 40 * j, boardYOffset + 40 * i, 40, 40);
                        break;
                    case 1:
                        g.setColor(Color.YELLOW);
                        g.fillRect(boardXOffset + 40 * j, boardYOffset + 40 * i, 40, 40);
                        break;
                    case 2:
                        g.setColor(Color.RED);
                        g.fillRect(boardXOffset + 40 * j, boardYOffset + 40 * i, 40, 40);
                        break;
                    case 3:
                        g.setColor(Color.BLUE);
                        g.fillRect(boardXOffset + 40 * j, boardYOffset + 40 * i, 40, 40);
                        break;
                }
            }
        }
        g.setColor(Color.GREEN);
        g.fillRect(boardXOffset, boardYOffset - 20, 15 * 40, 20);
        g.setColor(Color.CYAN);
        g.fillRect(boardXOffset + 15 * 40, boardYOffset, 20, 11 * 40);
        g.setColor(Color.MAGENTA);
        g.fillRect(boardXOffset, boardYOffset + 11 * 40, 15 * 40, 20);
        g.setColor(Color.ORANGE);
        g.fillRect(boardXOffset - 20, boardYOffset, 20, 11 * 40);
    }

    private void paintLowerCards(Graphics2D g) {
        int boardXOffset = 2 + insets.left;
        int boardYOffset = 670 + insets.top;
        for (int i = 0; i < CARD_AMOUNT; i++) {
            if (!lowerCards[i]) {
                switch (i / 3) {
                    case 0:
                        g.setColor(Color.GREEN);
                        break;
                    case 1:
                        g.setColor(Color.CYAN);
                        break;
                    case 2:
                        g.setColor(Color.MAGENTA);
                        break;
                    case 3:
                        g.setColor(Color.ORANGE);
                        break;
                }
                g.fillRect(boardXOffset + i * (80 + 3), boardYOffset, 80, 80);
                g.setColor(Color.BLACK);
                g.drawString(((i % 3) + 1) + " " + directions[i / 3], boardXOffset + i * (80 + 3) + 15,
                        boardYOffset + 43);
            }
        }
        Font oldFont = g.getFont();
        Font font = new Font("Arial", Font.PLAIN, 20);
        g.setFont(font);
        g.setColor(Color.BLACK);
        g.drawString("1. Player", (int) (getSize().getWidth() / 2) - 35, boardYOffset + 80 + 30);
        g.setFont(oldFont);
    }

    private void paintUpperCards(Graphics2D g) {
        int boardXOffset = 2 + insets.left;
        int boardYOffset = 50 + insets.top;
        for (int i = 0; i < CARD_AMOUNT; i++) {
            if (!upperCards[i]) {
                switch (i / 3) {
                    case 0:
                        g.setColor(Color.GREEN);
                        break;
                    case 1:
                        g.setColor(Color.CYAN);
                        break;
                    case 2:
                        g.setColor(Color.MAGENTA);
                        break;
                    case 3:
                        g.setColor(Color.ORANGE);
                        break;
                }
                g.fillRect(boardXOffset + i * (80 + 3), boardYOffset, 80, 80);
                g.setColor(Color.BLACK);
                g.drawString(((i % 3) + 1) + " " + directions[i / 3], boardXOffset + i * (80 + 3) + 15,
                        boardYOffset + 43);
            }
        }
        Font oldFont = g.getFont();
        Font font = new Font("Arial", Font.PLAIN, 20);
        g.setFont(font);
        g.setColor(Color.BLACK);
        g.drawString("2. Player", (int) (getSize().getWidth() / 2) - 35, boardYOffset - 18);
        g.setFont(oldFont);
    }

    private void paintPlayCards(Graphics2D g) {
        int boardXOffset = 30 + insets.left;
        int boardYMiddle = (getHeight() / 2) + insets.top;
        Font oldFont = g.getFont();
        Font font = new Font("Arial", Font.PLAIN, 15);
        g.setFont(font);
        for (int i = 0; i < playCards.length; i++) {
            switch (playCards[i] / 3) {
                case 0:
                    g.setColor(Color.GREEN);
                    break;
                case 1:
                    g.setColor(Color.CYAN);
                    break;
                case 2:
                    g.setColor(Color.MAGENTA);
                    break;
                case 3:
                    g.setColor(Color.ORANGE);
                    break;
            }
            if (i == 0 && (playCards[0] != -1)) {
                g.fillRect(boardXOffset, boardYMiddle + 5, 120, 120);
                g.setColor(Color.BLACK);
                g.drawString(((playCards[0] % 3) + 1) + " " + directions[playCards[0] / 3], boardXOffset + 30,
                        boardYMiddle + 70);
            } else if (i == 1 && (playCards[1] != -1)) {
                g.fillRect(boardXOffset, boardYMiddle - 125, 120, 120);
                g.setColor(Color.BLACK);
                g.drawString(((playCards[1] % 3) + 1) + " " + directions[playCards[1] / 3], boardXOffset + 30,
                        boardYMiddle - 60);
            } else if (i == 2 && (currentPlayer ? (playCards[1] == -1) : (playCards[0] == -1)) && (playCards[2] != -1)) {
                g.fillRect(boardXOffset, boardYMiddle + (currentPlayer ? -125 : 5), 120, 120);
                g.setColor(Color.BLACK);
                g.drawString(((playCards[2] % 3) + 1) + " " + directions[playCards[2] / 3], boardXOffset + 30,
                        boardYMiddle + (currentPlayer ? -60 : 70));
            }
        }
        g.setFont(oldFont);
    }

    private void paintPedro(Graphics2D g) {
        int boardXOffset = 850 + insets.left;
        int boardYMiddle = (getHeight() / 2) + insets.top;
        g.setColor(Color.BLUE);
        if (currentPlayer) {
            g.fillRect(boardXOffset, boardYMiddle - 125, 120, 120);
        } else {
            g.fillRect(boardXOffset, boardYMiddle + 5, 120, 120);
        }
    }

    private void paintStonesAndPiranhasAndWins(Graphics2D g) {
        int boardXOffset = 30 + insets.left;
        int boardYMiddle = (getHeight() / 2) + insets.top;
        g.setColor(Color.BLACK);
        g.drawString("Number of Stones: " + stones[0], boardXOffset, boardYMiddle + 145);
        g.drawString("Number of Stones: " + stones[1], boardXOffset, boardYMiddle - 170);
        g.drawString("Number of Piranhas: " + piranhas[0], boardXOffset, boardYMiddle + 160);
        g.drawString("Number of Piranhas: " + piranhas[1], boardXOffset, boardYMiddle - 155);
        g.drawString("Wins Player 1: " + wins[0], boardXOffset, boardYMiddle + 175);
        g.drawString("Wins Player 2: " + wins[1], boardXOffset, boardYMiddle - 140);
    }

    private void paintChoosePiranhas(Graphics2D g) {
        if (choosePiranha) {
            Font oldFont = g.getFont();
            Font font = new Font("Arial", Font.PLAIN, 20);
            g.setFont(font);
            g.setColor(Color.RED);
            g.drawString("Choose Piranha!", 2 + insets.left, currentCard ? 32 + insets.top : 780);
            g.setFont(oldFont);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        int cardXOffset = 2 + insets.left;
        int cardYOffsetTop = 50 + insets.top;
        int cardYOffsetBottom = 670 + insets.top;
        int boardXOffset = 200 + insets.left;
        int boardYOffset = 180 + insets.top;
        for (int i = 0; i < CARD_AMOUNT; i++) {
            if (x >= (cardXOffset + i * (80 + 3)) && x <= (cardXOffset + i * (80 + 3) + 80) && y >= cardYOffsetTop
                    && y <= (cardYOffsetTop + 80)) {
                if (!upperCards[i] && secondPlayer
                        && ((playCards[1] == -1) || (currentPlayer && (playCards[2] == -1)))) {
                    upperCards[i] = !upperCards[i];
                    playCards[(playCards[1] == -1) ? 1 : 2] = i;
                    repaint();
                }
            } else if (x >= (cardXOffset + i * (80 + 3)) && x <= (cardXOffset + i * (80 + 3) + 80)
                    && y >= cardYOffsetBottom && y <= (cardYOffsetBottom + 80)) {
                if (!lowerCards[i] && firstPlayer
                        && ((playCards[0] == -1) || (!currentPlayer && (playCards[2] == -1)))) {
                    lowerCards[i] = !lowerCards[i];
                    playCards[(playCards[0] == -1) ? 0 : 2] = i;
                    repaint();
                }
            }
        }
        if (choosePiranha) {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    if (board[i][j] == 2 && x >= (boardXOffset + j * 40) && x < (boardXOffset + (j + 1) * 40)
                            && y >= (boardYOffset + i * 40) && y < (boardYOffset + (i + 1) * 40)) {
                        board[i][j] = 0;
                        piranhas[(currentCard ? 1 : 0)]++;
                        if (piranhas[0] == 3) {
                            gameOver(true);
                        } else if (piranhas[1] == 3) {
                            gameOver(false);
                        } else {

                        }
                        choosePiranha = false;
                        changeCurrentPlayer();
                        repaint();
                    } else {
                        continue;
                    }
                }
            }
        }
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {

    }

    @Override
    public void mouseExited(MouseEvent arg0) {

    }

    @Override
    public void mousePressed(MouseEvent arg0) {

    }

    @Override
    public void mouseReleased(MouseEvent arg0) {

    }
}
