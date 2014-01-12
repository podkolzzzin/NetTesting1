package AmusingRectangles;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class InputHandler implements MouseMotionListener, KeyListener, MouseListener {
    private int prevMouseX, prevMouseY;
    private int mouseX, mouseY;

    private boolean mouseIn = false;

    public class Key {
        private int presses, absorbs;
        private boolean down = false;
        private boolean clicked = false;

        public Key() {
            keys.add(this);
        }

        public void toggle(boolean pressed) {
            if (pressed != down) {
                down = pressed;
            }
            if (pressed) {
                presses++;
            }
        }

        public void update() {
            if (absorbs < presses) {
                absorbs++;
                clicked = true;
            } else {
                clicked = false;
            }
        }

        public boolean isDown() {
            return this.down;
        }

        public boolean isClicked() {
            return this.clicked;
        }
    }

    private ArrayList<Key> keys = new ArrayList<Key>();

    public Key left = new Key();
    public Key right = new Key();
    public Key up = new Key();
    public Key down = new Key();
    public Key space = new Key();

    public Key r = new Key();

    public InputHandler(Component component) {
        component.addMouseListener(this);
        component.addMouseMotionListener(this);
        component.addKeyListener(this);
    }

    public void update() {
        for (Key key : keys) {
            key.update();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) { left.toggle(true); }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) { right.toggle(true); }
        if (e.getKeyCode() == KeyEvent.VK_UP) { up.toggle(true); }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) { down.toggle(true); }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) { space.toggle(true); }

        if (e.getKeyCode() == KeyEvent.VK_R) { r.toggle(true); }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) { left.toggle(false); }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) { right.toggle(false); }
        if (e.getKeyCode() == KeyEvent.VK_UP) { up.toggle(false); }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) { down.toggle(false); }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) { space.toggle(false); }

        if (e.getKeyCode() == KeyEvent.VK_R) { r.toggle(false); }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        mouseIn = true;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        mouseIn = false;
    }

    public boolean hasMouseMoved() {
        boolean t = prevMouseX != mouseX || prevMouseY != mouseY;
        prevMouseX = mouseX;
        prevMouseY = mouseY;
        return t;
    }

    public int getMouseX() {
        return this.mouseX;
    }

    public int getMouseY() {
        return this.mouseY;
    }

    public boolean isMouseIn() {
        return this.mouseIn;
    }
}