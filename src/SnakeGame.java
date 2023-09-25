import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.util.LinkedList;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    private final int WIDTH = 300;
    private final int HEIGHT = 300;
    private final int DOT_SIZE = 10;
    private final int DELAY = 100;

    private boolean inGame = true;
    private Timer timer;
    private int appleX, appleY;
    private int[] x, y;
    private int snakeLength;
    private char direction;

    public SnakeGame() {
        initGame();
        addKeyListener(this);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.black);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void initGame() {
        x = new int[WIDTH * HEIGHT / (DOT_SIZE * DOT_SIZE)];
        y = new int[WIDTH * HEIGHT / (DOT_SIZE * DOT_SIZE)];
        snakeLength = 3;
        direction = 'R';

        for (int i = 0; i < snakeLength; i++) {
            x[i] = 50 - i * DOT_SIZE;
            y[i] = 50;
        }

        spawnApple();
    }

    private void spawnApple() {
        Random rand = new Random();
        appleX = rand.nextInt((WIDTH / DOT_SIZE)) * DOT_SIZE;
        appleY = rand.nextInt((HEIGHT / DOT_SIZE)) * DOT_SIZE;
    }

    private void move() {
        for (int i = snakeLength; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U':
                y[0] -= DOT_SIZE;
                break;
            case 'D':
                y[0] += DOT_SIZE;
                break;
            case 'L':
                x[0] -= DOT_SIZE;
                break;
            case 'R':
                x[0] += DOT_SIZE;
                break;
        }
    }

    private void checkCollision() {
        if (x[0] >= WIDTH || x[0] < 0 || y[0] >= HEIGHT || y[0] < 0) {
            inGame = false;
        }

        for (int i = 1; i < snakeLength; i++) {
            if (x[0] == x[i] && y[0] == y[i]) {
                inGame = false;
            }
        }

        if (x[0] == appleX && y[0] == appleY) {
            snakeLength++;
            spawnApple();
        }
    }

    private void gameOver(Graphics g) {
        String message = "Game Over";
        Font font = new Font("Helvetica", Font.BOLD, 20);
        FontMetrics metrics = getFontMetrics(font);

        g.setColor(Color.white);
        g.setFont(font);
        g.drawString(message, (WIDTH - metrics.stringWidth(message)) / 2, HEIGHT / 2);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (inGame) {
            drawSnake(g);
            drawApple(g);
        } else {
            gameOver(g);
        }
    }

    private void drawSnake(Graphics g) {
        for (int i = 0; i < snakeLength; i++) {
            if (i == 0) {
                g.setColor(Color.green);
            } else {
                g.setColor(Color.green.darker());
            }
            g.fillRect(x[i], y[i], DOT_SIZE, DOT_SIZE);
        }
    }

    private void drawApple(Graphics g) {
        g.setColor(Color.red);
        g.fillOval(appleX, appleY, DOT_SIZE, DOT_SIZE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            move();
            checkCollision();
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if ((key == KeyEvent.VK_LEFT) && (direction != 'R')) {
            direction = 'L';
        }
        if ((key == KeyEvent.VK_RIGHT) && (direction != 'L')) {
            direction = 'R';
        }
        if ((key == KeyEvent.VK_UP) && (direction != 'D')) {
            direction = 'U';
        }
        if ((key == KeyEvent.VK_DOWN) && (direction != 'U')) {
            direction = 'D';
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
