import javax.swing.*;
import java.awt.*;
public class MyPanel extends JPanel {

    private char[][] board;
    
    MyPanel(char[][] board) {
        this.board = board;
        this.setPreferredSize(new Dimension(1000, 1000));
    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;

        // Draw the board
        g2D.setPaint(Color.blue);
        g2D.setStroke(new BasicStroke(5));

        // Vertical lines
        g2D.drawLine(333, 100, 333, 900);
        g2D.drawLine(666, 100, 666, 900);

        // Horizontal lines
        g2D.drawLine(100, 333, 900, 333);
        g2D.drawLine(100, 666, 900, 666);

        // Draw X and O shapes
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int x = 100 + j * 283;
                int y = 100 + i * 283;
                if (board[i][j] == 'X') {
                    drawX(g2D, x, y);
                } else if (board[i][j] == 'O') {
                    drawO(g2D, x, y);
                }
            }
        }
    }

    private void drawX(Graphics2D g2D, int x, int y) {
        g2D.setColor(Color.red);
        g2D.setStroke(new BasicStroke(10));
        g2D.drawLine(x + 20, y + 20, x + 213, y + 213);
        g2D.drawLine(x + 213, y + 20, x + 20, y + 213);
    }

    private void drawO(Graphics2D g2D, int x, int y) {
        g2D.setColor(Color.black);
        g2D.setStroke(new BasicStroke(10));
        g2D.drawOval(x + 20, y + 20, 200, 200);
    }

    public void setBoard(char[][] board) {
        this.board = board;
        repaint();
    }
}
