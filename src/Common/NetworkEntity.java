package Common;

import AmusingRectangles.ARect;
import AmusingRectangles.Entity;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Listener;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by podko_000
 * At 18:09 on 12.01.14
 */

public class NetworkEntity extends Listener {
    protected static final int UDP_PORT = 54555;
    protected static final int TCP_PORT = 54556;

    protected static final int ASK_FOR_ENTITIES = -1;
    protected static final int CLIENT_CONNECTED = -2;

    private Boolean operationStarted = false;
    private Boolean operationFinished = false;

    private String protocol;

    public void register(Kryo kryo) {
        kryo.register(Packet.class);
        kryo.register(Entity.class);
        kryo.register(Entity[].class);
        kryo.register(ARect.class);
        kryo.register(AuthResponse.class);
    }

    protected void startOperation() {
        synchronized (this) {
            operationStarted = true;
        }
    }

    protected void finishOperation() {
        synchronized (this) {
            operationStarted = false;
            operationFinished = true;
        }
    }

    public void await() {
        if (!operationStarted) { return; } else {
            while (!operationFinished) {
                try {
                    Thread.currentThread().sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}