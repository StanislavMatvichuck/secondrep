import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	boolean running = false;
	private Player player = new Player();
	private KeyListener button = new KeyListener();
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	private ArrayList<Wall> walls = new ArrayList<Wall>();
	private ArrayList<Bonus> bonuses = new ArrayList<Bonus>();
	private int score;
	private int lastscore;
	private int level;
	private boolean gameOver = false;

	private int fastCannon = 0;
	private int playerSpeed = 1;

	private int wallMoveTimer;
	private int wallGenTimer;

	public void game() {
		Dimension size = new Dimension(300, 430); // dimensions
		setMinimumSize(size);
		setPreferredSize(size);
		setMaximumSize(size);
		addKeyListener(button); // key listening
	}

	public void run() {
		while (running) {
			update();
			render();
			try {
				Thread.sleep(8);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public void start() {
		running = true;
		new Thread(this).start();
	}

	public void stop() {
		running = false;
	}

	public void update() {

		if (gameOver == true) {
			return; // if game is over - do nothing
		}
		if (KeyListener.leftPressed)
			player.posx-= playerSpeed; // left
		if (KeyListener.rightPressed)
			player.posx+=playerSpeed; // right

		if ((KeyListener.firePressed) && (KeyListener.fireDisabled == false)) { // fire

			bullets.add(new Bullet(player.posx + 10, 360)); // add bullet

			if (fastCannon < 0){
				KeyListener.fireDisabled = true; // block SPACE
			} else {
				KeyListener.fireDisabled = false;
			}
		}
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).posy -= 4; // bullets moving

			if (bullets.get(i).posy < -10)
				bullets.remove(i); // flew away bullets deleting
		}

		// walls moving
		if (wallMoveTimer == 0) {
			for (int i = 0; i < walls.size(); i++) {
				walls.get(i).posy++; // walls move by 1 px
				if (walls.get(i).posy >= 340)
					gameOver = true; // game over rule
			}
			// walls generation
			if (wallGenTimer == 0) {
				for (int i = 0; i < 10; i++) {
					walls.add(new Wall(i * 30, -30));
				}
				wallGenTimer = 30;
			}
			wallGenTimer--;
			// end walls generation

			wallMoveTimer = 10 - level;
		}
		wallMoveTimer--;
		// end walls moving

		// walls deleting

		for (int b = 0; b < bullets.size(); b++) {
			for (int w = 0; w < walls.size(); w++) {
				if (bullets.get(b).posx < walls.get(w).posx + 30) {
					if (bullets.get(b).posx + 10 > walls.get(w).posx) {
						if (bullets.get(b).posy <= walls.get(w).posy + 30) {

							if (Math.random() > 0.98) {
								bonuses.add(new Bonus(walls.get(w).posx + 5,
										walls.get(w).posy + 5));
							} // gererate bonus

							walls.remove(w); // delete wall
							bullets.remove(b); // delete bullet
							score++; // score increment

							if (score > lastscore)
								lastscore = score; // set max score

							break;
						}
					}
				}

			}
		}
		// levels
		if (score >= 5)
			level = 1;
		if (score >= 120)
			level = 2;
		if (score >= 200)
			level = 3;
		if (score >= 350)
			level = 4;
		if (score >= 500)
			level = 5;
		if (score >= 650)
			level = 6;
		if (score >= 700)
			level = 7;

		for (int i = 0; i < bonuses.size(); i++) {
			bonuses.get(i).update(); // bonuses moving

			if (bonuses.get(i).posy > 350) {
				if ((player.posx < bonuses.get(i).posx + 20)
						&& (player.posx + 30 > bonuses.get(i).posx)) {
					fastCannon = 100; // enable fast cannon
					bonuses.remove(i); // delete bonus
				}
			}
		}
		fastCannon--;
		if(fastCannon > 0){
			playerSpeed = 3;
		} else {
			playerSpeed = 1;
		}
	}

	public void render() {

		BufferStrategy b = getBufferStrategy();
		if (b == null) {
			createBufferStrategy(2);
			return;
		}
		Graphics g = b.getDrawGraphics();

		g.setColor(new Color(160, 160, 160));
		g.fillRect(0, 0, 300, 400); // bg

		g.setColor(new Color(140, 140, 140));
		g.fillRect(0, 400, 300, 30); // bottom
		g.setColor(new Color(90, 90, 90));
		g.drawLine(0, 400, 300, 400); // dark line
		g.setColor(new Color(210, 210, 210));
		g.drawLine(0, 401, 300, 401); // bright line

		g.setColor(new Color(240, 240, 240));
		g.drawString("Score is " + score, 5, 420); // score draw
		g.drawString("Level " + level, 255, 420); // level draw

		g.setColor(new Color(100, 100, 100));
		g.drawString("High " + lastscore, 200, 420); // best score draw

		player.render(g); // player render

		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).render(g); // bullets render
		}
		for (int i = 0; i < walls.size(); i++) {
			walls.get(i).render(g); // walls render
		}

		for (int i = 0; i < bonuses.size(); i++) {
			bonuses.get(i).render(g);
		}

		if (gameOver) { // if game is over!
			g.setColor(new Color(160, 160, 160));
			g.fillRect(0, 0, 300, 400); // bg

			g.setColor(new Color(140, 140, 140));
			g.fillRect(0, 400, 300, 30); // bottom
			g.setColor(new Color(90, 90, 90));
			g.drawLine(0, 400, 300, 400); // dark line
			g.setColor(new Color(210, 210, 210));
			g.drawLine(0, 401, 300, 401); // bright line

			g.setColor(new Color(240, 240, 240));
			g.drawString("Game over! Your high score is " + lastscore, 20, 420); // when
																					// game
																					// is
																					// over

			g.setColor(new Color(100, 100, 100));
			g.drawString("Press Enter to retry", 20, 390);

			score = 0; // clean
			level = 0; // clean
			wallGenTimer = 0; // clean
			wallMoveTimer = 0; // clean
			for (int i = 0; i < bullets.size(); i++) {
				bullets.remove(i); // clean
			}
			for (int i = 0; i < walls.size(); i++) {
				walls.remove(i); // clean
			}
			player.posx = 135; // player posx clean

			if (KeyListener.enterPressed) {
				gameOver = false; // new game start
				KeyListener.enterPressed = false;
			}

		}

		g.dispose();
		b.show();
	}

	public static void main(String[] args) {
		Game game = new Game();
		JFrame frame = new JFrame("Square Launcher");

		game.game();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLayout(new BorderLayout());
		frame.add(game, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.requestFocus();

		game.start();

	}

}
