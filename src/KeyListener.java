import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyListener extends KeyAdapter {
	public static boolean leftPressed = false;
	public static boolean rightPressed = false;
	public static boolean firePressed = false;
	public static boolean fireDisabled = false;
	public static boolean enterPressed = false;
	public static boolean bonusPressed = false;

	public void keyPressed(KeyEvent e) {
		if ((e.getKeyCode() == KeyEvent.VK_A)
				|| (e.getKeyCode() == KeyEvent.VK_LEFT))
			leftPressed = true;

		if ((e.getKeyCode() == KeyEvent.VK_D)
				|| (e.getKeyCode() == KeyEvent.VK_RIGHT))
			rightPressed = true;

		if (e.getKeyCode() == KeyEvent.VK_SPACE)
			firePressed = true;

		if (e.getKeyCode() == KeyEvent.VK_ENTER)
			enterPressed = true;

		if (e.getKeyCode() == KeyEvent.VK_DOWN)
			bonusPressed = true;
	}

	public void keyReleased(KeyEvent e) {
		if ((e.getKeyCode() == KeyEvent.VK_A)
				|| (e.getKeyCode() == KeyEvent.VK_LEFT))
			leftPressed = false;

		if ((e.getKeyCode() == KeyEvent.VK_D)
				|| (e.getKeyCode() == KeyEvent.VK_RIGHT))
			rightPressed = false;

		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			firePressed = false;
			fireDisabled = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_DOWN)
			bonusPressed = false;
	}
}
