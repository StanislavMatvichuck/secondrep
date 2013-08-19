import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

public class Player extends Canvas {
	private static final long serialVersionUID = 1L;
	
	public int posx = 135;

	public void render(Graphics g) {
		if (posx < 0)
			posx = 0;
		if (posx > 270)
			posx = 270;

		g.setColor(new Color(110, 110, 110));
		g.fillRect(posx, 370, 30, 30);
		g.setColor(new Color(200, 200, 200));
		g.fillRect(posx + 2, 372, 26, 26);
	}

}
