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

    public void render(Graphics g) {
        synchronized (rects) {
            for (Iterator it = rects.entrySet().iterator(); it.hasNext(); ) {
                ARect rect = (ARect) ((Map.Entry) it.next()).getValue();
                rect.render(g);
            }
        }
    }

    // ===============
    // Data processing

    private Color[] colors = new Color[]{
            Color.ORANGE, Color.RED, Color.MAGENTA, Color.CYAN,
            Color.WHITE, Color.GREEN, Color.GRAY, Color.PINK
    };

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