package game;

import java.awt.Insets;

import javax.swing.JFrame;

public class Game extends Thread{
	private int width, height, lastSavePosX = 0, lastSavePosY = 0;
	private boolean running = true;
	private JFrame frame;
	private Frame fp;
	private Insets insets;

	public Game(int width, int height){
		this.width = width;
		this.height = height;
		this.frame = new JFrame("Piranha Pedro");
		this.frame.setSize(this.width, this.height);
		this.frame.setLocationRelativeTo(null);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setResizable(false);
		this.frame.setVisible(true);
		this.insets = this.frame.getInsets();
		this.frame.setVisible(false);
		this.width += this.insets.left + this.insets.right;
		this.height += this.insets.top + this.insets.bottom;
		this.frame.setSize(this.width, this.height);
		fp = new Frame();
		this.frame.add(fp);
		this.frame.setVisible(true);
		this.start();
	}

	private boolean playTurn() {
		int pedroI = -1;
		int pedroJ = -1;
		int nextI = -1, nextJ = -1, walkLength;
		boolean forSuccessful = true;
		int playCards[] = fp.getPlayCards();
		int index = 1;
		for (int i = 0; i < 3; i++) {
			int board[][] = fp.getBoard();
			for (int j = 0; j < board.length; j++) {
				for (int k = 0; k < board[i].length; k++) {
					if (board[j][k] == 3) {
						pedroI = j;
						pedroJ = k;
						break;
					}
				}
			}
			lastSavePosX = pedroJ;
			lastSavePosY = pedroI;
			walkLength = playCards[fp.getCurrentPlayer() ? index : i] % 3;
			switch (playCards[fp.getCurrentPlayer() ? index : i] / 3) {
			case 0:
				nextI = pedroI - walkLength - 1;
				forSuccessful = forDifferent(board, false, true, nextI - pedroI, pedroI, pedroJ);
				break;
			case 1:
				nextJ = pedroJ + walkLength + 1;
				forSuccessful = forDifferent(board, true, false, nextJ - pedroJ, pedroI, pedroJ);
				break;
			case 2:
				nextI = pedroI + walkLength + 1;
				forSuccessful = forDifferent(board, false, true, nextI - pedroI, pedroI, pedroJ);
				break;
			case 3:
				nextJ = pedroJ - walkLength - 1;
				forSuccessful = forDifferent(board, true, false, nextJ - pedroJ, pedroI, pedroJ);
				break;
			}
			if (forSuccessful) {
				fp.changeCurrentCard();
				fp.setPlayCard(fp.getCurrentPlayer() ? index : i, -1);
				index = (index + 2) % 3;
				lastSavePosX = (nextJ != -1) ? nextJ : pedroJ;
				lastSavePosY = (nextI != -1) ? nextI : pedroI;
			} else {
				return false;
			}
		}
		return true;
	}

	private boolean forDifferent(int[][] board, boolean changeX, boolean changeY, int walkLength, int pedroI, int pedroJ) {
		int nextI = pedroI;
		int nextJ = pedroJ;
		for (int i = 0; i < Math.abs(walkLength); i++) {
			if (changeX) {
				if ((walkLength >= 0) ? (nextJ = pedroJ + 1) < 15 : (nextJ = pedroJ - 1) >= 0) {
					if (board[nextI][nextJ] == 1) {
						fp.changePedro(pedroI, pedroJ, nextI, nextJ);
						pedroJ = nextJ;
						nextJ = pedroJ;
					} else if (board[nextI][nextJ] == 2) {
						fp.choosePiranha();
						return false;
					} else if (board[nextI][nextJ] == 0) {
						if (fp.createLand(pedroI, pedroJ, nextI, nextJ)) {
							pedroJ = nextJ;
							nextJ = pedroJ;
						} else {
							return false;
						}
					}
				} else {
					fp.choosePiranha();
					return false;
				}
			} else if (changeY) {
				if ((walkLength >= 0) ? (nextI = pedroI + 1) < 11 : (nextI = pedroI - 1) >= 0) {
					if (board[nextI][nextJ] == 1) {
						fp.changePedro(pedroI, pedroJ, nextI, nextJ);
						pedroI = nextI;
						nextI = pedroI;
					} else if (board[nextI][nextJ] == 2) {
						fp.choosePiranha();
						return false;
					} else if (board[nextI][nextJ] == 0) {
						if (fp.createLand(pedroI, pedroJ, nextI, nextJ)) {
							pedroI = nextI;
							nextI = pedroI;
						} else {
							return false;
						}
					}
				} else {
					fp.choosePiranha();
					return false;
				}
			}
		}
		return true;
	}
	
	public void run() {
		while(running) {
			try {
				sleep(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			boolean playTurnSuccessful = true;
			int[] playCards = fp.getPlayCards();
			if ((playCards[0] != -1) && (playCards[1] != -1) && (playCards[2] != -1)) {
				playTurnSuccessful = playTurn();
				if (playTurnSuccessful) {
					fp.changeCurrentPlayer();
					fp.repaintBoard();
				} else {
					fp.resetBoard();
				}
			}
			
		}
	}
}
