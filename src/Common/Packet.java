package Common;

/**
 * Created by podko_000
 * At 18:08 on 12.01.14
 */

public class Packet {
    public int x, y, width, height;
    public long id;
    public int owner;

    @Override
    public String toString() {
        return String.format("{x=%d y=%d} {width=%d height=%d}", x, y, width, height);
    }
}