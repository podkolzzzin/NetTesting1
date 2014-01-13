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
        addRect(-1, 400 + random.nextInt(128), 200 + random.nextInt(96), -1, false);
    }

    private int prevX, prevY;
    private ARect draggedRect;

    public void update() {
        /*InputHandler input = component.getInput();

        if (!input.isMousePressed()) {
            prevX = input.getMouseX();
            prevY = input.getMouseY();
            draggedRect = null;
        }

        if (input.isMousePressed() && draggedRect == null) {
            draggedRect = getElementAt(input.getMouseX(), input.getMouseY());
        }

        if (input.isMousePressed() && draggedRect != null) {
            int dx = input.getMouseX() - prevX;
            int dy = input.getMouseY() - prevY;

            draggedRect.setX(draggedRect.getX() + dx);
            draggedRect.setY(draggedRect.getY() + dy);

            prevX = input.getMouseX();
            prevY = input.getMouseY();

            component.getClient().update(draggedRect);
        }*/

        synchronized (rects) {
            for (ARect rect : rects.values()) {
                rect.update();
            }
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

    public void addRect(int id, int x, int y, int owner, boolean fromReceiver) {
        ARect rect = new ARect(this, x, y);

        if (id != -1) {
            rect.setId(id);
        }

        if (owner != -1) {
            rect.setOwner(owner);
        } else {
            rect.setOwner(component.getUserId());
        }

        synchronized (rects) {
            rects.put(rect.getId(), rect);
        }

        Console.writeLine("Rect(owner=" + rect.getOwner() + ", id=" + rect.getId() + ")");

        if (!fromReceiver) {
            component.getClient().send(rect);
        }
    }

    public void removeAllByOwner(int userId) {
        synchronized (rects) {
            for (Iterator it = rects.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<Integer, ARect> pair = (Map.Entry) it.next();
                if (pair.getValue().getOwner() == userId) {
                    it.remove();
                }
            }
        }
    }

    // =======
    // Getters

    public HashMap<Integer, ARect> getRects() {
        return rects;
    }

    public Component getComponent() {
        return component;
    }

    public Random getRandom() {
        return random;
    }
}