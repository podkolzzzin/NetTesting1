package AmusingRectangles;

import java.awt.*;

public class ARect extends Entity {
    public static final int WIDTH = 36;
    public static final int HEIGHT = WIDTH * 3 / 4;

    public ARect(int x, int y) {
        super(x, y, WIDTH, HEIGHT);
    }

    public void render(Graphics g) {
        g.setColor(Color.green);
        g.fillRect(getX(), getY(), getWidth(), getHeight());
    }
}