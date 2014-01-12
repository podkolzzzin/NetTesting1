package AmusingRectangles;

import Common.Console;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class Field {
    private Component component;
    private Random random = new Random();

    private HashMap<Integer, ARect> rects = new HashMap<Integer, ARect>();

    public Field(Component component) {
        this.component = component;
    }

    public void init() {
        addRect(-1, 400 + random.nextInt(128), 200 + random.nextInt(96), false);
    }

    private int prevX, prevY;

    public void update() {
        InputHandler input = component.getInput();

        ARect rect = getElementAt(input.getMouseX(), input.getMouseY());

        if (!input.isMousePressed()) {
            prevX = input.getMouseX();
            prevY = input.getMouseY();
        }

        if (input.isMousePressed() && rect != null) {
            int dx = input.getMouseX() - prevX;
            int dy = input.getMouseY() - prevY;

            Console.writeLine((dx) + ", " + (dy));

            rect.setX(input.getMouseX() - dx);
            rect.setY(input.getMouseY() - dy);

            prevX = input.getMouseX();
            prevY = input.getMouseY();
        }
    }

    public void render(Graphics g) {
        synchronized (rects) {
            for (ARect rect : rects.values()) {
                rect.render(g);
            }
        }
    }

    public ARect getElementAt(int x, int y) {
        synchronized (rects) {
            for (ARect rect : rects.values()) {
                if (rect.intersects(new Point(x, y))) {
                    return rect;
                }
            }
        }
        return null;
    }

    // ===============
    // Data processing

    public void addRect(int id, int x, int y, boolean fromReceiver) {
        ARect rect = new ARect(x, y);

        if (id != -1) {
            rect.setId(id);
        }

        rects.put(rect.getId(), rect);

        if (!fromReceiver) {
            component.getClient().send(rect);
        }

        Console.writeLine("New rect (" + rect.getId() + "): " + rect.getX() + ", " + rect.getY() + " => " + rects.size());
    }

    public HashMap<Integer, ARect> getRects() {
        return rects;
    }
}