import java.awt.Color;
import java.awt.Graphics;

public class Bullet {
	
	public int posx;
	public int posy;

	public Bullet(int x, int y) {
		this.posx = x;
		this.posy = y;
	}

	public void render(Graphics g) {

		g.setColor(new Color(100, 100, 100));
		g.fillOval(posx, posy, 10, 10);

		g.setColor(new Color(200, 200, 200));
		g.fillOval(posx + 1, posy + 1, 8, 8);

	}

}
