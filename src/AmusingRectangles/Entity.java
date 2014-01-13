package AmusingRectangles;

import java.awt.*;
import java.io.Serializable;

/**
 * Created by Alexey
 * At 7:12 PM on 1/12/14
 */

public class Entity {
    public static int serialId;

    protected transient Field field;

    private int id;
    private int x, y;
    private int width, height;
    private int owner;

    public Entity() {
        init();
    }

    public Entity(Field field, int x, int y, int width, int height) {
        this.field = field;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        init();
    }

    public void init() {
        id = ++serialId;
    }

    public Rectangle getBB() {
        return new Rectangle(x, y, width, height);
    }

    public boolean intersects(Point point) {
        return getBB().contains(point);
    }

    public boolean intersects(Rectangle rectangle) {
        return rectangle.intersects(getBB());
    }

    public void move(int dx, int dy) {
        setX(getX() + dx);
        setY(getY() + dy);
    }

    // ===================
    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }
}