package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.Timer;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements KeyListener {
    private static final int True = 0;
    Timer gameTimer;
    ArrayList<Bullet> bullets = new ArrayList<>();

    public GamePanel() {
        setFocusable(true); // allow panel to receive keyboard input
        addKeyListener(this); // listen to keyboard events
        setBackground(Color.BLACK); // set background color

        // timer to update game state every 50 ms
        gameTimer = new Timer(50, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playerScore++; // increase score as time passes
                System.out.println("Score updated: " + playerScore);

                // 10% chance to spawn a new bullet at a random horizontal position
                if (Math.random() < 0.1) {
                    int x = (int) (Math.random() * (getWidth() - 20));
                    bullets.add(new Bullet(x, 0, 10, 10, 25));
                    System.out.println("New bullet created at x: " + x);
                }

                // move each bullet downward by its speed
                for (Bullet b : bullets) {
                    b.y += b.speed;
                    System.out.println("Bullet moved to y: " + b.y);
                }

                // create a rectangle representing the player for collision detection
                Rectangle playerRect = new Rectangle(playerX, playerY, playerWidth, playerHeight);

                // check if any bullet collides with the player
                for (Bullet b : bullets) {
                    Rectangle bulletRect = new Rectangle(b.x, b.y, b.width, b.height);
                    if (playerRect.intersects(bulletRect)) {
                        gameTimer.stop(); // stop the game loop
                        System.out.println("Game Over! Player hit by bullet.");
                        canPlayerMove = false; // stop player movement
                        isGameOver = true; // set game over flag
                        break;
                    }
                }

                repaint(); // request redraw of the screen
            }
        });
        gameTimer.start(); // start the game loop timer
    }

    int playerX = 385; // initial player horizontal position
    int playerY = 495; // fixed vertical position (player can't move up/down)
    int playerHeight = 50;
    int playerWidth = 50;
    int playerSpeed = 20; // speed of horizontal movement
    int playerScore = 0;
    boolean canPlayerMove = true; // true if player is allowed to move
    boolean isGameOver = false; // true if game is over

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // draw the player as a blue rectangle
        g.setColor(Color.BLUE);
        g.fillRect(playerX, playerY, playerWidth, playerHeight);

        // draw player position text
        g.setColor(Color.WHITE);
        g.drawString("X: " + playerX + " Y: Player Y Axis Cant be changed", 10, 20);

        // draw the current score in green
        g.setColor(Color.GREEN);
        g.drawString("SCORE: " + playerScore, 10, 35);

        // draw all bullets as red ovals
        g.setColor(Color.RED);
        for (Bullet b : bullets) {
            g.fillOval(b.x, b.y, b.width, b.height);
        }

        // if game is over, display game over messages
        if (isGameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.setColor(Color.BLUE);
            String gameOverText = "GAME OVER!";
            int gameOverX = (getWidth() - g.getFontMetrics().stringWidth(gameOverText)) / 2;
            g.drawString(gameOverText, gameOverX, getHeight() / 2);

            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.setColor(Color.GREEN);
            String scoreText = "Final Score: " + playerScore;
            int scoreX = (getWidth() - g.getFontMetrics().stringWidth(scoreText)) / 2;
            g.drawString(scoreText, scoreX, getHeight() / 2 + 50);

            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.setColor(Color.YELLOW);
            String restartText = "Press R to Restart";
            int restartX = (getWidth() - g.getFontMetrics().stringWidth(restartText)) / 2;
            g.drawString(restartText, restartX, getHeight() / 2 + 90);
        }
    }

    public void keyPressed(KeyEvent e) {
        int pressedKey = e.getKeyCode();
        System.out.println("Key Pressed: " + pressedKey);

        if (canPlayerMove) {
            // move player left
            if (pressedKey == KeyEvent.VK_LEFT) {
                playerX = playerX - playerSpeed;
                System.out.println("Player moved left to X: " + playerX);
            }
            // move player right
            else if (pressedKey == KeyEvent.VK_RIGHT) {
                playerX = playerX + playerSpeed;
                System.out.println("Player moved right to X: " + playerX);

                // keep player inside panel boundaries
                if (playerX < 0) playerX = 0;
                if (playerX + playerWidth > getWidth()) playerX = getWidth() - playerWidth;
            }
        }

        // if game is over and player presses 'R', restart the game
        if (!canPlayerMove && e.getKeyCode() == KeyEvent.VK_R) {
            System.out.println("Restart key pressed, restarting game...");
            restartGame();
            return;
        }

        repaint();
    }

    public void keyReleased(KeyEvent e) {}

    public void keyTyped(KeyEvent e) {}

    // inner class representing a bullet
    public class Bullet {
        int x, y, width, height, speed;

        public Bullet(int x, int y, int width, int height, int speed) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.speed = speed;
        }
    }

    // resets game state to start a new game
    public void restartGame() {
        System.out.println("Restarting game...");
        bullets.clear();
        playerX = 385;
        playerY = 495;
        playerScore = 0;
        isGameOver = false;
        canPlayerMove = true;
        gameTimer.start();
        repaint();
    }
}
