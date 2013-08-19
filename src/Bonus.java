import java.awt.Color;
import java.awt.Graphics;

public class Bonus {
	public int posx, posy;

	public Bonus(int x, int y) {
		this.posx = x;
		this.posy = y;
	}

	public void update() {
		if (posy < 380)
			posy++;
	}

	public void render(Graphics g) {
		g.setColor(new Color(0, 172, 206));
		g.fillRect(posx, posy, 20, 20);

		g.setColor(new Color(0, 213, 255));
		g.fillOval(posx + 3, posy + 3, 12, 12);
	}
}
