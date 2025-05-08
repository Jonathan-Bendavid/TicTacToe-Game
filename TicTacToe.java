import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.Scanner;

import javax.swing.*;

public class TicTacToe {
    private char[][] board;
    private static final char PLAYER = 'X';
    private static final char AI = 'O';
    private JFrame frame;
    private MyPanel panel;
    private Random rand = new Random();
	static Scanner input = new Scanner(System.in);
    

    public TicTacToe() {
        board = new char[3][3];
        frame = new JFrame();
        panel = new MyPanel(board);
        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void updateDisplay() {
        panel.setBoard(board);
        panel.repaint();
    }

    public static boolean isLegal(int x, int y, char[][] board) {
        return x >= 0 && x < 3 && y >= 0 && y < 3 && board[x][y] == '\u0000';
    }

    public static boolean equals3(char a, char b, char c) {
		return a == b && b == c && a != '\u0000';
	}

	public int checkGameState() {
		for (int i = 0; i < 3; i++) {
			if (equals3(board[i][0], board[i][1], board[i][2])) {
				return board[i][0] == AI ? 100 : -100;
			}
			if (equals3(board[0][i], board[1][i], board[2][i])) {
				return board[0][i] == AI ? 100 : -100;
			}
		}
		if (equals3(board[0][0], board[1][1], board[2][2])) {
			return board[0][0] == AI ? 100 : -100;
		}
		if (equals3(board[0][2], board[1][1], board[2][0])) {
			return board[0][2] == AI ? 100 : -100;
		}

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (board[i][j] == '\u0000') {
					return 0;
				}
			}
		}
		return 1;
	}

	public int[] aiMove(int diff) {
		if (diff == 1) {
			return randomMove();
		} else if (diff == 2) {
			if (rand.nextDouble() < 0.4) {
				return bestAiMove();
			} else {
				return randomMove();
			}
		} else {
			return bestAiMove();
		}
	}

	public int[] randomMove() {
		int[] move = {-1, -1};
		boolean found = false;
		while (!found) {
			move[0] = rand.nextInt(3);
			move[1] = rand.nextInt(3);
			if (board[move[0]][move[1]] == '\u0000') {
				found = true;
			}
		}
		return move;
	}

	public int[] bestAiMove() {
		int bestVal = Integer.MIN_VALUE;
		int[] bestMove = {-1, -1};
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (board[i][j] == '\u0000') {
					board[i][j] = AI;
					int moveVal = alphaBeta(9, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
					board[i][j] = '\u0000';
					if (moveVal > bestVal) {
						bestMove[0] = i;
						bestMove[1] = j;
						bestVal = moveVal;
					}
				}
			}
		}
		return bestMove;
	}

	private int alphaBeta(int depth, int alpha, int beta, boolean isAI) {
		int score = checkGameState();
		if (score != 0 || depth < 0)
			return score;
		if (isAI) {
			int maxScore = Integer.MIN_VALUE;
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if (board[i][j] == '\u0000') {
						board[i][j] = AI;
						maxScore = Math.max(maxScore, alphaBeta(depth - 1, alpha, beta, false));
						alpha = Math.max(alpha, maxScore);
						board[i][j] = '\u0000';
						if (beta <= alpha)
							break;
					}
				}
			}
			return maxScore;
		} else {
			int minScore = Integer.MAX_VALUE;
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if (board[i][j] == '\u0000') {
						board[i][j] = PLAYER;
						minScore = Math.min(minScore, alphaBeta(depth - 1, alpha, beta, true));
						beta = Math.min(beta, minScore);
						board[i][j] = '\u0000';
						if (beta <= alpha)
							break;
					}
				}
			}
			return minScore;
		}
	}

    public void playTicTacToe(int diff) throws InterruptedException {
        int gameState = 0;
        boolean isPlayerTurn = Math.random() >= 0.5;
        while (gameState == 0) {
            if (isPlayerTurn) {
            	PlayerMove();
            } else {
                int[] bestMove = aiMove(diff);
                board[bestMove[0]][bestMove[1]] = AI;
                updateDisplay();
            }
            isPlayerTurn = !isPlayerTurn;
            gameState = checkGameState();
        }

        Thread.sleep(2000);
        endGame(gameState);
    }

    private void PlayerMove() throws InterruptedException {
        final boolean[] legal = {false};
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                int row = mouseY / (panel.getHeight() / 3);
                int col = mouseX / (panel.getWidth() / 3);
                if (isLegal(row, col, board)) {
                    board[row][col] = PLAYER;
                    updateDisplay();
                    legal[0] = true;
                }
            }
        });
        while (!legal[0]) {
            Thread.sleep(100);
        }
        panel.removeMouseListener(panel.getMouseListeners()[0]);
    }

    private void endGame(int gameState) throws InterruptedException {
		frame.getContentPane().removeAll(); // Clear the frame
		JLabel messageLabel = new JLabel();
		if (gameState == 1) {
			messageLabel.setText("It's A Draw");
		} else if (gameState == -100) {
			messageLabel.setText("You Win");
		} else {
			messageLabel.setText("You Lost");
		}
		messageLabel.setFont(messageLabel.getFont().deriveFont(Font.PLAIN, 40)); 
		messageLabel.setHorizontalAlignment(SwingConstants.CENTER); 
		messageLabel.setVerticalAlignment(SwingConstants.CENTER); 
		frame.getContentPane().add(messageLabel, BorderLayout.CENTER); 
		frame.revalidate();
		frame.repaint();
		Thread.sleep(2000);
		frame.getContentPane().removeAll(); // Clear the frame
		frame.getContentPane().add(panel, BorderLayout.CENTER); 
		frame.revalidate();
		frame.repaint();
		board = new char[3][3]; 
		updateDisplay();
	}

    public static void main(String[] args) throws InterruptedException {
        TicTacToe game = new TicTacToe();
        
        while (true) {
        	int diff = Integer.parseInt(JOptionPane.showInputDialog("Enter Difficulty: 1 for Easy, 2 for Medium, 3 for Impossible"));
            game.playTicTacToe(diff);
        }
    }
}
