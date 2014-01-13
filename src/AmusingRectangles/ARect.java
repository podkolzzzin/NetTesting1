package AmusingRectangles;

import java.awt.*;

public class ARect extends Entity {
    public static final int WIDTH = 120;
    public static final int HEIGHT = WIDTH * 3 / 4;

    private int rotation;

    public ARect(Field field, int x, int y) {
        super(field, x, y, WIDTH, HEIGHT);

        this.rotation = -field.getRandom().nextInt(360);
    }

    public void update() {
        int speed = 8;
        double angle = Math.toRadians(-rotation);
        int dx = (int) (speed * Math.cos(angle));
        int dy = (int) (speed * Math.sin(angle));

        move(dx, dy);
        field.getComponent().getClient().update(this);

        if (getX() <= 0 || getX() >= Component.WIDTH - WIDTH) {
            rotation = 180 - rotation;
        }

        if (getY() <= 0 || getY() + HEIGHT >= Component.HEIGHT) {
            rotation = -rotation;
        }
    }

    public void render(Graphics g) {
        g.setColor(Color.green);
        g.fillRect(getX(), getY(), getWidth(), getHeight());
    }
}