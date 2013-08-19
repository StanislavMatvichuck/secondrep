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
		Dimension size = new Dimension(300, 430); // �������
		setMinimumSize(size);
		setPreferredSize(size);
		setMaximumSize(size);
		addKeyListener(button); // ���������� ������
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
			player.posx--; // �����
		if (KeyListener.rightPressed)
			player.posx++; // ������
		if ((KeyListener.firePressed) && (KeyListener.fireDisabled == false)) { // �����
			bullets.add(new Bullet(player.posx + 10, 360)); // �������� ������
			KeyListener.fireDisabled = true; // ������������� �����
		}
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).posy -= 4; // �������� ��������

			if (bullets.get(i).posy < -10)
				bullets.remove(i); // �������� ����������� �������
		}

		// �������� ����
		if (wallMoveTimer == 0) {
			for (int i = 0; i < walls.size(); i++) {
				walls.get(i).posy++; // ������������ ���� �� 1 ����
				if (walls.get(i).posy >= 340)
					gameOver = true; // ������� ���������
			}
			// ��������� ����
			if (wallGenTimer == 0) {
				for (int i = 0; i < 10; i++) {
					walls.add(new Wall(i * 30, -30));
				}
				wallGenTimer = 30;
			}
			wallGenTimer--;
			// !��������� ����

			wallMoveTimer = 10 - level;
		}
		wallMoveTimer--;
		// !�������� ����

		// ����������� ����

		for (int b = 0; b < bullets.size(); b++) {
			for (int w = 0; w < walls.size(); w++) {
				if (bullets.get(b).posx < walls.get(w).posx + 30) {
					if (bullets.get(b).posx + 10 > walls.get(w).posx) {
						if (bullets.get(b).posy <= walls.get(w).posy + 30) {
							walls.remove(w); // �������� �����
							bullets.remove(b); // �������� �������
							score++; // ���������� ����������
							if (score > lastscore)
								lastscore = score; // ������������ �������������
													// ����������
							break;
						}
					}
				}

			}
		}
		// ���������� ������
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
		g.fillRect(0, 0, 300, 400); // ���

		g.setColor(new Color(140, 140, 140));
		g.fillRect(0, 400, 300, 30); // ������ �����
		g.setColor(new Color(90, 90, 90));
		g.drawLine(0, 400, 300, 400); // ������ �����
		g.setColor(new Color(210, 210, 210));
		g.drawLine(0, 401, 300, 401); // ������� �����

		g.setColor(new Color(240, 240, 240));
		g.drawString("Score is " + score, 5, 420); // ����� �����
		g.drawString("Level " + level, 255, 420); // ����� ������

		g.setColor(new Color(100, 100, 100));
		g.drawString("High " + lastscore, 200, 420); // ������ ����

		player.render(g); // ����������� ������

		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).render(g); // ����������� ��������
		}
		for (int i = 0; i < walls.size(); i++) {
			walls.get(i).render(g); // ����������� ����
		}

		if (gameOver) { // ���� ���� ���������!
			g.setColor(new Color(160, 160, 160));
			g.fillRect(0, 0, 300, 400); // ���

			g.setColor(new Color(140, 140, 140));
			g.fillRect(0, 400, 300, 30); // ������ �����
			g.setColor(new Color(90, 90, 90));
			g.drawLine(0, 400, 300, 400); // ������ �����
			g.setColor(new Color(210, 210, 210));
			g.drawLine(0, 401, 300, 401); // ������� �����

			g.setColor(new Color(240, 240, 240));
			g.drawString("Game over! Your high score is " + lastscore, 20, 420); // �����
																					// ����
																					// ��������

			g.setColor(new Color(100, 100, 100));
			g.drawString("Press Enter to retry", 20, 390);

			score = 0; // ���������
			level = 0; // ���������
			wallGenTimer = 0; // ���������
			wallMoveTimer = 0; // ���������
			for (int i = 0; i < bullets.size(); i++) {
				bullets.remove(i); // ���������
			}
			for (int i = 0; i < walls.size(); i++) {
				walls.remove(i); // ���������
			}
			player.posx = 135; // �������� ������� ������

			if (KeyListener.enterPressed) {
				gameOver = false; // ������ ����� ����
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
