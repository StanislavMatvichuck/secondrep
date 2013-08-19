import java.awt.Color;
import java.awt.Graphics;

public class Wall {
	public int posx, posy;

	public Wall(int x, int y) {
		this.posx = x;
		this.posy = y;
	}

	public void render(Graphics g) {
		g.setColor(new Color(150, 150, 150));
		g.fillRect(posx, posy, 30, 30);
		g.setColor(new Color(120, 120, 120));
		g.fillRect(posx + 2, posy + 2, 26, 26);
	}

}
