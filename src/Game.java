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
	private int score;
	private int lastscore;
	private int level;
	private boolean gameOver = false;

	private int wallMoveTimer;
	private int wallGenTimer;

	public void game() {
		Dimension size = new Dimension(300, 430); // размеры
		setMinimumSize(size);
		setPreferredSize(size);
		setMaximumSize(size);
		addKeyListener(button); // считывание клавиш
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
			return;
		}
		if (KeyListener.leftPressed)
			player.posx--; // влево
		if (KeyListener.rightPressed)
			player.posx++; // вправо
		if ((KeyListener.firePressed) && (KeyListener.fireDisabled == false)) { // огонь
			bullets.add(new Bullet(player.posx + 10, 360)); // добавить снаряд
			KeyListener.fireDisabled = true; // заблокировать пушку
		}
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).posy -= 4; // движение снярядов

			if (bullets.get(i).posy < -10)
				bullets.remove(i); // удаление вылетевшего сняряда
		}

		// движение стен
		if (wallMoveTimer == 0) {
			for (int i = 0; i < walls.size(); i++) {
				walls.get(i).posy++; // передвижение стен на 1 вниз
				if (walls.get(i).posy >= 340)
					gameOver = true; // условие проигрыша
			}
			// генерация стен
			if (wallGenTimer == 0) {
				for (int i = 0; i < 10; i++) {
					walls.add(new Wall(i * 30, -30));
				}
				wallGenTimer = 30;
			}
			wallGenTimer--;
			// !геренация стен

			wallMoveTimer = 10 - level;
		}
		wallMoveTimer--;
		// !движение стен

		// уничтожение стен

		for (int b = 0; b < bullets.size(); b++) {
			for (int w = 0; w < walls.size(); w++) {
				if (bullets.get(b).posx < walls.get(w).posx + 30) {
					if (bullets.get(b).posx + 10 > walls.get(w).posx) {
						if (bullets.get(b).posy <= walls.get(w).posy + 30) {
							walls.remove(w); // удаление стены
							bullets.remove(b); // удаление снаряда
							score++; // увеличение результата
							if (score > lastscore)
								lastscore = score; // присваивание максимального
													// результата
							break;
						}
					}
				}

			}
		}
		// увеличение уровня
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

	}

	public void render() {

		BufferStrategy b = getBufferStrategy();
		if (b == null) {
			createBufferStrategy(2);
			return;
		}
		Graphics g = b.getDrawGraphics();

		g.setColor(new Color(160, 160, 160));
		g.fillRect(0, 0, 300, 400); // фон

		g.setColor(new Color(140, 140, 140));
		g.fillRect(0, 400, 300, 30); // нижняя часть
		g.setColor(new Color(90, 90, 90));
		g.drawLine(0, 400, 300, 400); // темная линия
		g.setColor(new Color(210, 210, 210));
		g.drawLine(0, 401, 300, 401); // светлая линия

		g.setColor(new Color(240, 240, 240));
		g.drawString("Score is " + score, 5, 420); // вывод счета
		g.drawString("Level " + level, 255, 420); // вывод уровня

		g.setColor(new Color(100, 100, 100));
		g.drawString("High " + lastscore, 200, 420); // лучший счет

		player.render(g); // отображение игрока

		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).render(g); // отображение снарядов
		}
		for (int i = 0; i < walls.size(); i++) {
			walls.get(i).render(g); // отображение стен
		}

		if (gameOver) { // Если игра закончена!
			g.setColor(new Color(160, 160, 160));
			g.fillRect(0, 0, 300, 400); // фон

			g.setColor(new Color(140, 140, 140));
			g.fillRect(0, 400, 300, 30); // нижняя часть
			g.setColor(new Color(90, 90, 90));
			g.drawLine(0, 400, 300, 400); // темная линия
			g.setColor(new Color(210, 210, 210));
			g.drawLine(0, 401, 300, 401); // светлая линия

			g.setColor(new Color(240, 240, 240));
			g.drawString("Game over! Your high score is " + lastscore, 20, 420); // Когда
																					// игра
																					// окончена

			g.setColor(new Color(100, 100, 100));
			g.drawString("Press Enter to retry", 20, 390);

			score = 0; // обнуление
			level = 0; // обнуление
			wallGenTimer = 0; // обнуление
			wallMoveTimer = 0; // обнуление
			for (int i = 0; i < bullets.size(); i++) {
				bullets.remove(i); // обнуление
			}
			for (int i = 0; i < walls.size(); i++) {
				walls.remove(i); // обнуление
			}
			player.posx = 135; // исходная позиция игрока

			if (KeyListener.enterPressed) {
				gameOver = false; // запуск новой игры
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
