package AmusingRectangles;

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

        addRect(-1, 400 + random.nextInt(128), 200 + random.nextInt(96), false);
    }

    public void render(Graphics g) {
        for (Iterator it = rects.entrySet().iterator(); it.hasNext(); ) {
            ARect rect = (ARect) ((Map.Entry) it.next()).getValue();
            rect.render(g);
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
//            rect.setColor(colors[id / 10000]);
        } else {
//            rect.setColor(Color.green);
        }

        rects.put(rect.getId(), rect);

        if (!fromReceiver) {
            component.getClient().send(rect);
        }
    }

    public HashMap<Integer, ARect> getRects() {
        return rects;
    }
}