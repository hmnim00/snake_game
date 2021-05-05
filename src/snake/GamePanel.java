package snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

  // variables
  static final int SCREEN_WIDTH = 600;
  static final int SCREEN_HEIGHT = 600;
  static final int UNIT_SIZE = 25;
  static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
  // speed
  static final int DELAY = 75;
  // coordinates
  final int x[] = new int[GAME_UNITS];
  final int y[] = new int[GAME_UNITS];
  // initial body
  int bodyParts = 6;
  // apples
  int applesEaten;
  // position
  int appleX;
  int appleY;
  // Direction "R"=> Right, "L" => Left, "U" => Up, "D" => Down
  char direction = 'R';
  boolean running = false;
  Timer timer;
  Random random;

  GamePanel() {
    random = new Random();
    this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
    this.setBackground(Color.black);
    this.setFocusable(true);
    this.addKeyListener(new MyKeyAdapter());

    startGame();
  }

  // Methods
  public void startGame() {
    newApple();
    running = true;
    timer = new Timer(DELAY, this);
    timer.start();
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    draw(g);
  }

  public void draw(Graphics g) {
    if (running) {
      // Matrix
      for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
        //for (int j = 0; j < SCREEN_WIDTH / UNIT_SIZE; j ++)
        g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
        g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
      }

      // draw apple
      g.setColor(Color.red);
      g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

      // draw snake
      for (int i = 0; i < bodyParts; i++) {
        // header of the snake
        if (i == 0) {
          g.setColor(Color.green);
          g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
        } else {
          g.setColor(new Color(45, 180, 0));
          g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
          g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
        }
      }

      // score
      /*
      g.setColor(Color.red);
      g.setFont(new Font("Ink Free", Font.BOLD, 40));
      FontMetrics metrics = getFontMetrics(g.getFont());
      g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
      */
    } else {
      gameOver(g);
    }

  }

  public void newApple() {
    // generate coordinates
    appleX = random.nextInt((int)(SCREEN_WIDTH / UNIT_SIZE))*UNIT_SIZE;
    appleY = random.nextInt((int)(SCREEN_HEIGHT / UNIT_SIZE))*UNIT_SIZE;
  }

  public void move() {
    // move the snake
    for (int i = bodyParts; i > 0; i--) {
      x[i] = x[i-1];
      y[i] = y[i-1];
    }

    switch (direction) {
      case 'U':
        y[0] = y[0] - UNIT_SIZE;
        break;
      case 'D':
        y[0] = y[0] + UNIT_SIZE;
        break;
      case 'L':
        x[0] = x[0] - UNIT_SIZE;
        break;
      case 'R':
        x[0] = x[0] + UNIT_SIZE;
        break;
    }

  }

  public void checkApple() {

    // increment body
    if ((x[0] == appleX) && (y[0] == appleY)) {
      bodyParts++;
      applesEaten++;
      newApple();
    }

  }

  public void checkCollisions() {
    // head collides with body
    for (int i = bodyParts; i > 0; i--) {
      if ((x[0] == x[i]) && (y[0] == y[i])) {
        running = false;
      }
    }

    // head collides with border left
    if (x[0] < 0) {
      running = false;
    }
    // head collides with border right
    if(x[0] > SCREEN_WIDTH) {
      running = false;
    }
    // head collides with border top
    if(y[0] < 0) {
      running = false;
    }
    // head collides with border bottom
    if(y[0] > SCREEN_HEIGHT) {
      running = false;
    }

    if(!running) {
      timer.stop();
    }

  }

  public void gameOver(Graphics g) {
    // final score
    g.setColor(Color.red);
    g.setFont(new Font("Ink Free", Font.BOLD, 40));
    FontMetrics scoreMetrics = getFontMetrics(g.getFont());
    g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - scoreMetrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());

    // game over text
    g.setColor(Color.red);
    g.setFont(new Font("Ink Free", Font.BOLD, 75));
    FontMetrics metrics = getFontMetrics(g.getFont());
    g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);

  }

  @Override
  public void actionPerformed(ActionEvent e) {

    if (running) {
      move();
      checkApple();
      checkCollisions();
    }

    repaint();

  }

  public class MyKeyAdapter extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {

      switch(e.getKeyCode()) {
        case KeyEvent.VK_LEFT:
          if (direction != 'R')
            direction = 'L';
          break;
        case KeyEvent.VK_RIGHT:
          if (direction != 'L')
            direction = 'R';
          break;
        case KeyEvent.VK_UP:
          if (direction != 'D')
            direction = 'U';
          break;
        case KeyEvent.VK_DOWN:
          if (direction != 'U')
            direction = 'D';
          break;
      }

    }
  }

}
